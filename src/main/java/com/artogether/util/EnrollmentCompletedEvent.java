package com.artogether.util;

import org.springframework.context.ApplicationEvent;

public class EnrollmentCompletedEvent extends ApplicationEvent {

    private final Integer eventId;

    public EnrollmentCompletedEvent(Object source, Integer eventId) {
        super(source);
        this.eventId = eventId;
    }

    public Integer getEventId() {
        return eventId;
    }
}
