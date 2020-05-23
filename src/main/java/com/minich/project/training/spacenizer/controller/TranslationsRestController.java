package com.minich.project.training.spacenizer.controller;

import com.minich.project.training.spacenizer.core.service.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class TranslationsRestController {

    @Autowired
    private TranslationService translationService;

    @RequestMapping(value = "/api/v1/spacenizer/localization", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getDictionary(@RequestParam(required = false) String lang) {
        Map<String, String> dictionary = translationService.getDictionaryByLocale(lang);
        ResponseEntity answer = ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(dictionary);
        log.info(answer.toString());
        return answer;
    }

}
