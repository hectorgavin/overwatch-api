package com.dojo.overwatch.model.unofficialApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ability {

    private Integer id;

    private String name;

    private String description;

    @JsonProperty("is_ultimate")
    private Boolean isUltimate;

}
