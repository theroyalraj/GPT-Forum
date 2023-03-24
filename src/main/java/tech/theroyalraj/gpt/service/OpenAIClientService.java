package tech.theroyalraj.gpt.service;

import java.util.Collections;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.RequiredArgsConstructor;
import tech.theroyalraj.gpt.config.OpenAIClient;
import tech.theroyalraj.gpt.config.OpenAIClientConfig;
import tech.theroyalraj.gpt.model.request.ChatGPTRequest;
import tech.theroyalraj.gpt.model.request.ChatRequest;
import tech.theroyalraj.gpt.model.request.Message;
import tech.theroyalraj.gpt.model.request.TranscriptionRequest;
import tech.theroyalraj.gpt.model.request.WhisperTranscriptionRequest;
import tech.theroyalraj.gpt.model.response.ChatGPTResponse;
import tech.theroyalraj.gpt.model.response.WhisperTranscriptionResponse;

@Service
@RequiredArgsConstructor
public class OpenAIClientService {

    private final OpenAIClient openAIClient;
    private final OpenAIClientConfig openAIClientConfig;

    private final static String ROLE_USER = "user";

    public ChatGPTResponse chat(ChatRequest chatRequest){
        Message message = Message.builder()
                .role(ROLE_USER)
                .content(chatRequest.getQuestion())
                .build();
        ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder()
                .model(openAIClientConfig.getModel())
                .messages(Collections.singletonList(message))
                .build();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
			String json = ow.writeValueAsString(chatGPTRequest);
			System.err.println(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return openAIClient.chat(chatGPTRequest);
    }

    public WhisperTranscriptionResponse createTranscription(TranscriptionRequest transcriptionRequest){
        WhisperTranscriptionRequest whisperTranscriptionRequest = WhisperTranscriptionRequest.builder()
                .model(openAIClientConfig.getAudioModel())
                .file(transcriptionRequest.getFile())
                .build();
        return openAIClient.createTranscription(whisperTranscriptionRequest);
    }
}
