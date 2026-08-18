package com.cleoaguiar.todolistapi.controller.api;

import com.cleoaguiar.todolistapi.dto.AuthRequest;
import com.cleoaguiar.todolistapi.dto.AuthResponse;
import com.cleoaguiar.todolistapi.dto.UserRegisterRequest;
import com.cleoaguiar.todolistapi.dto.UserResponse;
import com.cleoaguiar.todolistapi.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;


@Tag(name = "Authentication", description = "Endpoint de autenticação e registro de usuários")
public interface AuthApi {
    @Operation(
            summary = "Registrar usuário",
            description = "Criar uma conta de usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    name = "Solicitação bem-sucedida.",
                                    value = """
                                            {
                                                "token": "eyJhbGciOiJIU..."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso"),
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
                                                "timestamp": "2026-08-15T20:57:43.94644522",
                                                "status": 400,
                                                "error": "Bad Request",
                                                "message": "Erro de validação.",
                                                "errors": {
                                                    "password": "A senha é obrigatória.",
                                                    "email": "O e-mail é obrigatório.",
                                                    "username": "O nome do usuário é obrigatório."
                                                }
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<UserResponse> register(UserRegisterRequest userRegisterRequest);

    @Operation(
            summary = "Autenticar usuário",
            description = "Autentica o usuário e retorna um token JWT"
    )
    @ApiResponses(value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login realizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class),
                                    examples = @ExampleObject(
                                            name = "Solicitação bem-sucedida.",
                                            value = """
                                                    {
                                                        "token": "eyJhbGciOiJIU..."
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Credencias inválidas",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            name = "O acesso ao recurso é proibido.",
                                            value = """
                                                     {
                                                        "timestamp": "2026-08-15T21:17:17",
                                                        "status": 403,
                                                        "error": "Forbidden",
                                                        "message": "Você não tem permissão para acessar esta tarefa.",
                                                        "errors": null
                                                     }
                                                    """
                                    )
                            )
                    )
            })
            ResponseEntity<AuthResponse> login(AuthRequest authRequest);
}
