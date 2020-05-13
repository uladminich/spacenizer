package com.minich.project.training.spacenizer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    public static final String GLOBAL_PLAYER_ID = "GLOBAL";
    private String boardId;
    private String name;
    private boolean isCreator;
    private boolean isActiveTurn;
    private boolean isAlive = true;
    private boolean hasOneRoundCard;
    private List<Card> availableCards;
    private List<Card> activeCards;
    private int redAmount;
    private int redConsumption;
    private int redProduction;
    private int blueAmount;
    private int blueConsumption;
    private int blueProduction;
    private int changeCardAmount;
}
