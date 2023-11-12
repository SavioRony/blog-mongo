package br.com.fiap.blogmongo.controller.handler;

import br.com.fiap.blogmongo.exception.NotFoundException;
import br.com.fiap.blogmongo.model.dto.ExceptionDatailsErro;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerControler {
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<String> handlerOptimisticLockingFailureException(OptimisticLockingFailureException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de concorrência: O Artigo foi atualizado por outro usuario." +
                "Por favor, tente novamente.");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ExceptionDatailsErro> handlerDuplicateKeyException(DuplicateKeyException ex){
        String mensagem = "Objeto ja cadastro para esse codigo!";
        ExceptionDatailsErro body = new ExceptionDatailsErro(HttpStatus.CONFLICT.value(), mensagem);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDatailsErro> handlerNotFoundException(NotFoundException ex){
        ExceptionDatailsErro body = new ExceptionDatailsErro(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
