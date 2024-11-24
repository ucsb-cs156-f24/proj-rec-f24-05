package edu.ucsb.cs156.rec.repositories;

import edu.ucsb.cs156.rec.entities.RecommendationRequest;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * The RecommendationRequestRepository is a repository for RecommendationRequest entities.
 */

@Repository
public interface RecommendationRequestRepository extends CrudRepository<RecommendationRequest, Long> {
  /**
   * This method returns all RecommendationRequest entities with a given requesterId.
   * @param requesterId id of the person making the reqest 
   * @return all RecommendationRequest entities with a given requesterName
   */
  Optional<RecommendationRequest> findByRequesterId(Long requesterId);

  /**
   * This method returns all RecommendationRequest entities with a given professorId.
   * @param professorId id of the professor the request is made to 
   * @return all RecommendationRequest entities made to the professor 
   */
  Optional<RecommendationRequest> findByProfessorId(Long professorId);
}