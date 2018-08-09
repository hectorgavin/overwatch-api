package com.dojo.overwatch.model.unofficialApi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Pageable<T> {
    private String next;
    private Integer total;
    private List<T> data;
}
