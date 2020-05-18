package com.minich.project.training.spacenizer.core.service.impl;

import com.minich.project.training.spacenizer.core.service.TranslationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Service
public class TranslationServiceImpl implements TranslationService {

    private Map<String, String> ruMessages;
    private Map<String, String> enMessages;

    @PostConstruct
    public void init() {
        ruMessages = getMessageMap(new Locale("ru_RU"));
        enMessages = getMessageMap( Locale.US);
    }

    @Override
    public Map<String, String> getDictionaryByLocale(String lang) {
        if(StringUtils.isEmpty(lang)) {
            lang = "ru";
        }
        switch (lang){
            case "en":
                return enMessages;
            case "ru":
            default:
                return ruMessages;
        }
    }

    private Map<String, String> getMessageMap(Locale locale) {
        ResourceBundle messageBundle = ResourceBundle.getBundle("lang/messages", locale);
        Map<String, String> dictionary = new HashMap<>();
        messageBundle.keySet().forEach( key -> dictionary.put(key, messageBundle.getString(key)));
        return dictionary;
    }

}
