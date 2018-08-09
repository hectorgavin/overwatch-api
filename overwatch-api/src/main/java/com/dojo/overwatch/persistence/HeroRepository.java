package com.dojo.overwatch.persistence;

import com.dojo.overwatch.model.persistence.HeroEntity;
import org.springframework.data.repository.CrudRepository;

public interface HeroRepository extends CrudRepository<HeroEntity, Integer> {}
