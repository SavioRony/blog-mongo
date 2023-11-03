package br.com.fiap.blogmongo.service;

import br.com.fiap.blogmongo.model.Artigo;

import java.util.List;

public interface ArtigoService {
    List<Artigo> obterTodos();
    Artigo obterPorCodigo(String codigo);
    Artigo criar(Artigo artigo);
}
