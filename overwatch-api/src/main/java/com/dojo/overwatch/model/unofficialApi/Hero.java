package com.dojo.overwatch.model.unofficialApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hero {

    private Integer id;

    private String name;

    @JsonProperty("real_name")
    private String realName;

    private Integer health;

    private Integer armour;

    private Integer shield;

}
