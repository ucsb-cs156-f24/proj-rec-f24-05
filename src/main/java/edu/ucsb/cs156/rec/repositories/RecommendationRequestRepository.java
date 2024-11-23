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
   * This method returns all RecommendationRequest entities with a given requesterName.
   * @param requesterName name of the person making the reqest 
   * @return all RecommendationRequest entities with a given requesterName
   */
  Optional<RecommendationRequest> findByRequesterName(String requesterName);

  /**
   * This method returns all RecommendationRequest entities with a given professorName.
   * @param professorName name of the professor the request is made to 
   * @return all RecommendationRequest entities made to the professor 
   */
  Optional<RecommendationRequest> findByProfessorName(String professorName);
}