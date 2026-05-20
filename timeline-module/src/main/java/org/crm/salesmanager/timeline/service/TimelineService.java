package org.crm.salesmanager.timeline.service;

import org.crm.salesmanager.timeline.dto.TimelineEventDTO;
import org.crm.salesmanager.timeline.enums.TimelineAction;

import java.util.List;

public interface TimelineService {

    void recordEvent(Long leadId, Long userId, TimelineAction action, String description);

    List<TimelineEventDTO> getLeadTimeline(Long leadId);
}
