package br.com.fiap.blogmongo.service.impl;

import br.com.fiap.blogmongo.exception.NotFoundException;
import br.com.fiap.blogmongo.model.Artigo;
import br.com.fiap.blogmongo.model.Autor;
import br.com.fiap.blogmongo.model.dto.ArtigoStatusCount;
import br.com.fiap.blogmongo.model.dto.AutorTotalArtigo;
import br.com.fiap.blogmongo.repository.ArtigoRepository;
import br.com.fiap.blogmongo.repository.AutorRepository;
import br.com.fiap.blogmongo.service.ArtigoService;
import com.mongodb.DuplicateKeyException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    @Transactional(readOnly = true)
    public Artigo obterPorCodigo(String codigo) {
        return repository.findById(codigo).orElseThrow(() -> new IllegalArgumentException("Artigo não encontrado!"));
    }

    @Override
    @Transactional
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
    @Transactional
    public void atualizar(Artigo artigo) {
        if(repository.existsById(artigo.getCodigo())){
            if(artigo.getAutor() != null && artigo.getAutor().getCodigo() != null){
                Autor autor = autorRepository.findById(artigo.getAutor().getCodigo()).orElseThrow(() ->
                        new NotFoundException("Autor Inexistente!"));
                artigo.setAutor(autor);
            }
            try{
                Query query = new Query(Criteria.where("_id").is(artigo.getCodigo()));
                Update update = new Update()
                        .set("url", artigo.getUrl())
                        .set("titulo", artigo.getTitulo())
                        .set("data", artigo.getData())
                        .set("texto", artigo.getTexto())
                        .set("status", artigo.getStatus())
                        .set("autor", artigo.getAutor());
                this.mongoTemplate.updateFirst(query, update, Artigo.class);
            }catch (OptimisticLockingFailureException e){
                Artigo atualizado = repository.findById(artigo.getCodigo()).get();
                Query query = new Query(Criteria.where("_id").is(artigo.getCodigo()));
                Update update = new Update()
                        .set("url", artigo.getUrl())
                        .set("titulo", artigo.getTitulo())
                        .set("data", artigo.getData())
                        .set("texto", artigo.getTexto())
                        .set("status", artigo.getStatus())
                        .set("autor", artigo.getAutor())
                        .set("version", atualizado.getVersion());
                this.mongoTemplate.updateFirst(query, update, Artigo.class);
            }
        }else{
            throw new NotFoundException("Artigo Inexistente!");
        }
    }

    @Override
    @Transactional
    public void atualizarArtigo(String id, String novaURL) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("url", novaURL);
        this.mongoTemplate.updateFirst(query, update, Artigo.class);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        this.repository.deleteById(id);

    }

    @Override
    @Transactional
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

    @Override
    public List<ArtigoStatusCount> contarArtigoPorStatus() {
        TypedAggregation<Artigo> aggregation = Aggregation.newAggregation(Artigo.class,
                Aggregation.group("status").count().as("quantidade"),
                Aggregation.project("quantidade").and("status").previousOperation());
        AggregationResults<ArtigoStatusCount> results = mongoTemplate.aggregate(aggregation, ArtigoStatusCount.class);
        return results.getMappedResults();
    }

    @Override
    public List<AutorTotalArtigo> calcularTotalArtigosPorAutorNoPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        TypedAggregation<Artigo> aggregation = Aggregation.newAggregation(
                Artigo.class,
                Aggregation.match(Criteria.where("data")
                        .gte(dataInicio.toLocalDate().atStartOfDay())
                        .lt(dataFim.plusDays(1).toLocalDate().atStartOfDay())),
                Aggregation.group("autor").count().as("totalArtigos"),
                Aggregation.project("totalArtigos").and("autor").previousOperation()
        );
        AggregationResults<AutorTotalArtigo> results = mongoTemplate.aggregate(aggregation, AutorTotalArtigo.class);
        return results.getMappedResults();
    }
}
