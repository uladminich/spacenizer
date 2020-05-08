package com.minich.project.training.spacenizer.core.service.action.impl;

import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.utils.CardUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service(GameAction.PLAY_CARD)
public class PlayCardActionImpl implements GameAction {


    @Override
    public Board doAction(Board state) {
        String fromPlayerId = state.getAction().getFromPlayer();
        String toPlayerId = state.getAction().getToPlayer();
        String fromCardIdUI = state.getAction().getFromCard();
//        String toCardId = state.getAction().getFromCard(); TODO if card play to another card

        List<Player> players = state.getPlayers();
        Player fromPlayer = getPlayerById(fromPlayerId, players);
        Player toPlayer;
        boolean isGlobalCardPlayed = Player.GLOBAL_PLAYER_ID.equals(toPlayerId);
        if(isGlobalCardPlayed) {
            toPlayer = state.getGlobalPlayer();
        } else {
            toPlayer = fromPlayerId.equals(toPlayerId) ? fromPlayer : getPlayerById(toPlayerId, players);
        }
        Card fromCard = getAvailableCardById(fromCardIdUI, fromPlayer);

        fromPlayer.getAvailableCards().remove(fromCard);
        toPlayer.getActiveCards().add(fromCard);
        if (isGlobalCardPlayed) {
            toPlayer.setActiveCards(toPlayer.getActiveCards().stream().distinct().collect(Collectors.toList()));
        }
        setNotActiveCardIfRequired(toPlayer, fromCard);

        state.getAction().setDescription(StringUtils.EMPTY);
        state.getAction().setName(GameAction.PLAY_CARD_FINISHED);
        return state;
    }

    private void setNotActiveCardIfRequired(Player toPlayer, Card fromCard) {
        if (fromCard != null && CardUtils.isOnePerPlayerCard(fromCard.getId()) &&  CardUtils.isPlayerHasAlreadyActiveCard(fromCard.getId(), toPlayer)) {
            fromCard.setActive(false);
        }
    }
}