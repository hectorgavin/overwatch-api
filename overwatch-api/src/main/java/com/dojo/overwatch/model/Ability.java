package com.dojo.overwatch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Ability {
    private final Integer id;
    private final String name;
    private final String description;
    private final Boolean isUltimate;
}
