package com.example.demo.palm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



import java.io.IOException;

@Service
public class PalmTranslationService {

    @Value("${palm.api.key}")
    private String apiKey;

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta3/models/text-bison-001:generateText";

    public String translate(String text) {
        String url = API_URL + "?key=" + apiKey;
        String requestBody = String.format("{\"prompt\": {\"text\": \"%s\"} }", text);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
        System.out.println(response);

        // Extracting output text from response
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode candidatesNode = rootNode.path("candidates");
            if (candidatesNode.isArray() && candidatesNode.size() > 0) {
                JsonNode outputNode = candidatesNode.get(0).path("output");
                String outputText = outputNode.asText();

                return outputText;
            } else {
                return "Unexpected API response format";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error parsing API response";
        }
    }


}


