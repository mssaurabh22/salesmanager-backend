package org.crm.salesmanager.lead.validator;

import org.crm.salesmanager.lead.enums.LeadStage;
import org.springframework.stereotype.Component;

@Component
public class LeadStageValidator {

    private static final boolean ALLOW_STAGE_JUMPS = false;

    public void validateTransition(LeadStage currentStage, LeadStage nextStage) {
        if (nextStage == null) {
            throw new RuntimeException("Lead stage is required");
        }

        if (currentStage == null || currentStage == nextStage) {
            return;
        }

        if (isTerminal(currentStage)) {
            throw new RuntimeException("Cannot change stage after lead reached " + currentStage);
        }

        if (nextStage == LeadStage.LOST) {
            return;
        }

        if (!ALLOW_STAGE_JUMPS && nextStage.ordinal() > currentStage.ordinal() + 1) {
            throw new RuntimeException("Lead stage jumps are not allowed from " + currentStage + " to " + nextStage);
        }

        if (nextStage.ordinal() < currentStage.ordinal()) {
            throw new RuntimeException("Invalid lead stage transition from " + currentStage + " to " + nextStage);
        }
    }

    private boolean isTerminal(LeadStage stage) {
        return stage == LeadStage.ORDER || stage == LeadStage.LOST;
    }
}
