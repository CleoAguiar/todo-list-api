package com.cleoaguiar.todolistapi.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse (
        @Schema(description = "Momento em que o erro ocorreu", example = "2026-08-15T21:17:17")
        LocalDateTime timestamp,

        @Schema(description = "Código HTTP do erro", example = "404")
        int status,

        @Schema(description = "Descrição do status HTTP", example = "Not Found")
        String error,

        @Schema(description = "Mensagem explicativa do erro", example = "Todo com id 1 não encontrado.")
        String message,

        @Schema(description = "Mapa de erros de validação por campo (quando aplicável)")
        Map<String, String> errors
){
}
