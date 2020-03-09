package com.minich.project.training.spacenizer.utils;

import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.CardType;

import java.util.Arrays;
import java.util.Optional;

public final class CardUtils {

    private CardUtils() {

    }

    public static CardType getCardTypeById(long cardTypeId) {
        Optional<CardType> optType = Arrays.stream(CardType.values())
                .filter(c -> c.getId() == cardTypeId).findFirst();
        return optType.orElse(null);
    }

    public static boolean isPlayerHasActiveCard(long cardTypeId, Player player) {
        return player.getActiveCards().stream().anyMatch(card -> card.getId() == cardTypeId);
    }
}
