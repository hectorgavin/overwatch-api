package com.dojo.overwatch.mapper;

import com.dojo.overwatch.common.response.HeroResponse;
import com.dojo.overwatch.common.response.PageableHerosResponse;
import com.dojo.overwatch.model.Hero;
import com.dojo.overwatch.model.PageableHeros;
import com.dojo.overwatch.model.persistence.HeroEntity;
import com.dojo.overwatch.utils.HttpUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Component
public class HeroMapper {

    public HeroResponse toHeroResponse(@NonNull final Hero hero) {
        return new HeroResponse(
                hero.getId(),
                hero.getName(),
                hero.getRealName(),
                hero.getHealth(),
                hero.getArmour(),
                hero.getShield()
        );
    }

    public PageableHerosResponse toHerosResponse(@NonNull final PageableHeros heros) {
        return new PageableHerosResponse(
                heros.getNextPage(),
                heros.getTotal(),
                heros.getElements().stream().map(this::toHeroResponse).collect(collectingAndThen(toList(), ImmutableList::copyOf))
        );
    }

    public Hero toHero(@NonNull final com.dojo.overwatch.model.unofficialApi.Hero hero) {
        return new Hero(
                hero.getId(),
                hero.getName(),
                hero.getRealName(),
                hero.getHealth(),
                hero.getArmour(),
                hero.getShield()
        );
    }

    public Hero toHero(@NonNull final HeroEntity hero) {
        return new Hero(
                hero.getId(),
                hero.getName(),
                hero.getRealName(),
                hero.getHealth(),
                hero.getArmour(),
                hero.getShield()
        );
    }

    public HeroEntity toHeroEntity(final Hero hero) {
        return new HeroEntity(
                hero.getId(),
                hero.getName(),
                hero.getRealName(),
                hero.getHealth(),
                hero.getArmour(),
                hero.getShield(),
                Lists.newArrayList()
        );
    }

    public PageableHeros toPageableHeros(@NonNull final com.dojo.overwatch.model.unofficialApi.PageableHeros heros) {
        // Note: nextPage will be the queryParam "page" from the url defined in "next" property
        final Integer nextPage = Optional.ofNullable(heros.getNext())
                .flatMap(url -> HttpUtils.getQueryParam(url, "page"))
                .map(Integer::valueOf)
                .orElse(null);

        return new PageableHeros(
                nextPage,
                heros.getTotal(),
                heros.getData().stream().map(this::toHero).collect(collectingAndThen(toList(), ImmutableList::copyOf))
        );
    }

}
