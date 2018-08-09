package com.dojo.overwatch.utils;

import lombok.NonNull;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

public class HttpUtils {

    public static Optional<String> getQueryParam(@NonNull final String urlAsString, @NonNull final String paramName) {
        return UriComponentsBuilder.fromHttpUrl(urlAsString).build().getQueryParams().entrySet().stream()
            .filter(entry -> entry.getKey().equals(paramName))
            .map(Map.Entry::getValue)
            .filter(values -> values.size() > 0)
            .map(values -> values.get(0))
            .findFirst();
    }

}
