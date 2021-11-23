package org.sonatype.cs.metrics.service;

import org.sonatype.cs.metrics.model.Metric;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepositoryService extends CrudRepository<Metric, Long> {

}
