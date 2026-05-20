package org.crm.salesmanager.followup.listener;

import lombok.RequiredArgsConstructor;
import org.crm.salesmanager.common.event.LeadEngagementEvent;
import org.crm.salesmanager.followup.service.FollowUpService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeadEngagementListener {

    private final FollowUpService followUpService;

    @EventListener
    public void onLeadEngagement(LeadEngagementEvent event) {
        followUpService.suggestNextFollowUp(event.leadId(), event.userId(), event.reason());
    }
}
