package com.lectorie.lectorie.controller;

import com.lectorie.lectorie.service.TimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("api/v1/time")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/timezones")
    public ResponseEntity<List<String>> getAllTimezones(){
        return ResponseEntity.ok(timeService.getAllTimezones());
    }
}
