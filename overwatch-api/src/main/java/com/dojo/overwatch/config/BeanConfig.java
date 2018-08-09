package com.dojo.overwatch.config;

import com.dojo.overwatch.mapper.AbilityMapper;
import com.dojo.overwatch.mapper.HeroMapper;
import com.dojo.overwatch.persistence.AbilityRepository;
import com.dojo.overwatch.persistence.HeroRepository;
import com.dojo.overwatch.service.OverwatchService;
import com.dojo.overwatch.service.impl.OverwatchInMemoryDecoratorServiceImpl;
import com.dojo.overwatch.service.impl.UnofficialOverwatchApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private AbilityRepository abilityRepository;

    @Autowired
    private HeroMapper heroMapper;

    @Autowired
    private AbilityMapper abilityMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${overwatch-api.use-in-memory-db:true}")
    private Boolean useInMemoryDb;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OverwatchService overwatchService() {
        final OverwatchService unofficialOverwatchApiService = new UnofficialOverwatchApiServiceImpl(
                restTemplate,
                heroMapper,
                abilityMapper
        );

        if (useInMemoryDb) {
            return new OverwatchInMemoryDecoratorServiceImpl(
                    heroRepository,
                    abilityRepository,
                    heroMapper,
                    abilityMapper,
                    unofficialOverwatchApiService
            );
        } else {
            return unofficialOverwatchApiService;
        }
    }

}
