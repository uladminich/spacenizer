package com.minich.project.training.spacenizer.core.service;

import java.util.Map;

public interface TranslationService {

    Map<String, String> getDictionaryByLocale(String lang);

}
