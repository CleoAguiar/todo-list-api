package com.cleoaguiar.todolistapi.controller;

import com.cleoaguiar.todolistapi.dto.TodoRequest;
import com.cleoaguiar.todolistapi.dto.TodoResponse;
import com.cleoaguiar.todolistapi.exception.ErrorResponse;
import com.cleoaguiar.todolistapi.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TodoResponse.class),
                            examples = @ExampleObject(
                                    name = "Solicitação bem-sucedida.",
                                    value = """
                                            {
                                                "content": [],
                                                "empty": true,
                                                "first": true,
                                                "last": true,
                                                "number": 0,
                                                "numberOfElements": 0,
                                                "pageable": {
                                                    "offset": 0,
                                                    "pageNumber": 0,
                                                    "pageSize": 10,
                                                    "paged": true,
                                                    "sort": {
                                                        "empty": true,
                                                        "sorted": false,
                                                        "unsorted": true
                                                    },
                                                    "unpaged": false
                                                },
                                                "size": 10,
                                                "sort": {
                                                    "empty": true,
                                                    "sorted": false,
                                                    "unsorted": true
                                                },
                                                "totalElements": 0,
                                                "totalPages": 0
                                            }
                                            """
                            )
                    )),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário não autenticado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "",
                                    value = "1"
                            )
                    ))
    })
    public Page<TodoResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int limit) {
        return todoService.getAll(page, limit);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Criar tarefa",
            description = "Cria uma nova tarefa para o usuário autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tarefa criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TodoResponse.class),
                            examples = @ExampleObject(
                                    name = "Um novo recurso foi criado com sucesso.",
                                    value = """
                                            {
                                                "id": 3,
                                                "title": "Estudar Spring Boot",
                                                "description": "Revisar OpenAPI",
                                                "status": "TODO",
                                                "createdAt": "2026-08-15T21:11:31.798297989",
                                                "updatedAt": "2026-08-15T21:11:31.798317791"
                                            }
                                            """
                            )
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "O servidor não conseguiu entender a solicitação.",
                                    value = """
                                            {
                                                "timestamp": "2026-08-15T21:12:45.730041865",
                                                "status": 400,
                                                "error": "Bad Request",
                                                "message": "Erro de validação.",
                                                "errors": {
                                                    "description": "A descrição deve ter no máximo 500 caracteres.",
                                                    "title": "O título deve ter no máximo 100 caracteres."
                                                }
                                            }
                                            """
                            )
                    )),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário não autenticado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "O acesso ao recurso é proibido.",
                                    value = "1"
                            )
                    ))
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
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefa encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TodoResponse.class),
                            examples = @ExampleObject(
                                    name = "Solicitação bem-sucedida.",
                                    value = """
                                            {
                                                "id": 3,
                                                "title": "Estudar Spring Boot",
                                                "description": "Revisar OpenAPI",
                                                "status": "TODO",
                                                "createdAt": "2026-08-15T21:11:31.798298",
                                                "updatedAt": "2026-08-15T21:11:31.798318"
                                            }
                                            """
                            )
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "O recurso solicitado não pôde ser encontrado.",
                                    value = """
                                            {
                                                "timestamp": "2026-08-15T21:16:41.533375331",
                                                "status": 404,
                                                "error": "Not Found",
                                                "message": "Todo com id 1 não encontrado.",
                                                "errors": null
                                            }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar esta tarefa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "O acesso ao recurso é proibido.",
                                    value = """
                                            {
                                                "timestamp": "2026-08-15T21:17:17.7926887",
                                                "status": 403,
                                                "error": "Forbidden",
                                                "message": "Forbidden",
                                                "errors": null
                                            }
                                            """
                            )
                    ))

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
            @ApiResponse(
                    responseCode = "204",
                    description = "Tarefa  removida com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "O recurso solicitado não pôde ser encontrado.",
                                    value = """
                                            {
                                                "timestamp": "2026-08-15T21:19:29.570603661",
                                                "status": 404,
                                                "error": "Not Found",
                                                "message": "Todo com id 1 não encontrado.",
                                                "errors": null
                                            }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar esta tarefa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "O acesso ao recurso é proibido.",
                                    value = "1"
                            )
                    ))
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
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefa atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TodoResponse.class),
                            examples = @ExampleObject(
                                    name = "Solicitação bem-sucedida.",
                                    value = """
                                            {
                                                "id": 4,
                                                "title": "Estudar Spring Boot - titulo atualizado",
                                                "description": "OpenAPI Atualizado",
                                                "status": "IN_PROGRESS",
                                                "createdAt": "2026-08-15T21:22:13.361624",
                                                "updatedAt": "2026-08-15T21:27:02.19847457"
                                            }
                                            """
                            )
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "O recurso solicitado não pôde ser encontrado.",
                                    value = """
                                            {
                                                "timestamp": "2026-08-15T21:26:30.557923208",
                                                "status": 404,
                                                "error": "Not Found",
                                                "message": "Todo com id 3 não encontrado.",
                                                "errors": null
                                            }
                                            """
                            )
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "O servidor não conseguiu entender a solicitação.",
                                    value = """
                                            {
                                                "timestamp": "2026-08-15T21:27:56.247029332",
                                                "status": 400,
                                                "error": "Bad Request",
                                                "message": "Status inválido. Valores permitidos: TODO, IN_PROGRESS, DONE",
                                                "errors": null
                                            }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar esta tarefa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "O acesso ao recurso é proibido.",
                                    value = "1"
                            )
                    ))
    })
    public TodoResponse update(@PathVariable Long id, @Valid @RequestBody TodoRequest request) {
        return todoService.update(id, request);
    }
}

