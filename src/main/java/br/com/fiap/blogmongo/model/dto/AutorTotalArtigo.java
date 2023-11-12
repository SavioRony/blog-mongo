package br.com.fiap.blogmongo.model.dto;

import br.com.fiap.blogmongo.model.Autor;

public record AutorTotalArtigo(Autor autor, Long totalArtigos) {
}
