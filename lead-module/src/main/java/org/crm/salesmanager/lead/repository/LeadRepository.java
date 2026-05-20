package org.crm.salesmanager.lead.repository;

import org.crm.salesmanager.lead.domain.Lead;
import org.crm.salesmanager.lead.enums.LeadStage;
import org.crm.salesmanager.lead.enums.LeadTemperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeadRepository extends JpaRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {

    boolean existsByContactNumber(String contactNumber);

    boolean existsByEmail(String email);

    long countByAssignedTo(Long assignedTo);

    long countByTemperature(LeadTemperature temperature);

    long countByStage(LeadStage stage);

    @Query("select coalesce(sum(l.expectedValue), 0) from Lead l")
    Double sumExpectedValue();

    @Query("""
            select l.assignedTo as employeeId,
                   count(l) as leadsHandled,
                   sum(case when l.stage = :closedStage then 1 else 0 end) as closedLeads
            from Lead l
            where l.assignedTo is not null
            group by l.assignedTo
            """)
    List<EmployeeLeadPerformanceView> findEmployeeLeadPerformance(@Param("closedStage") LeadStage closedStage);

    interface EmployeeLeadPerformanceView {
        Long getEmployeeId();

        long getLeadsHandled();

        long getClosedLeads();
    }
}
