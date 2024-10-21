package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Country;

public record CountryDto(
        Integer id,
        String name,
        String code
) {

    public static CountryDto convert(Country from) {
        return new CountryDto(
                from.getId(),
                from.getName(),
                from.getCode()
        );
    }
}
