package com.example.demo.palm;

import com.example.demo.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/promote")
@CrossOrigin(origins = "http://localhost:8080")
public class Palm {

    @Autowired
    private PalmTranslationService palmService;

    @Autowired
    private TranslationService translationService1;

    @GetMapping("/translate")
    public ResponseEntity<String> translateText(@RequestParam String text) {
        System.out.println(text);
        if (text.length() > 500) {
            return new ResponseEntity<>("Text is too long!", HttpStatus.BAD_REQUEST);
        }
        String language=detectLanguage(text);
        if ("Arabic".equals(language)) {
            System.out.println(text);
            String englishTranslation = translationService1.translate("ar","en", text);
            System.out.println(englishTranslation);
//            String translatedText = palmService.translate("i need the ibn sirin interpretation of this dream "+englishTranslation+" and summarize and humanize " +
//                    "i do not need the parts interpretation i need relational humanity interpretation and take in your thought the" +
//                    "what is my is single, young or married  ");
            String dreamDescription = "i need the ibn sirin interpretation of this dream " + englishTranslation + " and summarize and humanize ";
            String detailedRequest = "I do not need the parts interpretation; I need relational humanity interpretation. ";
            String personalContext = "Consider whether the dreamer is single, young, or married in your interpretation.";

            String translatedText = palmService.translate(dreamDescription + detailedRequest + personalContext);

            String arabicTranslation = translationService1.translate("en","ar", translatedText);

            return new ResponseEntity<>(arabicTranslation, HttpStatus.OK);

        } else if ("English".equals(language)) {
            String translatedText = palmService.translate("i need the ibn sirin interpretation of this dream "+text+" and summarize and humanize ");
            String arabicTranslation = translationService1.translate("en","ar", translatedText);

            return new ResponseEntity<>(extractArabicWords(arabicTranslation), HttpStatus.OK);
        } else {
            // If the text is mixed or unknown, handle appropriately
            return new ResponseEntity<>("The text contains mixed languages and cannot be processed.", HttpStatus.BAD_REQUEST);
        }

    }

    public static String extractArabicWords(String text) {
        // Regular expression to match Arabic words
        String arabicWordPattern = "[\\p{InArabic}]+";

        // Splitting the text into words, filtering them using the pattern,
        // and joining them back into a single String
        return Arrays.stream(text.split("\\s+"))
                .filter(word -> word.matches(arabicWordPattern))
                .collect(Collectors.joining(" "));
    }

    public static String detectLanguage(String text) {
        String arabicPattern = "[\\p{InArabic}]";
        String englishPattern = "[a-zA-Z]";

        boolean hasArabic = text.chars().anyMatch(ch -> String.valueOf((char) ch).matches(arabicPattern));
        boolean hasEnglish = text.chars().anyMatch(ch -> String.valueOf((char) ch).matches(englishPattern));

        if (hasArabic && !hasEnglish) {
            return "Arabic";
        } else if (!hasArabic && hasEnglish) {
            return "English";
        } else {
            return "Mixed";
        }
    }
}


