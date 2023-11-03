package br.com.fiap.blogmongo.service.impl;

import br.com.fiap.blogmongo.model.Artigo;
import br.com.fiap.blogmongo.model.Autor;
import br.com.fiap.blogmongo.repository.ArtigoRepository;
import br.com.fiap.blogmongo.repository.AutorRepository;
import br.com.fiap.blogmongo.service.ArtigoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ArtigoServiceImpl implements ArtigoService {

    @Autowired
    private ArtigoRepository repository;
    @Autowired
    private AutorRepository autorRepository;

    private final MongoTemplate mongoTemplate;
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
        if(artigo.getAutor() != null && artigo.getAutor().getCodigo() != null){
            Autor autor = autorRepository.findById(artigo.getAutor().getCodigo()).orElseThrow(() ->
                    new IllegalArgumentException("Autor Inexistente!"));
            artigo.setAutor(autor);
        }else{
            artigo.setAutor(null);
        }
        return repository.save(artigo);
    }

    @Override
    public List<Artigo> findByDataGreaterThan(LocalDateTime data) {
        Query query = new Query(Criteria.where("data").gt(data));
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status) {
        Query query = new Query(Criteria.where("data").is(data).and("status").is(status));
        return mongoTemplate.find(query, Artigo.class);
    }
}
