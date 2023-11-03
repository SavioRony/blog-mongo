package br.com.fiap.blogmongo.service;


import br.com.fiap.blogmongo.model.Autor;


public interface AutorService {
    Autor obterPorCodigo(String codigo);
    Autor criar(Autor artigo);
}
