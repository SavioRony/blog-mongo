package br.com.fiap.blogmongo.service.impl;

import br.com.fiap.blogmongo.model.Autor;
import br.com.fiap.blogmongo.repository.AutorRepository;
import br.com.fiap.blogmongo.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AutorServiceImpl implements AutorService {

    @Autowired
    private AutorRepository repository;

    @Override
    public Autor obterPorCodigo(String codigo) {
        return repository.findById(codigo).orElseThrow(() -> new IllegalArgumentException("Autor não encontrado!"));
    }

    @Override
    public Autor criar(Autor artigo) {
        return repository.save(artigo);
    }
}
