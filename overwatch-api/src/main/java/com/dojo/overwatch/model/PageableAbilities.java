package com.dojo.overwatch.model;

import com.dojo.overwatch.common.Pageable;

import java.util.List;

public class PageableAbilities extends Pageable<Integer, Ability> {
    public PageableAbilities(final Integer nextPage,
                             final Integer total,
                             final List<Ability> elements) {
        super(nextPage, total, elements);
    }
}
