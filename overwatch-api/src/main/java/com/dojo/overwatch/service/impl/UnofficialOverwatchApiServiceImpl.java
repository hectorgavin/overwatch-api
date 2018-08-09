package com.dojo.overwatch.service.impl;

import com.dojo.overwatch.exception.AbilityNotFoundException;
import com.dojo.overwatch.exception.HeroNotFoundException;
import com.dojo.overwatch.mapper.AbilityMapper;
import com.dojo.overwatch.mapper.HeroMapper;
import com.dojo.overwatch.model.Ability;
import com.dojo.overwatch.model.Hero;
import com.dojo.overwatch.model.PageableAbilities;
import com.dojo.overwatch.model.PageableHeros;
import com.dojo.overwatch.model.unofficialApi.HeroWithAbilities;
import com.dojo.overwatch.service.OverwatchService;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class UnofficialOverwatchApiServiceImpl implements OverwatchService {

    private static final Logger LOG = LoggerFactory.getLogger(UnofficialOverwatchApiServiceImpl.class);

    private static final String BASE_ENDPOINT = "https://overwatch-api.net/api/v1";
    private static final String HEROS_ENDPOINT = BASE_ENDPOINT + "/hero";
    private static final String ABILITIES_ENDPOINT = BASE_ENDPOINT + "/ability";

    private static final String HERO_BY_ID_ENDPOINT = HEROS_ENDPOINT + "/{heroId}";
    private static final String ABILITY_BY_ID_ENDPOINT = ABILITIES_ENDPOINT + "/{abilityId}";

    private final HeroMapper heroMapper;
    private final AbilityMapper abilityMapper;
    private final RestTemplate restTemplate;


    public UnofficialOverwatchApiServiceImpl(final RestTemplate restTemplate,
                                             final HeroMapper heroMapper,
                                             final AbilityMapper abilityMapper) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new Ignore404ErrorHandler());
        this.heroMapper = heroMapper;
        this.abilityMapper = abilityMapper;
    }

    @Override
    public PageableHeros getHeros(final Integer page, final Integer limit) {
        final com.dojo.overwatch.model.unofficialApi.PageableHeros pageableHeros =
            restTemplate.getForObject(
                    constructPaginatedUrl(HEROS_ENDPOINT, page, limit),
                    com.dojo.overwatch.model.unofficialApi.PageableHeros.class
            );

        return heroMapper.toPageableHeros(pageableHeros);
    }

    @Override
    public Hero getHeroById(@NonNull final Integer heroId) {
        return Optional.of(
                restTemplate.getForEntity(
                        HERO_BY_ID_ENDPOINT,
                        com.dojo.overwatch.model.unofficialApi.Hero.class,
                        heroId
                )
        )
        .filter(response -> response.getStatusCode() == HttpStatus.OK)
        .map(HttpEntity::getBody)
        .map(heroMapper::toHero)
        .orElseThrow(() -> {
            LOG.error("Unofficial Overwatch API could not find hero with id "+ heroId);
            return new HeroNotFoundException("Could not find hero with id " + heroId);
        });
    }

    @Override
    public List<Ability> getHeroAbilitiesByHeroId(@NonNull final Integer heroId) {
        return Optional.of(
                restTemplate.getForEntity(
                        HERO_BY_ID_ENDPOINT,
                        HeroWithAbilities.class,
                        heroId
                )
        )
        .filter(response -> response.getStatusCode() == org.springframework.http.HttpStatus.OK)
        .map(HttpEntity::getBody)
        .map(fullHero -> fullHero.getAbilities().stream().map(abilityMapper::toAbility).collect(collectingAndThen(toList(), ImmutableList::copyOf)))
        .orElseThrow(() -> {
            LOG.error("Unofficial Overwatch API could not find hero with id "+ heroId);
            return new HeroNotFoundException("Could not find hero with id " + heroId);
        });
    }

    @Override
    public PageableAbilities getAbilities(final Integer page, final Integer limit) {
        final com.dojo.overwatch.model.unofficialApi.PageableAbilities pageableAbilities =
                restTemplate.getForObject(
                        constructPaginatedUrl(ABILITIES_ENDPOINT, page, limit),
                        com.dojo.overwatch.model.unofficialApi.PageableAbilities.class
                );

        return abilityMapper.toPageableAbilities(pageableAbilities);
    }

    @Override
    public Ability getAbilityById(@NonNull final Integer abilityId) {
        return Optional.of(
                restTemplate.getForEntity(
                        ABILITY_BY_ID_ENDPOINT,
                        com.dojo.overwatch.model.unofficialApi.Ability.class,
                        abilityId
                )
        )
        .filter(response -> response.getStatusCode() == HttpStatus.OK)
        .map(HttpEntity::getBody)
        .map(abilityMapper::toAbility)
        .orElseThrow(() -> {
            LOG.error("Unofficial Overwatch API could not find ability with id "+ abilityId);
            return new AbilityNotFoundException("Could not find ability with id "+ abilityId);
        });
    }

    private String constructPaginatedUrl(@NonNull final String url, final Integer page, final Integer limit) {
        final UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url);
        if (page != null) {
            urlBuilder.queryParam("page", page);
        }
        if (limit != null) {
            urlBuilder.queryParam("limit", limit);
        }
        return urlBuilder.build().toUriString();
    }

    public static class Ignore404ErrorHandler extends DefaultResponseErrorHandler {

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            if (response.getStatusCode() != HttpStatus.NOT_FOUND) {
                super.handleError(response);
            }
        }
    }

}
