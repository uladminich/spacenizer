package com.minich.project.training.spacenizer.controller;

import com.minich.project.training.spacenizer.core.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TranslationsRestController {

    @Autowired
    private TranslationService translationService;

    @RequestMapping(value = "/api/v1/spacenizer/localization", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getDictionary(@RequestParam(required = false) String lang) {
        Map<String, String> dictionary = translationService.getDictionaryByLocale(lang);
        return ResponseEntity.ok(dictionary);
    }

}
