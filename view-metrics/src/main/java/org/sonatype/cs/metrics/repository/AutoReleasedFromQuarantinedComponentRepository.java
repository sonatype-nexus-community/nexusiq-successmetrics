package org.sonatype.cs.metrics.repository;

import org.sonatype.cs.metrics.model.AutoReleasedFromQuarantineComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoReleasedFromQuarantinedComponentRepository
        extends JpaRepository<AutoReleasedFromQuarantineComponent, Long> {}
