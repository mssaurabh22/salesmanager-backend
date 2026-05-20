package org.crm.salesmanager.common.event;

public record LeadEngagementEvent(Long leadId, Long userId, String reason) {
}
