package com.cleoaguiar.todolistapi.controller;

import com.cleoaguiar.todolistapi.dto.TodoRequest;
import com.cleoaguiar.todolistapi.dto.TodoResponse;
import com.cleoaguiar.todolistapi.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
@Tag(name = "Tasks", description = "Operações de gerenciamento de tarefas")
@SecurityRequirement(name = "bearerAuth")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    @Operation(
            summary = "Listar tarefas",
            description = "Retorna uma lista paginada das tarefas do usuário autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public Page<TodoResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int limit) {
        return todoService.getAll(page, limit);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Criar tarefa",
            description = "Cria uma nova tarefa para o usuário  autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public TodoResponse create(@Valid @RequestBody TodoRequest request) {
        return todoService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar tarefa por ID",
            description = "Retorna uma tarefa específica do usuário autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public TodoResponse getById(@PathVariable Long id) {
        return todoService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Excluir tarefa",
            description = "Remove uma tarefa existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarefa  removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public void delete(@PathVariable Long id) {
        todoService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar tarefa",
            description = "Atualizar os dados de uma tarefa existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public TodoResponse update(@PathVariable Long id, @Valid @RequestBody TodoRequest request) {
        return todoService.update(id, request);
    }
}

