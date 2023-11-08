package br.com.fiap.blogmongo.service;

import br.com.fiap.blogmongo.model.Artigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {
    List<Artigo> obterTodos();
    Artigo obterPorCodigo(String codigo);
    Artigo criar(Artigo artigo);
     List<Artigo> findByDataGreaterThan(LocalDateTime data);
     List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status);

     void atualizar(Artigo artigo);

    void atualizarArtigo(String id, String novaURL);

    void deleteById(String id);

    void deleteArtigoById(String id);

    List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);

    List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate);

    List<Artigo> obterArtigosComplexos(Integer status, LocalDateTime data, String titulo);

    Page<Artigo> listaArtigos(Pageable pageable);

    List<Artigo> buscarPorStatusOrdenarPorTituloAsc(Integer status);
    List<Artigo> buscarPorStatusOrdenarPorTitulo(Integer status);

    List<Artigo> findByTexto(String searchTerm);


}
