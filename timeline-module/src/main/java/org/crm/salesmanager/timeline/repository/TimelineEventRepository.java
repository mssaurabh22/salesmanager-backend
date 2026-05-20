package org.crm.salesmanager.timeline.repository;

import org.crm.salesmanager.timeline.domain.TimelineEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimelineEventRepository extends JpaRepository<TimelineEvent, Long> {

    List<TimelineEvent> findByLeadIdOrderByTimestampDesc(Long leadId);
}
