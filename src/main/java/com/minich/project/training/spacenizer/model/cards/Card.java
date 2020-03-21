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

    private String title;
    private String name;
    private String description;
    private String idUI;
    private long id;
    private boolean isActive = true;

    public Card(CardType type) {
        id = type.getId();
        title = type.getTitle();
        name = type.getName();
        description = type.getDescription();
    }
}
