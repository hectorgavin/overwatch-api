package com.dojo.overwatch.model;

import com.dojo.overwatch.common.Pageable;

import java.util.List;

public class PageableHeros extends Pageable<Integer, Hero> {
    public PageableHeros(final Integer nextPage,
                         final Integer total,
                         final List<Hero> elements) {
        super(nextPage, total, elements);
    }
}
