package com.minich.project.training.spacenizer.core.service.impl;

import com.google.common.collect.ImmutableMap;
import com.minich.project.training.spacenizer.core.service.CardGenerator;
import com.minich.project.training.spacenizer.model.cards.CardType;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
public class CardGeneratorImpl implements CardGenerator {
    private static final int UNIQUE_AVAILABLE_CARD_AMOUNT = 9;
    private static final Random RANDOM = new Random();
    private static final Map<Integer, CardType> UNIQUE_CARS_MAP = ImmutableMap.<Integer, CardType>builder()
            .put(0, CardType.BAR)
            .put(1, CardType.LABORATORY)
            .put(2, CardType.MINE)
            .put(3, CardType.ROAD)
            .put(4, CardType.WASTE_RECYCLE)
            .put(5, CardType.NANO_TECHNOLOGIES)
            .put(6, CardType.ADVERSE_TERRAIN)
            .put(7, CardType.ROBOTS)
            .put(8, CardType.DANGEROUS_WORLD)
            .build();

    @Override
    public CardType getRandomCardType() {
        int cardIndex = RANDOM.nextInt(UNIQUE_AVAILABLE_CARD_AMOUNT);
        return UNIQUE_CARS_MAP.get(cardIndex);
    }
}
