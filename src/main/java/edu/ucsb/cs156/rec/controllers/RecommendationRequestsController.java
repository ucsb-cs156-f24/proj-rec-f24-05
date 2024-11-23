package edu.ucsb.cs156.rec.controllers;

import edu.ucsb.cs156.rec.entities.RecommendationRequest;
import edu.ucsb.cs156.rec.entities.UCSBDate;
import edu.ucsb.cs156.rec.errors.EntityNotFoundException;
import edu.ucsb.cs156.rec.repositories.RecommendationRequestRepository;
import edu.ucsb.cs156.rec.repositories.UCSBDateRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

/**
 * This is a REST controller for RecommendationRequests
 */

@Tag(name = "RecommendationRequests")
@RequestMapping("/api/recommendationrequests")
@RestController
@Slf4j
public class RecommendationRequestsController extends ApiController {

    @Autowired
    RecommendationRequestRepository recommendationRequestRepository;

    /**
     * Create a new RecommendationRequest 
     * 
     * @param professorName        the name of the professor 
     * @param professorEmail       the professor's email 
     * @param requesterName        the name of the requester 
     * @param recommendationType   the type of request made 
     * @param details              the details of the request 
     * @param other                other information 
     * @return the saved RecommendationRequest
     */
    @Operation(summary= "Create a new recommendation request")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public RecommendationRequest postRecommendationRequest(
            @Parameter(name="professorName") @RequestParam String professorName,
            @Parameter(name="professorEmail") @RequestParam String professorEmail,
            @Parameter(name="requesterName") @RequestParam String requesterName,
            @Parameter(name="recommendationType") @RequestParam String recommendationType,
            @Parameter(name="details") @RequestParam String details,
            @Parameter(name="other") @RequestParam String other)
            throws JsonProcessingException {


        LocalDateTime currentDateTime = LocalDateTime.now();

        RecommendationRequest recommendationRequest = new RecommendationRequest();

        recommendationRequest.setProfessorName(professorName);
        recommendationRequest.setProfessorEmail(professorEmail);
        recommendationRequest.setRequesterName(requesterName);
        recommendationRequest.setRecommendationType(recommendationType);
        recommendationRequest.setDetails(details);
        recommendationRequest.setOther(other);
        recommendationRequest.setSubmissionDate(currentDateTime);
        recommendationRequest.setCompletionDate(null);
        recommendationRequest.setStatus("pending");


        RecommendationRequest savedRecommendationRequest = recommendationRequestRepository.save(recommendationRequest);

        return savedRecommendationRequest;
    }


}
