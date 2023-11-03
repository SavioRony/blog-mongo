package br.com.fiap.blogmongo.repository;

import br.com.fiap.blogmongo.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArtigoRepository extends MongoRepository<Artigo, String> {
    List<Artigo> findByDataGreaterThan(LocalDateTime date);
}
