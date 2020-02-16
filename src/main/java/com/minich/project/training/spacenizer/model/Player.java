package com.minich.project.training.spacenizer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.minich.project.training.spacenizer.model.cards.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Player implements Serializable {
    private String boardId;
    private String name;
    private boolean isCreator;
    private boolean isActiveTurn;
    private boolean isAlive = true;
    private List<Card> availableCards;
    private List<Card> activeCards;
    private int redAmount;
    private int redConsumption;
    private int redProduction;

    public void addRedConsumption(int val) {
        redConsumption+=val;
    }

    public void addRedProduction(int val) {
        redProduction+=val;
    }
}
