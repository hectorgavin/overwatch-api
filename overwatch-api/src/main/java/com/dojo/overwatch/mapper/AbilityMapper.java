package com.dojo.overwatch.mapper;

import com.dojo.overwatch.common.response.PageableAbilitiesResponse;
import com.dojo.overwatch.common.response.AbilityResponse;
import com.dojo.overwatch.model.PageableAbilities;
import com.dojo.overwatch.model.Ability;
import com.dojo.overwatch.model.persistence.AbilityEntity;
import com.dojo.overwatch.utils.HttpUtils;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Component
public class AbilityMapper {

    public AbilityResponse toAbilityResponse(@NonNull final Ability ability) {
        return new AbilityResponse(
                ability.getId(),
                ability.getName(),
                ability.getDescription(),
                ability.getIsUltimate()
        );
    }

    public PageableAbilitiesResponse toPageableAbilitiesResponse(@NonNull final PageableAbilities abilities) {
        return new PageableAbilitiesResponse(
                abilities.getNextPage(),
                abilities.getTotal(),
                abilities.getElements().stream().map(this::toAbilityResponse).collect(collectingAndThen(toList(), ImmutableList::copyOf))
        );
    }

    public Ability toAbility(@NonNull final com.dojo.overwatch.model.unofficialApi.Ability ability) {
        return new Ability(
                ability.getId(),
                ability.getName(),
                ability.getDescription(),
                ability.getIsUltimate()
        );
    }

    public Ability toAbility(@NonNull final AbilityEntity ability) {
        return new Ability(
                ability.getId(),
                ability.getName(),
                ability.getDescription(),
                ability.getIsUltimate()
        );
    }

    public AbilityEntity toAbilityEntity(final Ability ability) {
        return new AbilityEntity(
                ability.getId(),
                ability.getName(),
                ability.getDescription(),
                ability.getIsUltimate()
        );
    }

    public PageableAbilities toPageableAbilities(@NonNull final com.dojo.overwatch.model.unofficialApi.PageableAbilities abilities) {
        // Note: nextPage will be the queryParam "page" from the url defined in "next" property
        final Integer nextPage = Optional.ofNullable(abilities.getNext())
                .flatMap(url -> HttpUtils.getQueryParam(url, "page"))
                .map(Integer::valueOf)
                .orElse(null);

        return new PageableAbilities(
                nextPage,
                abilities.getTotal(),
                abilities.getData().stream().map(this::toAbility).collect(collectingAndThen(toList(), ImmutableList::copyOf))
        );
    }

}
