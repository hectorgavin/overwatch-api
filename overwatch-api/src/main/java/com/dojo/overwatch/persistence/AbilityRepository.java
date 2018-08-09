package com.dojo.overwatch.persistence;

import com.dojo.overwatch.model.persistence.AbilityEntity;
import org.springframework.data.repository.CrudRepository;

public interface AbilityRepository extends CrudRepository<AbilityEntity, Integer> {}
