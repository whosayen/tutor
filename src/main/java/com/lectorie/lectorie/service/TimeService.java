package com.lectorie.lectorie.service;

import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class TimeService {

    public List<String> getAllTimezones() {
        Predicate<String> exclusionCriteria = zoneId ->
                zoneId.startsWith("System") || zoneId.startsWith("Etc/") || zoneId.startsWith("GMT") || zoneId.contains("SystemV");

        return ZoneId.getAvailableZoneIds().stream()
                .filter(exclusionCriteria.negate()) // Use negate() to exclude zones matching the criteria
                .sorted()
                .collect(Collectors.toList());
    }
}
