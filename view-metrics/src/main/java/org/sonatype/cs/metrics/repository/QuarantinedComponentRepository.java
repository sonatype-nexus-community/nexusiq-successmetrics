package org.sonatype.cs.metrics.repository;

import org.sonatype.cs.metrics.model.QuarantinedComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuarantinedComponentRepository extends JpaRepository<QuarantinedComponent, Long> {
    QuarantinedComponent findTop1ByOrderByQuarantineDateAsc();

    QuarantinedComponent findTop1ByOrderByQuarantineDateDesc();

    QuarantinedComponent findTop1ByOrderByDateClearedAsc();

    QuarantinedComponent findTop1ByOrderByDateClearedDesc();

    @Query("SELECT DISTINCT displayName FROM QuarantinedComponent ORDER BY displayName ASC")
    List<String> findDistinctDisplayName();

    @Query("SELECT DISTINCT format FROM QuarantinedComponent ORDER BY format ASC")
    List<String> findDistinctFormat();

    Long countByFormatAndThreatLevel(String format, Integer threatLevel);

    @Query(
            "SELECT COUNT(*) From QuarantinedComponent WHERE threatLevel = ?1 AND quarantineDate >="
                    + " ?2 AND quarantineDate<= ?3")
    Long countByThreatLevelByQuarantineDateBetween(
            Integer threatLevel, LocalDateTime start, LocalDateTime end);
}
