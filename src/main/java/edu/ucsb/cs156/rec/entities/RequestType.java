package edu.ucsb.cs156.rec.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a JPA entity that represents a RequestType (i.e., CS Department BS/MS program, Scholarship or Fellowship, etc.)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "requesttypes")
public class RequestType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String requestType;
}