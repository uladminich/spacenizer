package com.minich.project.training.spacenizer.model.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Card {
    private String name;
    private String description;
    private int redProduction;
    private int redConsumption;
    private String cardId;

    public Card(CardType type) {
        name = type.getName();
        description = type.getDescription();
        redProduction = type.getRedProduction();
        redConsumption = type.getRedConsumption();
    }
}
