package tech.theroyalraj.gpt.rest;

import lombok.RequiredArgsConstructor;
import tech.theroyalraj.gpt.model.request.ChatRequest;
import tech.theroyalraj.gpt.model.request.TranscriptionRequest;
import tech.theroyalraj.gpt.model.response.ChatGPTResponse;
import tech.theroyalraj.gpt.model.response.WhisperTranscriptionResponse;
import tech.theroyalraj.gpt.service.OpenAIClientService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class OpenAIClientController {

    private final OpenAIClientService openAIClientService;

    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChatGPTResponse chat(@RequestBody ChatRequest chatRequest){
        return openAIClientService.chat(chatRequest);
    }

    @PostMapping(value = "/transcription", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public WhisperTranscriptionResponse createTranscription(@ModelAttribute TranscriptionRequest transcriptionRequest){
        return openAIClientService.createTranscription(transcriptionRequest);
    }
}
