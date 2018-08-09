package com.dojo.overwatch.service;

import com.dojo.overwatch.exception.AbilityNotFoundException;
import com.dojo.overwatch.exception.HeroNotFoundException;
import com.dojo.overwatch.model.Ability;
import com.dojo.overwatch.model.Hero;
import com.dojo.overwatch.model.PageableAbilities;
import com.dojo.overwatch.model.PageableHeros;
import com.dojo.overwatch.persistence.AbilityRepository;
import com.dojo.overwatch.persistence.HeroRepository;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties="overwatch-api.use-in-memory-db=true")
public class OverwatchServiceTests {

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private AbilityRepository abilityRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OverwatchService overwatchService;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @After
    public void after() {
        mockServer.reset();
        heroRepository.deleteAll();
        abilityRepository.deleteAll();
    }

    @Test
    public void testGetHeroById() {
        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/hero/1"))
                .andExpect(method(GET))
                .andRespond(withSuccess(loadResource("hero-1.json"), MediaType.APPLICATION_JSON));

        final Hero hero = overwatchService.getHeroById(1);
        mockServer.verify();

        assertThat(hero.getId(), is(1));
        assertThat(hero.getName(), is("Ana"));
        assertThat(hero.getRealName(), is("Ana Amari"));
        assertThat(hero.getHealth(), is(200));
        assertThat(hero.getArmour(), is(0));
        assertThat(hero.getShield(), is(0));
    }

