package com.dojo.overwatch.resource;

import com.dojo.overwatch.exception.AbilityNotFoundException;
import com.dojo.overwatch.model.Ability;
import com.dojo.overwatch.model.PageableAbilities;
import com.dojo.overwatch.service.OverwatchService;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AbilityResourceTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OverwatchService overwatchService;

    @Test
    public void testGetAbilityById() throws Exception {
        final Ability ability = new Ability(1, "name", "description", true);
        given(overwatchService.getAbilityById(ability.getId())).willReturn(ability);

        mvc.perform(get("/api/abilities/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ability.getId())))
                .andExpect(jsonPath("$.name", is(ability.getName())))
                .andExpect(jsonPath("$.description", is(ability.getDescription())))
                .andExpect(jsonPath("$.isUltimate", is(ability.getIsUltimate())));
    }

    @Test
    public void testGetAbilityByIdNotFound() throws Exception {
        final String errorMessage = "Ability with id 1 not found";
        given(overwatchService.getAbilityById(1)).willThrow(new AbilityNotFoundException(errorMessage));

        mvc.perform(get("/api/abilities/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    @Test
    public void testGetAbilities() throws Exception {
        final Ability ability = new Ability(1, "name", "description", true);
        final PageableAbilities abilities = new PageableAbilities(2, 10, ImmutableList.of(ability));
        given(overwatchService.getAbilities(null, null)).willReturn(abilities);

        mvc.perform(get("/api/abilities")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPage", is(abilities.getNextPage())))
                .andExpect(jsonPath("$.total", is(abilities.getTotal())))
                .andExpect(jsonPath("$.elements", hasSize(1)))
                .andExpect(jsonPath("$.elements[0].id", is(ability.getId())))
                .andExpect(jsonPath("$.elements[0].name", is(ability.getName())))
                .andExpect(jsonPath("$.elements[0].description", is(ability.getDescription())))
                .andExpect(jsonPath("$.elements[0].isUltimate", is(ability.getIsUltimate())));
    }

    @Test
    public void testGetAbilitiesWithPageAndLimit() throws Exception {
        final Ability ability = new Ability(1, "name", "description", true);
        final PageableAbilities abilities = new PageableAbilities(2, 10, ImmutableList.of(ability));
        given(overwatchService.getAbilities(1, 1)).willReturn(abilities);

        mvc.perform(get("/api/abilities?page=1&limit=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPage", is(abilities.getNextPage())))
                .andExpect(jsonPath("$.total", is(abilities.getTotal())))
                .andExpect(jsonPath("$.elements", hasSize(1)))
                .andExpect(jsonPath("$.elements[0].id", is(ability.getId())))
                .andExpect(jsonPath("$.elements[0].name", is(ability.getName())))
                .andExpect(jsonPath("$.elements[0].description", is(ability.getDescription())))
                .andExpect(jsonPath("$.elements[0].isUltimate", is(ability.getIsUltimate())));
    }

}