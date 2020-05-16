package com.minich.project.training.spacenizer.utils;

import com.google.common.collect.ImmutableList;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.model.cards.CardType;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class CardUtils {

    private static final int DEFAULT_BLUE_TO_RED_CONVERT_COEFF = 2;
    private static final List<CardType> ONE_CARD_PER_PLAYER_LIST = ImmutableList.<CardType>builder()
            .add(CardType.ADVERSE_TERRAIN)
            .add(CardType.NANO_TECHNOLOGIES)
            .add(CardType.ROBOTS)
            .add(CardType.RESOURCE_CONVERTER)
//            .add(CardType.SECURITY_GUARDS)
//            .add(CardType.BARRACK)
            .build();

    private static final List<CardType> BUILDING_CARDS = ImmutableList.<CardType>builder()
            .add(CardType.MINE_RED)
            .add(CardType.MINE_BLUE)
            .add(CardType.BAR)
            .add(CardType.ROAD)
            .add(CardType.LABORATORY)
            .add(CardType.SUN_BATTERY)
            .add(CardType.WIND_GENERATOR)
            .add(CardType.RESOURCE_CONVERTER)
//            .add(CardType.BARRACK)
            .build();

    private CardUtils() {

    }

    public static CardType getCardTypeById(long cardTypeId) {
        Optional<CardType> optType = Arrays.stream(CardType.values())
                .filter(c -> c.getId() == cardTypeId).findFirst();
        return optType.orElse(null);
    }

    public static boolean isPlayerHasActiveCard(long cardTypeId, @NonNull Player player) {
        return player.getActiveCards().stream()
                .anyMatch(card -> card.getId() == cardTypeId);
    }

    public static boolean isOnePerPlayerCard(long cardTypeId) {
        return ONE_CARD_PER_PLAYER_LIST.stream()
                .anyMatch(cardType -> cardType.getId() == cardTypeId);
    }

    public static boolean hasMoreThanOneCardPerPlayer(@NonNull List<Card> availableCards, long cardTypeId) {
        return availableCards.stream()
                .filter(card -> card.getId() == cardTypeId)
                .count() >= 1;
    }

    public static boolean isPlayerHasAlreadyActiveCard(long cardTypeId, @NonNull Player player) {
        return player.getActiveCards().stream()
                .filter(card -> card.getId() == cardTypeId)
                .count() > 1;
    }

    public static boolean isBuildingCard(long cardTypeId) {
        return BUILDING_CARDS.stream()
                .anyMatch(type -> type.getId() == cardTypeId);
    }

    public static int getBlueToRedConversationCoefficient(@NonNull Player player){
        return CardUtils.isPlayerHasActiveCard(CardType.RESOURCE_CONVERTER.getId(), player)
                ? CardType.RESOURCE_CONVERTER.getMultiplier()
                : DEFAULT_BLUE_TO_RED_CONVERT_COEFF;
    }

    public static boolean isRobotsAffectedCards(CardType cardType) {
        return cardType == CardType.MINE_RED
                || cardType == CardType.WASTE_RECYCLE
                || cardType == CardType.LABORATORY
                || cardType == CardType.ROAD
                || cardType == CardType.MINE_BLUE;
    }
}