    @Test(expected = HeroNotFoundException.class)
    public void testGetHeroByIdNotFound() {
        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/hero/1"))
                .andExpect(method(GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        overwatchService.getHeroById(1);
    }

    @Test
    public void testGetAbilityById() {
        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/ability/1"))
                .andExpect(method(GET))
                .andRespond(withSuccess(loadResource("ability-1.json"), MediaType.APPLICATION_JSON));

        final Ability ability = overwatchService.getAbilityById(1);
        mockServer.verify();

        assertThat(ability.getId(), is(1));
        assertThat(ability.getName(), is("Biotic Rifle"));
        assertThat(ability.getDescription(), is("Ana’s rifle shoots darts that can restore health to her allies or deal ongoing damage to her enemies. She can use the rifle’s scope to zoom in on targets and make highly accurate shots."));
        assertThat(ability.getIsUltimate(), is(false));
    }

    @Test(expected = AbilityNotFoundException.class)
    public void testGetAbilityByIdNotFound() {
        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/ability/1"))
                .andExpect(method(GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        overwatchService.getAbilityById(1);
    }

    @Test
    public void testGetHeroAbilities() {
        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/hero/1"))
                .andExpect(method(GET))
                .andRespond(withSuccess(loadResource("hero-1.json"), MediaType.APPLICATION_JSON));

        final List<Ability> abilities = overwatchService.getHeroAbilitiesByHeroId(1);
        mockServer.verify();

        assertThat(abilities.size(), is(4));

        final Ability firstAbility = abilities.get(0);

        assertThat(firstAbility.getId(), is(1));
        assertThat(firstAbility.getName(), is("Biotic Rifle"));
        assertThat(firstAbility.getDescription(), is("Ana’s rifle shoots darts that can restore health to her allies or deal ongoing damage to her enemies. She can use the rifle’s scope to zoom in on targets and make highly accurate shots."));
        assertThat(firstAbility.getIsUltimate(), is(false));
    }

    @Test
    public void testGetHeros() {
        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/hero?limit=1"))
                .andExpect(method(GET))
                .andRespond(withSuccess(loadResource("heros.json"), MediaType.APPLICATION_JSON));

        final PageableHeros pageableHeros = overwatchService.getHeros(null, 1);
        mockServer.verify();

        assertThat(pageableHeros.getTotal(), is(24));
        assertThat(pageableHeros.getNextPage(), is(2));
        assertThat(pageableHeros.getElements().size(), is(1));

        final Hero firstHero = pageableHeros.getElements().get(0);

        assertThat(firstHero.getId(), is(1));
        assertThat(firstHero.getName(), is("Ana"));
        assertThat(firstHero.getRealName(), is("Ana Amari"));
        assertThat(firstHero.getHealth(), is(200));
        assertThat(firstHero.getArmour(), is(0));
        assertThat(firstHero.getShield(), is(0));
    }

    @Test
    public void testGetAbilities() {
        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/ability?limit=1"))
                .andExpect(method(GET))
                .andRespond(withSuccess(loadResource("abilities.json"), MediaType.APPLICATION_JSON));

        final PageableAbilities pageableAbilities = overwatchService.getAbilities(null, 1);
        mockServer.verify();

        assertThat(pageableAbilities.getTotal(), is(114));
        assertThat(pageableAbilities.getNextPage(), is(2));
        assertThat(pageableAbilities.getElements().size(), is(1));

        final Ability firstAbility = pageableAbilities.getElements().get(0);

        assertThat(firstAbility.getId(), is(1));
        assertThat(firstAbility.getName(), is("Biotic Rifle"));
        assertThat(firstAbility.getDescription(), is("Ana’s rifle shoots darts that can restore health to her allies or deal ongoing damage to her enemies. She can use the rifle’s scope to zoom in on targets and make highly accurate shots."));
        assertThat(firstAbility.getIsUltimate(), is(false));
    }

    @Test
    public void testHeroInMemory() {
        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/hero/1"))
                .andExpect(method(GET))
                .andRespond(withSuccess(loadResource("hero-1.json"), MediaType.APPLICATION_JSON));

        overwatchService.getHeroById(1);
        mockServer.verify();
        mockServer.reset();

        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/hero/1"))
                .andExpect(method(GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        final Hero hero = overwatchService.getHeroById(1);

        assertThat(hero.getId(), is(1));
        assertThat(hero.getName(), is("Ana"));
        assertThat(hero.getRealName(), is("Ana Amari"));
        assertThat(hero.getHealth(), is(200));
        assertThat(hero.getArmour(), is(0));
        assertThat(hero.getShield(), is(0));
    }

    @Test
    public void testAbilityInMemory() {
        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/ability/1"))
                .andExpect(method(GET))
                .andRespond(withSuccess(loadResource("ability-1.json"), MediaType.APPLICATION_JSON));

        overwatchService.getAbilityById(1);
        mockServer.verify();
        mockServer.reset();

        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/ability/1"))
                .andExpect(method(GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        final Ability ability = overwatchService.getAbilityById(1);

        assertThat(ability.getId(), is(1));
        assertThat(ability.getName(), is("Biotic Rifle"));
        assertThat(ability.getDescription(), is("Ana’s rifle shoots darts that can restore health to her allies or deal ongoing damage to her enemies. She can use the rifle’s scope to zoom in on targets and make highly accurate shots."));
        assertThat(ability.getIsUltimate(), is(false));
    }

    @Test
    public void testHeroAbilitiesInMemory() {
        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/hero/1"))
                .andExpect(method(GET))
                .andRespond(withSuccess(loadResource("hero-1.json"), MediaType.APPLICATION_JSON));

        overwatchService.getHeroAbilitiesByHeroId(1);
        mockServer.verify();
        mockServer.reset();

        mockServer.expect(requestTo("https://overwatch-api.net/api/v1/ability/1"))
                .andExpect(method(GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        final Ability ability = overwatchService.getAbilityById(1);

        assertThat(ability.getId(), is(1));
        assertThat(ability.getName(), is("Biotic Rifle"));
        assertThat(ability.getDescription(), is("Ana’s rifle shoots darts that can restore health to her allies or deal ongoing damage to her enemies. She can use the rifle’s scope to zoom in on targets and make highly accurate shots."));
        assertThat(ability.getIsUltimate(), is(false));
    }

    private String loadResource(final String fileName) {
        try {
            return IOUtils.toString(getClass().getResourceAsStream(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}