package br.com.fiap.blogmongo.controller;

import br.com.fiap.blogmongo.model.Artigo;
import br.com.fiap.blogmongo.model.dto.ArtigoStatusCount;
import br.com.fiap.blogmongo.model.dto.ArtigoUrlDto;
import br.com.fiap.blogmongo.model.dto.AutorTotalArtigo;
import br.com.fiap.blogmongo.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/artigos")
public class ArtigoController {

    @Autowired
    private ArtigoService artigoService;

    @GetMapping
    public List<Artigo> obterTodos(){
        return artigoService.obterTodos();
    }

    @GetMapping("/{codigo}")
    public Artigo obterPorCodigo(@PathVariable String codigo){
        return artigoService.obterPorCodigo(codigo);
    }

    @PostMapping
    public Artigo criar(@RequestBody Artigo artigo){
        return artigoService.criar(artigo);
    }

    @GetMapping("/maior-data")
    public List<Artigo> findByGreaterThan(@RequestParam("data")LocalDateTime data){
        return artigoService.findByDataGreaterThan(data);
    }

    @GetMapping("/data-status")
    public List<Artigo> findByDataAndStatus(@RequestParam("data") LocalDateTime data, @RequestParam(value = "status", defaultValue = "1") Integer status){
        return artigoService.findByDataAndStatus(data, status);
    }

    @PutMapping
    public void atualizar(@RequestBody Artigo artigo){
        artigoService.atualizar(artigo);
    }

    @PutMapping("/{id}")
    public void atualizarURL(@PathVariable String id, @RequestBody ArtigoUrlDto dto){
        artigoService.atualizarArtigo(id, dto.novaUrl());
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id){
        artigoService.deleteById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteArtigoById(@PathVariable String id){
        artigoService.deleteArtigoById(id);
    }


    @GetMapping("/status-maiordata")
    public List<Artigo> findByStatusAndDataGreaterThan(@RequestParam(value = "status", defaultValue = "1") Integer status,
                                                       @RequestParam("data") LocalDateTime data){
        return artigoService.findByStatusAndDataGreaterThan(status,data);
    }

    @GetMapping("/data-hora")
    public List<Artigo> obterArtigosEntreDatas(@RequestParam("de") LocalDateTime de,
                                                       @RequestParam("ate") LocalDateTime ate){
        return artigoService.obterArtigoPorDataHora(de,ate);
    }

    @GetMapping("/complexo")
    public List<Artigo> buscarArtigosComplexos(@RequestParam("status") Integer status,
                                               @RequestParam("data") LocalDateTime data,
                                               @RequestParam("titulo") String titulo){
        return artigoService.obterArtigosComplexos(status, data, titulo);
    }

    @GetMapping("/listar-paginado")
    public ResponseEntity<Page<Artigo>> listaArtigos(Pageable pageable){
        return ResponseEntity.ok(artigoService.listaArtigos(pageable));
    }

    @GetMapping("/listar-ordenado-titulo-asc")
    public List<Artigo> buscarOrdenadoPorTitulo(@RequestParam Integer status){
        return artigoService.buscarPorStatusOrdenarPorTituloAsc(status);
    }

    @GetMapping("/listar-ordenado-titulo")
    public List<Artigo> buscarOrdenadoPorTitulov2(@RequestParam Integer status){
        return artigoService.buscarPorStatusOrdenarPorTitulo(status);
    }

    @GetMapping("/buscar-texto")
    public List<Artigo> buscarPorTexto(@RequestParam("searchTerm") String termo){
        return artigoService.findByTexto(termo);
    }

    @GetMapping("/contar-status")
    public List<ArtigoStatusCount> contaArtigosPorStatus(){
        return artigoService.contarArtigoPorStatus();
    }

    @GetMapping("/artigo-autor")
    public List<AutorTotalArtigo> calcularTotalArtigosPorAutorNoPeriodo(@RequestParam("inicio") LocalDateTime inicio, @RequestParam("fim") LocalDateTime fim){
        return artigoService.calcularTotalArtigosPorAutorNoPeriodo(inicio, fim);
    }


}
