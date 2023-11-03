package br.com.fiap.blogmongo.controller;

import br.com.fiap.blogmongo.model.Artigo;
import br.com.fiap.blogmongo.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
