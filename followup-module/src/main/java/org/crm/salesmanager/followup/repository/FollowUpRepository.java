package org.crm.salesmanager.followup.repository;

import org.crm.salesmanager.followup.domain.FollowUp;
import org.crm.salesmanager.followup.enums.FollowUpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {

    List<FollowUp> findByAssignedToAndStatus(Long assignedTo, FollowUpStatus status);

    long countByStatus(FollowUpStatus status);

    long countByAssignedToAndStatus(Long assignedTo, FollowUpStatus status);

    List<FollowUp> findByFollowUpTimeBeforeAndStatus(LocalDateTime followUpTime, FollowUpStatus status);

    List<FollowUp> findByAssignedToAndFollowUpTimeBetweenAndStatusOrderByFollowUpTimeAsc(
            Long assignedTo,
            LocalDateTime start,
            LocalDateTime end,
            FollowUpStatus status
    );

    long countByAssignedToAndFollowUpTimeBetweenAndStatus(
            Long assignedTo,
            LocalDateTime start,
            LocalDateTime end,
            FollowUpStatus status
    );

    List<FollowUp> findByAssignedToAndStatusOrderByFollowUpTimeAsc(Long assignedTo, FollowUpStatus status);

    Optional<FollowUp> findByIdAndAssignedTo(Long id, Long assignedTo);

    boolean existsByLeadIdAndStatusAndFollowUpTimeAfter(Long leadId, FollowUpStatus status, LocalDateTime followUpTime);
}
