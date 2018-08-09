package com.dojo.overwatch.service.impl;

import com.dojo.overwatch.mapper.AbilityMapper;
import com.dojo.overwatch.mapper.HeroMapper;
import com.dojo.overwatch.model.Ability;
import com.dojo.overwatch.model.Hero;
import com.dojo.overwatch.model.PageableAbilities;
import com.dojo.overwatch.model.PageableHeros;
import com.dojo.overwatch.model.persistence.AbilityEntity;
import com.dojo.overwatch.model.persistence.HeroEntity;
import com.dojo.overwatch.persistence.AbilityRepository;
import com.dojo.overwatch.persistence.HeroRepository;
import com.dojo.overwatch.service.OverwatchService;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Transactional
public class OverwatchInMemoryDecoratorServiceImpl implements OverwatchService {

    private static final Logger LOG = LoggerFactory.getLogger(OverwatchInMemoryDecoratorServiceImpl.class);

    private final OverwatchService decorated;

    private final HeroRepository heroRepository;
    private final AbilityRepository abilityRepository;

    private final HeroMapper heroMapper;
    private final AbilityMapper abilityMapper;


    public OverwatchInMemoryDecoratorServiceImpl(final HeroRepository heroRepository,
                                                 final AbilityRepository abilityRepository,
                                                 final HeroMapper heroMapper,
                                                 final AbilityMapper abilityMapper,
                                                 final OverwatchService decorated) {
        this.decorated = decorated;
        this.heroRepository = heroRepository;
        this.abilityRepository = abilityRepository;
        this.heroMapper = heroMapper;
        this.abilityMapper = abilityMapper;
    }

    @Override
    public PageableHeros getHeros(final Integer page, final Integer limit) {
        final PageableHeros heros = decorated.getHeros(page, limit);
        heroRepository.saveAll(
                heros.getElements().stream()
                        .map(heroMapper::toHeroEntity)
                        .collect(collectingAndThen(toList(), ImmutableList::copyOf))
        );
        LOG.debug("Persisted {} heros from page {} in memory", heros.getElements().size(), page != null ? page : 1);
        return heros;
    }

    @Override
    public Hero getHeroById(@NonNull final Integer heroId) {
        return heroRepository.findById(heroId)
                .map(hero -> {
                    LOG.debug("Hero with id {} found in memory", heroId);
                    return heroMapper.toHero(hero);
                })
                .orElseGet(() -> {
                    final Hero hero = decorated.getHeroById(heroId);
                    heroRepository.save(heroMapper.toHeroEntity(hero));
                    LOG.debug("Persisted hero with id {} in memory", hero.getId());
                    return hero;
                });
    }

    @Override
    public List<Ability> getHeroAbilitiesByHeroId(@NonNull final Integer heroId) {
        return heroRepository.findById(heroId)
                .map(HeroEntity::getAbilities)
                .filter(abilityEntities -> abilityEntities.size() > 0)
                .map(abilityEntities -> {
                    LOG.debug("Abilities for hero with id {} found in memory", heroId);
                    return abilityEntities.stream().map(abilityMapper::toAbility).collect(toList());
                })
                .orElseGet(() -> {
                    final List<Ability> abilities = decorated.getHeroAbilitiesByHeroId(heroId);
                    final List<AbilityEntity> abilityEntities = abilities.stream()
                            .map(abilityMapper::toAbilityEntity)
                            .collect(collectingAndThen(toList(), ImmutableList::copyOf));

                    abilityRepository.saveAll(abilityEntities);
                    LOG.debug("Persisted {} abilities in memory", abilities.size());

                    return abilities;
                });
    }

    @Override
    public PageableAbilities getAbilities(final Integer page, final Integer limit) {
        final PageableAbilities abilities = decorated.getAbilities(page, limit);
        abilityRepository.saveAll(
                abilities.getElements().stream()
                        .map(abilityMapper::toAbilityEntity)
                        .collect(collectingAndThen(toList(), ImmutableList::copyOf))
        );
        LOG.debug("Persisted {} abilities in memory", abilities.getElements().size());
        return abilities;
    }

    @Override
    public Ability getAbilityById(@NonNull final Integer abilityId) {
        return abilityRepository.findById(abilityId)
                .map(ability -> {
                    LOG.debug("Ability with id {} found in memory", abilityId);
                    return abilityMapper.toAbility(ability);
                })
                .orElseGet(() -> {
                    final Ability ability = decorated.getAbilityById(abilityId);

                    abilityRepository.save(abilityMapper.toAbilityEntity(ability));
                    LOG.debug("Persisted ability with id {} in memory", ability.getId());

                    return ability;
                });
    }

}
