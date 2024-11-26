package edu.ucsb.cs156.rec.controllers;

import edu.ucsb.cs156.rec.repositories.UserRepository;
import edu.ucsb.cs156.rec.testconfig.TestConfig;
import edu.ucsb.cs156.rec.ControllerTestCase;
import edu.ucsb.cs156.rec.entities.RequestType;
import edu.ucsb.cs156.rec.entities.UCSBDate;
import edu.ucsb.cs156.rec.repositories.RequestTypeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.apache.catalina.connector.Request;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = RequestTypesController.class)
@Import(TestConfig.class)
public class RequestTypesControllerTests extends ControllerTestCase {

        @MockBean
        RequestTypeRepository requestTypeRepository;

        @MockBean
        UserRepository userRepository;

        // Authorization tests for /api/requesttypes/admin/all

        @Test
        public void logged_out_users_cannot_get_all() throws Exception {
                mockMvc.perform(get("/api/requesttypes/all"))
                                .andExpect(status().is(403)); // logged out users can't get all
        }
        
        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_can_get_all() throws Exception {
                mockMvc.perform(get("/api/requesttypes/all"))
                                .andExpect(status().is(200)); // logged
        }
        @Test
        public void logged_out_users_cannot_get_by_id() throws Exception {
                mockMvc.perform(get("/api/requesttypes?id=7"))
                                .andExpect(status().is(403)); // logged out users can't get by id
        }

        // // Tests with mocks for database actions

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_exists() throws Exception {

                // arrange

                RequestType requestType = RequestType.builder()
                                .requestType("CS Department BS/MS program")
                                .build();

                when(requestTypeRepository.findById(eq(7L))).thenReturn(Optional.of(requestType));

                // act
                MvcResult response = mockMvc.perform(get("/api/requesttypes?id=7"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(requestTypeRepository, times(1)).findById(eq(7L));
                String expectedJson = mapper.writeValueAsString(requestType);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_does_not_exist() throws Exception {

                // arrange

                when(requestTypeRepository.findById(eq(7L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/requesttypes?id=7"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                verify(requestTypeRepository, times(1)).findById(eq(7L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("RequestType with id 7 not found", json.get("message"));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_user_can_get_all_ucsbdates() throws Exception {

                // arrange

                RequestType requestType1 = RequestType.builder()
                                .requestType("CS Department BS/MS program")
                                .build();

                RequestType requestType2 = RequestType.builder()
                                .requestType("Scholarship or Fellowship")
                                .build();

                ArrayList<RequestType> expectedRequestTypes = new ArrayList<>();
                expectedRequestTypes.addAll(Arrays.asList(requestType1, requestType2));

                when(requestTypeRepository.findAll()).thenReturn(expectedRequestTypes);

                // act
                MvcResult response = mockMvc.perform(get("/api/requesttypes/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(requestTypeRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedRequestTypes);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }
        
        // Authorization tests for /api/phones/post
        // (Perhaps should also have these for put and delete)

        @Test
        public void logged_out_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/requesttypes/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_regular_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/requesttypes/post"))
                                .andExpect(status().is(403)); // only admins can post
        }
        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_user_can_get_all_requesttypes() throws Exception {

                // arrange

                RequestType requestType1 = RequestType.builder()
                                .requestType("CS Department BS/MS program")
                                .build();

                RequestType requestType2 = RequestType.builder()
                                .requestType("Scholarship or Fellowship")
                                .build();

                ArrayList<RequestType> expectedRequestTypes = new ArrayList<>();
                expectedRequestTypes.addAll(Arrays.asList(requestType1, requestType2));

                when(requestTypeRepository.findAll()).thenReturn(expectedRequestTypes);

                // act
                MvcResult response = mockMvc.perform(get("/api/requesttypes/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(requestTypeRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedRequestTypes);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }


        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_post_a_new_request_type() throws Exception {
                // arrange

                RequestType requestType1 = RequestType.builder()
                                .requestType("Advice")
                                .build();

                when(requestTypeRepository.save(eq(requestType1))).thenReturn(requestType1);

                // act
                MvcResult response = mockMvc.perform(
                                post("/api/requesttypes/post?requestType=Advice")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(requestTypeRepository, times(1)).save(requestType1);
                String expectedJson = mapper.writeValueAsString(requestType1);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void admin_cannot_post_duplicate_request_type() throws Exception {
            // Arrange
            String requestType = "Advice";
    
            RequestType existingRequestType = RequestType.builder()
                    .id(1L)
                    .requestType(requestType)
                    .build();
    
            when(requestTypeRepository.findByRequestType(requestType)).thenReturn(Optional.of(existingRequestType));
    
            // Act
            MvcResult response = mockMvc.perform(
                            post("/api/requesttypes/post?requestType=Advice")
                                    .with(csrf()))
                    .andExpect(status().isBadRequest()) 
                    .andReturn();
    
            // Assert
            Map<String, Object> json = responseToJson(response);
            assertEquals("DuplicateArgumentException", json.get("type"));
            assertEquals("The request type of Advice has already been added to the database", json.get("message"));
    
            verify(requestTypeRepository, times(1)).findByRequestType(requestType);
            verify(requestTypeRepository, times(0)).save(any(RequestType.class));
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_can_edit_an_existing_request_type() throws Exception {
                // arrange

                RequestType requestTypeOrig = RequestType.builder()
                                .requestType("Advice")
                                .build();

                RequestType requestTypeEdited = RequestType.builder()
                                .requestType("New Advice")
                                .build();

                String requestBody = mapper.writeValueAsString(requestTypeEdited);

                when(requestTypeRepository.findById(eq(67L))).thenReturn(Optional.of(requestTypeOrig));

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/requesttypes?id=67")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(requestTypeRepository, times(1)).findById(67L);
                verify(requestTypeRepository, times(1)).save(requestTypeEdited); // should be saved with correct user
                String responseString = response.getResponse().getContentAsString();
                assertEquals(requestBody, responseString);
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_cannot_edit_request_type_that_does_not_exist() throws Exception {
                // arrange


                RequestType requestTypeEdited = RequestType.builder()
                                .requestType("New Advice")
                                .build();

                String requestBody = mapper.writeValueAsString(requestTypeEdited);

                when(requestTypeRepository.findById(eq(67L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/requesttypes?id=67")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(requestTypeRepository, times(1)).findById(67L);
                Map<String, Object> json = responseToJson(response);
                assertEquals("RequestType with id 67 not found", json.get("message"));

        }


        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void admin_cannot_update_to_a_duplicate_request_type() throws Exception {
            // Arrange
            String requestType = "Advice";
    
            RequestType requestTypeEdited = RequestType.builder()
                                .requestType(requestType)
                                .build();

        String requestBody = mapper.writeValueAsString(requestTypeEdited);

    
            when(requestTypeRepository.findByRequestType(requestType)).thenReturn(Optional.of(requestTypeEdited));

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/requesttypes?id=67")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isBadRequest()).andReturn();
    
    
            // Assert
            Map<String, Object> json = responseToJson(response);
            assertEquals("DuplicateArgumentException", json.get("type"));
            assertEquals("The request type of Advice has already been added to the database", json.get("message"));
    
        }
        
}
