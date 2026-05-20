package org.crm.salesmanager.followup.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.followup.service.FollowUpService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowUpScheduler {

    private final FollowUpService followUpService;

    @Scheduled(cron = "0 */5 * * * *")
    public void markMissedFollowUps() {
        log.debug("Running follow-up scheduler for overdue follow-up detection");
        followUpService.markOverdueFollowUpsAsMissed();
    }
}
