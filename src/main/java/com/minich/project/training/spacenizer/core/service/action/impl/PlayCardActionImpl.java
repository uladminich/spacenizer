package com.minich.project.training.spacenizer.core.service.action.impl;

import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(GameAction.PLAY_CARD)
public class PlayCardActionImpl implements GameAction {

    @Override
    public Board doAction(Board state) {
        String fromPlayerId = state.getAction().getFromPlayer();
        String toPlayerId = state.getAction().getToPlayer();
        String fromCardId = state.getAction().getFromCard();
//        String toCardId = state.getAction().getFromCard(); TODO if card play to another card

        List<Player> players = state.getPlayers();
        Player fromPlayer = getPlayerById(fromPlayerId, players);
        Player toPlayer = fromPlayerId.equals(toPlayerId) ? fromPlayer : getPlayerById(toPlayerId, players);

        Card fromCard = getAvailableCardById(fromCardId, fromPlayer);

        fromPlayer.getAvailableCards().remove(fromCard);
        toPlayer.getActiveCards().add(fromCard);

        toPlayer.addRedConsumption(fromCard.getRedConsumption());
        toPlayer.addRedProduction(fromCard.getRedProduction());
        updateNegativeValueWithZero(toPlayer);

        state.getAction().setName(GameAction.PLAY_CARD_FINISHED);
        return state;
    }

    private Player getPlayerById(String id, List<Player> players) {
        return players.stream()
                .filter(p -> id.equals(p.getName()))
                .findFirst()
                .orElse(null);
    }

    private Card getAvailableCardById(String id, Player players) {
        return players.getAvailableCards()
                .stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void updateNegativeValueWithZero(Player player){
        if (player.getRedProduction() < 0) {
            player.setRedConsumption(0);
        }

        if (player.getRedConsumption() < 0) {
            player.setRedConsumption(0);
        }
    }
}
