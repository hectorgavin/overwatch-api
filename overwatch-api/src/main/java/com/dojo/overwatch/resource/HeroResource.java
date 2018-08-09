package com.dojo.overwatch.resource;

import com.dojo.overwatch.common.response.AbilityResponse;
import com.dojo.overwatch.common.response.HeroResponse;
import com.dojo.overwatch.common.response.PageableHerosResponse;
import com.dojo.overwatch.mapper.AbilityMapper;
import com.dojo.overwatch.mapper.HeroMapper;
import com.dojo.overwatch.model.Ability;
import com.dojo.overwatch.model.Hero;
import com.dojo.overwatch.model.PageableHeros;
import com.dojo.overwatch.service.OverwatchService;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api/heros")
public class HeroResource {

    private final OverwatchService overwatchService;
    private final HeroMapper heroMapper;
    private final AbilityMapper abilityMapper;

    @Autowired
    public HeroResource(final OverwatchService overwatchService,
                        final HeroMapper heroMapper,
                        final AbilityMapper abilityMapper) {
        this.overwatchService = overwatchService;
        this.heroMapper = heroMapper;
        this.abilityMapper = abilityMapper;
    }

    @GetMapping
    public PageableHerosResponse getHeros(@RequestParam(value = "page", required = false) final Integer page,
                                          @RequestParam(value = "limit", required = false) final Integer limit) {
        final PageableHeros heros = overwatchService.getHeros(page, limit);

        return heroMapper.toHerosResponse(heros);
    }

    @GetMapping(value = "/{heroId}")
    public HeroResponse getHeroById(@PathVariable("heroId") @NonNull Integer heroId) {
        final Hero hero = overwatchService.getHeroById(heroId);

        return heroMapper.toHeroResponse(hero);
    }

    @GetMapping("/{heroId}/abilities")
    public List<AbilityResponse> getHeroAbilities(@PathVariable("heroId") @NonNull Integer heroId) {
        final List<Ability> abilities = overwatchService.getHeroAbilitiesByHeroId(heroId);

        return abilities.stream()
                .map(abilityMapper::toAbilityResponse)
                .collect(collectingAndThen(toList(), ImmutableList::copyOf));
    }

}
