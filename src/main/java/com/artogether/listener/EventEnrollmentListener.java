package com.artogether.listener;

import com.artogether.event.event.Event;
import com.artogether.event.event.EventRepo;
import com.artogether.util.EnrollmentCompletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EventEnrollmentListener {


    private final EventRepo eventRepo;

    public EventEnrollmentListener(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }


    @EventListener
    @Transactional
    public void handleEnrollmentCompleted(EnrollmentCompletedEvent event) {

        Event eventEntity = eventRepo.findById(event.getEventId())
                .orElseThrow(() -> new RuntimeException("活動不存在"));

        // 檢查活動報名人數是否達到上限人數
        if (eventEntity.getEnrolled() >= eventEntity.getMaximum()) {
            eventEntity.setStatus((byte) 5); // 設定活動狀態為結束報名
            eventRepo.save(eventEntity);
        }
    }


}
