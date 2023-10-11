package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/translate")
public class TranslateController {
//http://localhost:8080/translate?text=Hello&sourceLang=en&targetLang=ar
    @Autowired
    private TranslationService translationService;

    @GetMapping
    public String translateText(@RequestParam String text,
                                @RequestParam String sourceLang,
                                @RequestParam String targetLang) {
        return translationService.translate(sourceLang, targetLang, text);
    }
}
