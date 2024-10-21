package com.lectorie.lectorie.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lectorie.lectorie.dto.CountryDto;
import com.lectorie.lectorie.exception.custom.CountryAlreadyExistException;
import com.lectorie.lectorie.model.Country;
import com.lectorie.lectorie.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final CountryRepository repository;


    public CountryService(CountryRepository repository) {
        this.repository = repository;
    }


    public List<CountryDto> getAllCountries() throws JsonProcessingException {
        return repository.findAll()
                .stream()
                .map(CountryDto::convert)
                .collect(Collectors.toList());
    }

    public CountryDto addCountry(String countryName, String countryCode) {
        if(repository.existsByName(countryName))
            throw new CountryAlreadyExistException("Country Exist",3990);

        Country country = new Country(countryName,countryCode);
        return CountryDto.convert(repository.save(country));
    }


}
