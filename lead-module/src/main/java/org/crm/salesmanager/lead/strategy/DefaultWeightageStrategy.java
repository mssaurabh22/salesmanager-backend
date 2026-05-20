package org.crm.salesmanager.lead.strategy;

import org.crm.salesmanager.lead.enums.LeadTemperature;
import org.springframework.stereotype.Component;

@Component
public class DefaultWeightageStrategy implements WeightageStrategy {

    private static final int ACTIVITY_IMPACT = 10;
    private static final int MAX_WEIGHTAGE = 100;

    @Override
    public LeadTemperature resolveTemperature(Integer weightage) {
        int safeWeightage = weightage == null ? 0 : weightage;

        if (safeWeightage <= 30) {
            return LeadTemperature.COLD;
        }

        if (safeWeightage <= 70) {
            return LeadTemperature.WARM;
        }

        return LeadTemperature.HOT;
    }

    @Override
    public Integer applyActivityImpact(Integer currentWeightage) {
        int safeWeightage = currentWeightage == null ? 0 : currentWeightage;
        return Math.min(MAX_WEIGHTAGE, safeWeightage + ACTIVITY_IMPACT);
    }
}
