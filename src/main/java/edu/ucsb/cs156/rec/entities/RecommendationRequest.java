package edu.ucsb.cs156.rec.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * This is a JPA entity that represents a RecommendationRequest, i.e. an entry
 * that comes from the RecommendationRequest API for requests from students.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "recommendationrequest")
public class RecommendationRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String status; 

  private String professorName;
  private String professorEmail;
  private String requesterName;
  private String recommendationType;
  private String details;
  private String other; 
  private LocalDateTime submissionDate;
  private LocalDateTime completionDate; 
}