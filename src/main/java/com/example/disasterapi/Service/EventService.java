package com.example.disasterapi.Service;

import com.example.disasterapi.Model.Event;
import com.example.disasterapi.Repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(String id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Event create(Event event) {
        // Generate a unique ID if one is not provided
        if (event.getId() == null || event.getId().isEmpty()) {
            event.setId(UUID.randomUUID().toString());
        }
        return eventRepository.save(event);
    }
}
