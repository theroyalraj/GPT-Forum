package tech.theroyalraj.gpt.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import tech.theroyalraj.gpt.model.request.ChatMessage;
import tech.theroyalraj.gpt.model.request.ChatMessage.MessageType;
import tech.theroyalraj.gpt.model.request.ChatRequest;
import tech.theroyalraj.gpt.model.request.Message;
import tech.theroyalraj.gpt.model.response.ChatGPTResponse;
import tech.theroyalraj.gpt.service.OpenAIClientService;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    
    private final OpenAIClientService openAIClientService;
    
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public List<ChatMessage> sendMessage(@Payload ChatMessage chatMessage) {
    	List<ChatMessage> listOfmessage =  new ArrayList<>();
		logger.info("" + chatMessage);
		if (chatMessage.getType().equals(MessageType.CHAT)) {
			ChatRequest chatGPTReq = new ChatRequest();
			chatGPTReq.setQuestion("Imagine you are connected to same forum and "+chatMessage.getSender() + " texted \"" + chatMessage.getContent()
					+ "\" on the forum. Reply to this text");
			ChatGPTResponse chat = openAIClientService.chat(chatGPTReq);
			Message gptMessage = chat.getChoices().get(0).getMessage();
			ChatMessage gptMessResponse = new ChatMessage();
			gptMessResponse.setContent(gptMessage.getContent());
			gptMessResponse.setSender("GPT Bot");
			gptMessResponse.setType(ChatMessage.MessageType.BOT);
			listOfmessage.add(gptMessResponse);
		}else {
			listOfmessage.add(chatMessage);
		}
        return listOfmessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public List<ChatMessage> addUser(@Payload ChatMessage chatMessage, 
                               SimpMessageHeaderAccessor headerAccessor) {
    	List<ChatMessage> listOfmessage =  new ArrayList<>();
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		listOfmessage.add(chatMessage);
        return listOfmessage;
    }

}
