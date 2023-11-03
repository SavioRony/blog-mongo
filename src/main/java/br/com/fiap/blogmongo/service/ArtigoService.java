package br.com.fiap.blogmongo.service;

import br.com.fiap.blogmongo.model.Artigo;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {
    List<Artigo> obterTodos();
    Artigo obterPorCodigo(String codigo);
    Artigo criar(Artigo artigo);
     List<Artigo> findByDataGreaterThan(LocalDateTime data);
     List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status);
}
