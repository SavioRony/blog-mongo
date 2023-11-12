package br.com.fiap.blogmongo.model.dto;

import java.util.List;

public record ExceptionArgumentNotValidDatailsErro(int status, String campos, List<String> mensagem) {
}
