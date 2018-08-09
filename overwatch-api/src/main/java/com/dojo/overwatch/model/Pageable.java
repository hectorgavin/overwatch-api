package com.dojo.overwatch.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Pageable<O, T> {
    private O nextPage;
    private Integer total;
    private List<T> elements;
}
