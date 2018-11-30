package com.zhilong.springcloud.service.impl;

import com.zhilong.springcloud.entity.Event;
import com.zhilong.springcloud.repository.EventRepository;
import com.zhilong.springcloud.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Override
    public void save(Event event) {

        eventRepository.save(event);
    }
}
