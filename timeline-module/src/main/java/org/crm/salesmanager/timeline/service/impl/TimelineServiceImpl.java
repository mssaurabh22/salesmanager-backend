package org.crm.salesmanager.timeline.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.common.exception.BusinessException;
import org.crm.salesmanager.timeline.domain.TimelineEvent;
import org.crm.salesmanager.timeline.dto.TimelineEventDTO;
import org.crm.salesmanager.timeline.enums.TimelineAction;
import org.crm.salesmanager.timeline.repository.TimelineEventRepository;
import org.crm.salesmanager.timeline.service.TimelineService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimelineServiceImpl implements TimelineService {

    private final TimelineEventRepository timelineEventRepository;

    @Override
    public void recordEvent(Long leadId, Long userId, TimelineAction action, String description) {
        if (leadId == null || action == null) {
            throw new BusinessException("Timeline event requires lead id and action");
        }

        timelineEventRepository.save(TimelineEvent.builder()
                .leadId(leadId)
                .userId(userId)
                .action(action)
                .description(description)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @Override
    public List<TimelineEventDTO> getLeadTimeline(Long leadId) {
        if (leadId == null) {
            throw new BusinessException("Lead id is required");
        }

        return timelineEventRepository.findByLeadIdOrderByTimestampDesc(leadId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private TimelineEventDTO toDTO(TimelineEvent event) {
        return TimelineEventDTO.builder()
                .id(event.getId())
                .leadId(event.getLeadId())
                .userId(event.getUserId())
                .action(event.getAction())
                .description(event.getDescription())
                .timestamp(event.getTimestamp())
                .build();
    }
}
