package com.minich.project.training.spacenizer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Action implements Serializable {

    private String name;
    private String fromPlayer;
    private String toPlayer;
    private String fromCard;
    private String toCard;
    private String description;
    private String playerActionDescription;

    public Action(String name) {
        this.name = name;
    }
}
