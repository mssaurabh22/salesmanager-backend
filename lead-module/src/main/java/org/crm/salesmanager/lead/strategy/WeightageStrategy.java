package org.crm.salesmanager.lead.strategy;

import org.crm.salesmanager.lead.enums.LeadTemperature;

public interface WeightageStrategy {

    LeadTemperature resolveTemperature(Integer weightage);

    Integer applyActivityImpact(Integer currentWeightage);
}
