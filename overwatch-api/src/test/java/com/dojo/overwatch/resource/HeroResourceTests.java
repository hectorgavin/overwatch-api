package com.dojo.overwatch.resource;

import com.dojo.overwatch.exception.HeroNotFoundException;
import com.dojo.overwatch.model.Ability;
import com.dojo.overwatch.model.Hero;
import com.dojo.overwatch.model.PageableHeros;
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

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HeroResourceTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OverwatchService overwatchService;

    @Test
    public void testGetHeroById() throws Exception {
        final Hero hero = new Hero(1, "name", "realName", 1, 2, 3);
        given(overwatchService.getHeroById(hero.getId())).willReturn(hero);

        mvc.perform(get("/api/heros/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(hero.getId())))
                .andExpect(jsonPath("$.name", is(hero.getName())))
                .andExpect(jsonPath("$.realName", is(hero.getRealName())))
                .andExpect(jsonPath("$.health", is(hero.getHealth())))
                .andExpect(jsonPath("$.armour", is(hero.getArmour())))
                .andExpect(jsonPath("$.shield", is(hero.getShield())));
    }

    @Test
    public void testGetHeroByIdNotFound() throws Exception {
        final String errorMessage = "Hero with id 1 not found";
        given(overwatchService.getHeroById(1)).willThrow(new HeroNotFoundException(errorMessage));

        mvc.perform(get("/api/heros/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    @Test
    public void testGetHeroAbilities() throws Exception {
        final Ability ability = new Ability(1, "name", "description", true);
        given(overwatchService.getHeroAbilitiesByHeroId(1)).willReturn(Collections.singletonList(ability));

        mvc.perform(get("/api/heros/1/abilities")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(ability.getId())))
                .andExpect(jsonPath("$[0].name", is(ability.getName())))
                .andExpect(jsonPath("$[0].description", is(ability.getDescription())))
                .andExpect(jsonPath("$[0].isUltimate", is(ability.getIsUltimate())));
    }

    @Test
    public void testGetHeroAbilitiesWhenHeroNotFound() throws Exception {
        final String errorMessage = "Hero with id 1 not found";
        given(overwatchService.getHeroAbilitiesByHeroId(1)).willThrow(new HeroNotFoundException(errorMessage));

        mvc.perform(get("/api/heros/1/abilities")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    @Test
    public void testGetHeros() throws Exception {
        final Hero hero = new Hero(1, "name", "realName", 1, 2, 3);
        final PageableHeros heros = new PageableHeros(2, 10, ImmutableList.of(hero));
        given(overwatchService.getHeros(null, null)).willReturn(heros);

        mvc.perform(get("/api/heros")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPage", is(heros.getNextPage())))
                .andExpect(jsonPath("$.total", is(heros.getTotal())))
                .andExpect(jsonPath("$.elements", hasSize(1)))
                .andExpect(jsonPath("$.elements[0].id", is(hero.getId())))
                .andExpect(jsonPath("$.elements[0].name", is(hero.getName())))
                .andExpect(jsonPath("$.elements[0].realName", is(hero.getRealName())))
                .andExpect(jsonPath("$.elements[0].health", is(hero.getHealth())))
                .andExpect(jsonPath("$.elements[0].armour", is(hero.getArmour())))
                .andExpect(jsonPath("$.elements[0].shield", is(hero.getShield())));
    }

    @Test
    public void testGetHerosWithPageAndLimit() throws Exception {
        final Hero hero = new Hero(1, "name", "realName", 1, 2, 3);
        final PageableHeros heros = new PageableHeros(2, 10, ImmutableList.of(hero));
        given(overwatchService.getHeros(1, 1)).willReturn(heros);

        mvc.perform(get("/api/heros?page=1&limit=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPage", is(heros.getNextPage())))
                .andExpect(jsonPath("$.total", is(heros.getTotal())))
                .andExpect(jsonPath("$.elements", hasSize(1)))
                .andExpect(jsonPath("$.elements[0].id", is(hero.getId())))
                .andExpect(jsonPath("$.elements[0].name", is(hero.getName())))
                .andExpect(jsonPath("$.elements[0].realName", is(hero.getRealName())))
                .andExpect(jsonPath("$.elements[0].health", is(hero.getHealth())))
                .andExpect(jsonPath("$.elements[0].armour", is(hero.getArmour())))
                .andExpect(jsonPath("$.elements[0].shield", is(hero.getShield())));
    }

}