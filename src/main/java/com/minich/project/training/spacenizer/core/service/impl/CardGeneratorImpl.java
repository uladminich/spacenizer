package com.minich.project.training.spacenizer.core.service.impl;

import com.google.common.collect.ImmutableMap;
import com.minich.project.training.spacenizer.core.service.CardGenerator;
import com.minich.project.training.spacenizer.model.cards.CardType;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class CardGeneratorImpl implements CardGenerator {
    private static final int UNIQUE_GLOBAL_CARD_AMOUNT = 6;
    private static final Random RANDOM = new Random();

    private static final Map<Integer, CardType> UNIQUE_CARS_MAP = ImmutableMap.<Integer, CardType>builder()
            .put(0, CardType.BAR)
            .put(1, CardType.LABORATORY)
            .put(2, CardType.MINE_RED)
            .put(3, CardType.ROAD)
            .put(4, CardType.WASTE_RECYCLE)
            .put(5, CardType.NANO_TECHNOLOGIES)
            .put(6, CardType.ADVERSE_TERRAIN)
            .put(7, CardType.ROBOTS)
            .put(8, CardType.SUN_BATTERY)
            .put(9, CardType.WIND_GENERATOR)
            .put(10, CardType.RESOURCE_CONVERTER)
            .put(11, CardType.MINE_BLUE)
            .put(12, CardType.HOME_HELP_RESOURCES)
            .put(13, CardType.HOME_HELP_CARD)
            .put(14, CardType.FIRE_DISASTER)
            .put(15, CardType.DISEASE_OUTBREAK)
            .put(16, CardType.INDUSTRIAL_ACTION)
            .put(17, CardType.HIGH_PRODUCTION)
            .build();

    private static final Map<Integer, CardType> UNIQUE_GLOBAL_CARS_MAP = ImmutableMap.<Integer, CardType>builder()
            .put(0, CardType.DANGEROUS_WORLD)
            .put(1, CardType.ICE_WORLD)
            .put(2, CardType.RICH_MINERAL_DEPOSIT)
            .put(3, CardType.EARTHQUAKES)
            .build();

    @Override
    public CardType getRandomCardType() {
        int cardIndex = RANDOM.nextInt(UNIQUE_CARS_MAP.size());
        return UNIQUE_CARS_MAP.get(cardIndex);
    }

    @Override
    public Optional<CardType> getRandomGlobalCardType() {
        /*
        * Actual amount of global cards - 4.
        * Get random from 0 to 7.
        * Chance to no global cards game - 2/6.
        **/
        int cardIndex = RANDOM.nextInt(UNIQUE_GLOBAL_CARD_AMOUNT);
        return Optional.ofNullable(UNIQUE_GLOBAL_CARS_MAP.get(cardIndex));
    }


}
