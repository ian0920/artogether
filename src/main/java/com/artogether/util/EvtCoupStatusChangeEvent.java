package com.artogether.util;

import org.springframework.context.ApplicationEvent;

public class EvtCoupStatusChangeEvent extends ApplicationEvent {

    private final Integer evtCoupId;

    public EvtCoupStatusChangeEvent(Object source, Integer evtCoupId) {
        super(source);
        this.evtCoupId = evtCoupId;
    }

    public Integer getEvtCoupId() {
        return evtCoupId;
    }
}
