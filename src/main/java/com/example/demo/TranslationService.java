package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TranslationService {

    @Value("${google.api.key}")
    private String apiKey;

    private static final String API_URL = "https://translation.googleapis.com/language/translate/v2";

    public String translate(String sourceLang, String targetLang, String text) {
        try {
            // Creating the URL for the API request
            String url = String.format("%s?key=%s&q=%s&source=%s&target=%s",
                    API_URL, apiKey, text, sourceLang, targetLang);

            // Making the API request and getting the response
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            // Parsing the JSON response and extracting the translated text
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            String translatedText = rootNode.path("data").path("translations").get(0).path("translatedText").asText();

            return translatedText;
        } catch (Exception e) {
            // Handling exceptions like I/O exception during JSON parsing
            e.printStackTrace();
            return "Error processing translation response";
        }
    }
}
