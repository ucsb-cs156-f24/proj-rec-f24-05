package edu.ucsb.cs156.rec.controllers;

import edu.ucsb.cs156.rec.repositories.UserRepository;
import edu.ucsb.cs156.rec.testconfig.TestConfig;
import edu.ucsb.cs156.rec.ControllerTestCase;
import edu.ucsb.cs156.rec.entities.RecommendationRequest;
import edu.ucsb.cs156.rec.entities.UCSBDate;
import edu.ucsb.cs156.rec.repositories.RecommendationRequestRepository;
import edu.ucsb.cs156.rec.repositories.UCSBDateRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDateTime;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = RecommendationRequestsController.class)
@Import(TestConfig.class)
public class RecommendationRequestsControllerTests extends ControllerTestCase {

        @MockBean
        RecommendationRequestRepository recommendationRequestRepository;
    
        @MockBean
        UserRepository userRepository;



        // Authorization tests for /api/recommendationrequests/post
        // (Perhaps should also have these for put and delete)

        @Test
        public void logged_out_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/recommendationrequests/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_regular_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/recommendationrequests/post"))
                                .andExpect(status().is(403)); // only admins can post
        }

        // // Tests with mocks for database actions


        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_post_a_new_recommendation_request() throws Exception {
                // arrange

                LocalDateTime currentTime = LocalDateTime.now();
                

                RecommendationRequest recommendationRequest1 = RecommendationRequest.builder()
                                        .status("pending")
                                        .professorName("test_professor")
                                        .professorEmail("test_email@gmail.com")
                                        .requesterName("test_name")
                                        .recommendationType("test_rec_type")
                                        .details("test_details")
                                        .other("test_other")
                                        .submissionDate(currentTime)
                                        .completionDate(null)
                                        .build();
                                    

                when(recommendationRequestRepository.save(eq(recommendationRequest1))).thenReturn(recommendationRequest1);

                UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/recommendationrequests/post")
                    .queryParam("status", "pending")
                    .queryParam("professorName", "test_professor")
                    .queryParam("professorEmail", "test_email@gmail.com")
                    .queryParam("requesterName", "test_name")
                    .queryParam("recommendationType", "test_rec_type")
                    .queryParam("details", "test_details")
                    .queryParam("other", "test_other")
                    .queryParam("submissionDate", currentTime.toString())
                    .queryParam("completionDate", "null");
                
                // act
                MvcResult response = mockMvc.perform(
                                post(builder.toUriString())
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                System.out.println("HERE IS THE RESPONSE " + response);

                // assert
                String expectedJson = mapper.writeValueAsString(recommendationRequest1);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

}
