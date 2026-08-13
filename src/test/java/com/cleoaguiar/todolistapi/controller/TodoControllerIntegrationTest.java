package com.cleoaguiar.todolistapi.controller;

import com.cleoaguiar.todolistapi.dto.AuthRequest;
import com.cleoaguiar.todolistapi.dto.TodoRequest;
import com.cleoaguiar.todolistapi.dto.UserRegisterRequest;
import com.cleoaguiar.todolistapi.enums.TodoStatus;
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

    private final ObjectMapper objectMapper =  new ObjectMapper();
    private String token;

    private String login(String email, String password) throws Exception {
        AuthRequest loginRequest = new AuthRequest(email, password);

        String responseJson = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(responseJson).get("token").asText();
    }

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

        this.token = login(
                "cleo@email.com",
                "123456");
    }

    private String createTodo(TodoRequest todoRequest, String token) throws Exception {
        return mockMvc.perform(post("/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void shouldCreateTodoSuccessfully() throws Exception {
        TodoRequest request = new TodoRequest(
                "Todo title",
                "Todo description",
                TodoStatus.TODO
        );

        mockMvc.perform(post("/todos")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Todo title"))
                .andExpect(jsonPath("$.description").value("Todo description"));
    }

    @Test
    void shouldListTodosSuccessfully() throws Exception {
        TodoRequest firstRequest = new TodoRequest(
                "Todo title One",
                "Todo description One",
                TodoStatus.TODO
        );

        TodoRequest secondRequest = new TodoRequest(
                "Todo title Two",
                "Todo description Two",
                TodoStatus.TODO
        );

        mockMvc.perform(post("/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Todo title One"))
                .andExpect(jsonPath("$.description").value("Todo description One"));

        mockMvc.perform(post("/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Todo title Two"))
                .andExpect(jsonPath("$.description").value("Todo description Two"));

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
                "Todo title",
                "Todo description",
                TodoStatus.TODO
        );

        TodoRequest requestUpdated = new TodoRequest(
                "Updated todo title",
                "Updated todo description",
                TodoStatus.TODO
        );

        String responseJson = createTodo(request, token);
        String todoId = objectMapper.readTree(responseJson).get("id").asText();

        mockMvc.perform(put("/todos/{id}", todoId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated todo title"))
                .andExpect(jsonPath("$.description").value("Updated todo description"));
    }

    @Test
    void shouldDeleteTodoSuccessfully() throws Exception {
        TodoRequest request = new TodoRequest(
                "Todo title",
                "Todo description",
                TodoStatus.TODO
        );

        String responseJson = createTodo(request, token);
        String todoId = objectMapper.readTree(responseJson).get("id").asText();;

        mockMvc.perform(delete("/todos/{id}", todoId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnForbiddenWhenAccessingAnotherUsersTodo() throws Exception {
        TodoRequest request = new TodoRequest(
                "Owner todo title",
                "Owner todo description",
                TodoStatus.TODO
        );

        String responseJson = createTodo(request, token);
        String todoId = objectMapper.readTree(responseJson).get("id").asText();

        UserRegisterRequest userRequest = new UserRegisterRequest(
                "user",
                "user@email.com",
                "654321");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());

        String userToken = login(
                "user@email.com",
                "654321"
        );

        TodoRequest updateRequest = new TodoRequest(
                "Todo created by another user",
                "Todo created by another user description",
                TodoStatus.TODO
        );

        mockMvc.perform(put("/todos/{id}", todoId)
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden());
    }
}
