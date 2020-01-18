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

    private String id;
    private String name;
    private String description;
    private int redProduction;
    private int redConsumption;

    public Card(CardType type) {
        id = type.getId();
        name = type.getName();
        description = type.getDescription();
        redProduction = type.getRedProduction();
        redConsumption = type.getRedConsumption();
    }
}
