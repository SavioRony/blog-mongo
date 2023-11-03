package br.com.fiap.blogmongo.service.impl;

import br.com.fiap.blogmongo.model.Artigo;
import br.com.fiap.blogmongo.repository.ArtigoRepository;
import br.com.fiap.blogmongo.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtigoServiceImpl implements ArtigoService {

    @Autowired
    private ArtigoRepository repository;
    @Override
    public List<Artigo> obterTodos() {
        return repository.findAll();
    }

    @Override
    public Artigo obterPorCodigo(String codigo) {
        return repository.findById(codigo).orElseThrow(() -> new IllegalArgumentException("Artigo não encontrado!"));
    }

    @Override
    public Artigo criar(Artigo artigo) {
        return repository.save(artigo);
    }
}
