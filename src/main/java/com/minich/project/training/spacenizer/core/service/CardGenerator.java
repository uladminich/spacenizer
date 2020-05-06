package com.minich.project.training.spacenizer.core.service;

import com.minich.project.training.spacenizer.model.cards.CardType;

import java.util.Optional;

public interface CardGenerator {

    CardType getRandomCardType();

    Optional<CardType> getRandomGlobalCardType();
}
