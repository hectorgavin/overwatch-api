package com.dojo.overwatch.model.unofficialApi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeroWithAbilities extends Hero {

    private List<Ability> abilities;

}
