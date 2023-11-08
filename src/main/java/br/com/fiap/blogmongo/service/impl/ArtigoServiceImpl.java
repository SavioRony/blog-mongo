package br.com.fiap.blogmongo.service.impl;

import br.com.fiap.blogmongo.model.Artigo;
import br.com.fiap.blogmongo.model.Autor;
import br.com.fiap.blogmongo.repository.ArtigoRepository;
import br.com.fiap.blogmongo.repository.AutorRepository;
import br.com.fiap.blogmongo.service.ArtigoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
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

    @Override
    public void atualizar(Artigo artigo) {
        if(repository.existsById(artigo.getCodigo())){
            if(artigo.getAutor() != null && artigo.getAutor().getCodigo() != null){
                Autor autor = autorRepository.findById(artigo.getAutor().getCodigo()).orElseThrow(() ->
                        new IllegalArgumentException("Autor Inexistente!"));
                artigo.setAutor(autor);
            }
            mongoTemplate.save(artigo);
        }else{
            throw new IllegalArgumentException("Artigo não existe!");
        }
    }

    @Override
    public void atualizarArtigo(String id, String novaURL) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("url", novaURL);
        this.mongoTemplate.updateFirst(query, update, Artigo.class);
    }

    @Override
    public void deleteById(String id) {
        this.repository.deleteById(id);

    }

    @Override
    public void deleteArtigoById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Artigo.class);
    }

    @Override
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data) {
        return this.repository.findByStatusAndDataGreaterThan(status, data);
    }

    @Override
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate) {
        return repository.obterArtigoPorDataHora(de, ate);
    }

    @Override
    public List<Artigo> obterArtigosComplexos(Integer status, LocalDateTime data, String titulo) {
        Criteria criteria = new Criteria();
        criteria.and("data").lte(data);
        if(status != null){
            criteria.and("status").is(status);
        }

        if(titulo != null && !titulo.isEmpty()){
            criteria.and("titulo").regex(titulo, "i");
        }

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public Page<Artigo> listaArtigos(Pageable pageable) {
        Sort sort = Sort.by("titulo").ascending();
        Pageable pag = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return this.repository.findAll(pag);
    }

    @Override
    public List<Artigo> buscarPorStatusOrdenarPorTituloAsc(Integer status) {
        return this.repository.findByStatusOrderByTituloAsc(status);
    }

    @Override
    public List<Artigo> buscarPorStatusOrdenarPorTitulo(Integer status) {
        return this.repository.obterArtigoPorStatusComOrdenacao(status);
    }

    @Override
    public List<Artigo> findByTexto(String searchTerm) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchTerm);
        Query query = TextQuery.queryText(criteria).sortByScore();
        return mongoTemplate.find(query, Artigo.class);
    }
}
