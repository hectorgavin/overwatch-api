package com.dojo.overwatch.resource;

import com.dojo.overwatch.common.response.AbilityResponse;
import com.dojo.overwatch.common.response.PageableAbilitiesResponse;
import com.dojo.overwatch.mapper.AbilityMapper;
import com.dojo.overwatch.model.Ability;
import com.dojo.overwatch.model.PageableAbilities;
import com.dojo.overwatch.service.OverwatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/abilities")
public class AbilityResource {

    private final OverwatchService overwatchService;
    private final AbilityMapper abilityMapper;

    @Autowired
    public AbilityResource(final OverwatchService overwatchService,
                           final AbilityMapper abilityMapper) {
        this.overwatchService = overwatchService;
        this.abilityMapper = abilityMapper;
    }

    @GetMapping
    public PageableAbilitiesResponse getAbilities(@RequestParam(value = "page", required = false) final Integer page,
                                                  @RequestParam(value = "limit", required = false) final Integer limit) {
        final PageableAbilities abilities = overwatchService.getAbilities(page, limit);

        return abilityMapper.toPageableAbilitiesResponse(abilities);
    }

    @GetMapping("/{abilityId}")
    public AbilityResponse getAbilityById(@PathVariable("abilityId") @NonNull final Integer abilityId) {
        final Ability ability = overwatchService.getAbilityById(abilityId);

        return abilityMapper.toAbilityResponse(ability);
    }

}
