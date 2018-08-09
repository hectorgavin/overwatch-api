package com.dojo.overwatch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Hero {
    private final Integer id;
    private final String name;
    private final String realName;
    private final Integer health;
    private final Integer armour;
    private final Integer shield;
}
