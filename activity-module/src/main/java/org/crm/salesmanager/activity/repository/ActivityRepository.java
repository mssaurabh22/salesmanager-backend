package org.crm.salesmanager.activity.repository;

import org.crm.salesmanager.activity.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByLeadId(Long leadId);

    List<Activity> findByEmployeeId(Long employeeId);

    List<Activity> findByLeadIdOrderByCreatedAtDesc(Long leadId);

    long countByEmployeeIdAndCreatedAtBetween(Long employeeId, LocalDateTime start, LocalDateTime end);

    long countByEmployeeId(Long employeeId);

    @Query("""
            select a.employeeId as employeeId, count(a) as activityCount
            from Activity a
            where a.createdAt between :start and :end
            group by a.employeeId
            order by count(a) desc
            """)
    List<EmployeeActivityCountView> findEmployeeActivityCountsBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
            select a.employeeId as employeeId, count(a) as activityCount
            from Activity a
            group by a.employeeId
            order by count(a) desc
            """)
    List<EmployeeActivityCountView> findEmployeeActivityCounts();

    interface EmployeeActivityCountView {
        Long getEmployeeId();

        long getActivityCount();
    }
}
