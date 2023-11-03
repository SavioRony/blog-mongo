package br.com.fiap.blogmongo.repository;

import br.com.fiap.blogmongo.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtigoRepository extends MongoRepository<Artigo, String> {
}
