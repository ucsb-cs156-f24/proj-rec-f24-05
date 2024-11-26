package edu.ucsb.cs156.rec.repositories;

import edu.ucsb.cs156.rec.entities.RequestType;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The RequestTypeRepository is a repository for RequestType entities.
 */

@Repository
public interface RequestTypeRepository extends CrudRepository<RequestType, Long> {
  /**
   * This method returns all RequestType entities with a given requestType.
   * @param requestType type of request as a string (ex: CS Department BS/MS program, Scholarship or Fellowship, etc.)
   * @return all RequestType entities with a given requestType
   */
  Iterable<RequestType> findAllByRequestType(String requestType);
  Optional<RequestType> findByRequestType(String requestType);
}