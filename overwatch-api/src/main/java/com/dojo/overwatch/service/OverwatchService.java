package com.dojo.overwatch.service;

import com.dojo.overwatch.model.PageableAbilities;
import com.dojo.overwatch.model.Ability;
import com.dojo.overwatch.model.Hero;
import com.dojo.overwatch.model.PageableHeros;

import java.util.List;

public interface OverwatchService {

    PageableHeros getHeros(Integer page, Integer limit);

    Hero getHeroById(Integer heroId);

    List<Ability> getHeroAbilitiesByHeroId(Integer heroId);

    PageableAbilities getAbilities(Integer page, Integer limit);

    Ability getAbilityById(Integer abilityId);

}
