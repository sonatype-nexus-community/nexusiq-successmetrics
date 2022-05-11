package org.sonatype.cs.metrics.repository;

import org.sonatype.cs.metrics.model.QuarantinedComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuarantinedComponentRepository extends JpaRepository<QuarantinedComponent, Long> {}
