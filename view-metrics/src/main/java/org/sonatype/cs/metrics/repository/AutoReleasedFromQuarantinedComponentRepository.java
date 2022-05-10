package org.sonatype.cs.metrics.repository;

import org.sonatype.cs.metrics.model.AutoReleasedFromQuarantineComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoReleasedFromQuarantinedComponentRepository
        extends JpaRepository<AutoReleasedFromQuarantineComponent, Long> {
    @Query("SELECT DISTINCT displayName FROM AutoReleasedFromQuarantineComponent")
    List<String> findDistinctDisplayName();
}
