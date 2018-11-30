package com.zhilong.springcloud.controller;

import com.zhilong.springcloud.entity.Event;
import com.zhilong.springcloud.entity.Location;
import com.zhilong.springcloud.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/v1/event")
public class EventController {

    @Autowired
    EventService eventService;

    @PostMapping()
    public ResponseEntity event(){
        Location cluj = new Location();
        cluj.setCountry("Romania");
        cluj.setCity("Cluj-Napoca");

        Location newYork = new Location();
        newYork.setCountry("US");
        newYork.setCity("New-York");

        Location london = new Location();
        london.setCountry("UK");
        london.setCity("London");

        Event event = new Event();
        event.setLocation(cluj);
        event.setAlternativeLocations(
                Arrays.asList(newYork, london)
        );
        eventService.save(event);
        return ResponseEntity.ok(event);
    }


}
