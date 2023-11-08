package br.com.fiap.blogmongo.repository;

import br.com.fiap.blogmongo.model.Artigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArtigoRepository extends MongoRepository<Artigo, String> {
    List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);

    void deleteById(String id);

    @Query("{ $and:  [{'data': {$gte:  ?0}}, {'data':  {$lte:  ?1}}]}")
    List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate);

    @Override
    Page<Artigo> findAll(Pageable pageable);

    List<Artigo> findByStatusOrderByTituloAsc(Integer status);

    @Query(value = "{ 'status' :  { $eq: ?0 } }", sort = "{ 'titulo' : 1 }")
    List<Artigo> obterArtigoPorStatusComOrdenacao(Integer status);
}
