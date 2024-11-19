package edu.ucsb.cs156.rec.controllers;

import edu.ucsb.cs156.rec.entities.RequestType;
import edu.ucsb.cs156.rec.errors.EntityNotFoundException;
import edu.ucsb.cs156.rec.repositories.RequestTypeRepository;

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
 * This is a REST controller for RequestType
 */

@Tag(name = "RequestType")
@RequestMapping("/api/requesttype")
@RestController
@Slf4j
public class RequestTypeController extends ApiController {

    @Autowired
    RequestTypeRepository requestTypeRepository;

    /**
     * List all Request Types
     * 
     * @return an iterable of RequestType
     */
    @Operation(summary= "List all request types")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<RequestType> allRequestTypes() {
        Iterable<RequestType> types = requestTypeRepository.findAll();
        return types;
    }
}
