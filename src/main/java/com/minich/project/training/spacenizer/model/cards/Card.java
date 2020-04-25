package com.minich.project.training.spacenizer.model.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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
    private boolean isGlobal;

    public Card(CardType type) {
        id = type.getId();
        title = type.getTitle();
        name = type.getName();
        description = type.getDescription();
        isGlobal = type.isGlobal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
