package com.dojo.overwatch.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeroResponse {
    private Integer id;
    private String name;
    private String realName;
    private Integer health;
    private Integer armour;
    private Integer shield;
}
