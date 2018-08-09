package com.dojo.overwatch.common.response;

import com.dojo.overwatch.common.Pageable;

import java.util.List;

public class PageableAbilitiesResponse extends Pageable<Integer, AbilityResponse> {
    public PageableAbilitiesResponse(final Integer nextPage,
                                     final Integer total,
                                     final List<AbilityResponse> elements) {
        super(nextPage, total, elements);
    }
}
