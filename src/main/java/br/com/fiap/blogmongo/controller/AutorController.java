package br.com.fiap.blogmongo.controller;

import br.com.fiap.blogmongo.model.Autor;
import br.com.fiap.blogmongo.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autor")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping("/{codigo}")
    public Autor obterPorCodigo(@PathVariable String codigo) {
        return autorService.obterPorCodigo(codigo);
    }

    @PostMapping
    public Autor criar(@RequestBody Autor autor) {
        return autorService.criar(autor);
    }


}
