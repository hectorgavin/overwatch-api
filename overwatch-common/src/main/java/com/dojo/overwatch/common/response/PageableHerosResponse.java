package com.dojo.overwatch.common.response;

import com.dojo.overwatch.common.Pageable;

import java.util.List;

public class PageableHerosResponse extends Pageable<Integer, HeroResponse> {
    public PageableHerosResponse(final Integer nextPage,
                                 final Integer total,
                                 final List<HeroResponse> elements) {
        super(nextPage, total, elements);
    }
}
