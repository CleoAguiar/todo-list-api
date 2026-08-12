package com.cleoaguiar.todolistapi.controller;

import com.cleoaguiar.todolistapi.dto.AuthRequest;
import com.cleoaguiar.todolistapi.dto.TodoRequest;
import com.cleoaguiar.todolistapi.dto.UserRegisterRequest;
import com.cleoaguiar.todolistapi.enums.TodoStatus;
import com.cleoaguiar.todolistapi.repository.TodoRepository;
import com.cleoaguiar.todolistapi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TodoControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    private final ObjectMapper objectMapper =  new ObjectMapper();
    private String token;

    @BeforeEach
    void setup() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest(
                "cleo",
                "cleo@email.com",
                "123456");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        AuthRequest loginRequest = new AuthRequest(
                "cleo@email.com",
                "123456");

        String responseJson = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        this.token = objectMapper.readTree(responseJson).get("token").asText();
    }

    @Test
    void shouldCreateTodoSuccessfully() throws Exception {
        TodoRequest request = new TodoRequest(
                "My title",
                "My description",
                TodoStatus.TODO
        );

        mockMvc.perform(post("/todos")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("My title"))
                .andExpect(jsonPath("$.description").value("My description"));
    }

    @Test
    void shouldListTodosSuccessfully() throws Exception {
        TodoRequest first_request = new TodoRequest(
                "My title One",
                "My description One",
                TodoStatus.TODO
        );

        TodoRequest second_request = new TodoRequest(
                "My title Two",
                "My description Two",
                TodoStatus.TODO
        );

        mockMvc.perform(post("/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(first_request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("My title One"))
                .andExpect(jsonPath("$.description").value("My description One"));

        mockMvc.perform(post("/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(second_request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("My title Two"))
                .andExpect(jsonPath("$.description").value("My description Two"));

        mockMvc.perform(get("/todos")
                        .param("page", "0")
                        .param("limit", "10")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void shouldUpdateTodoSuccessfully() throws Exception {
        TodoRequest request = new TodoRequest(
                "My title",
                "My description",
                TodoStatus.TODO
        );

        TodoRequest request_updated = new TodoRequest(
                "My title Updated",
                "My description Updated",
                TodoStatus.TODO
        );

        String responseJson = mockMvc.perform(post("/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("My title"))
                .andExpect(jsonPath("$.description").value("My description"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String todo_id = objectMapper.readTree(responseJson).get("id").asText();

        mockMvc.perform(put("/todos/{id}", todo_id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request_updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("My title Updated"))
                .andExpect(jsonPath("$.description").value("My description Updated"));
    }

    @Test
    void shouldDeleteTodoSuccessfully() throws Exception {
        TodoRequest request = new TodoRequest(
                "My title",
                "My description",
                TodoStatus.TODO
        );

        String responseJson = mockMvc.perform(post("/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("My title"))
                .andExpect(jsonPath("$.description").value("My description"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String todo_id = objectMapper.readTree(responseJson).get("id").asText();

        mockMvc.perform(delete("/todos/{id}", todo_id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
