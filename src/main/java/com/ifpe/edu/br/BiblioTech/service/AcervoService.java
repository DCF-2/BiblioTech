package com.ifpe.edu.br.BiblioTech.service;

import com.ifpe.edu.br.BiblioTech.dto.LivroDTO;
import com.ifpe.edu.br.BiblioTech.model.Livro;
import com.ifpe.edu.br.BiblioTech.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AcervoService {

    @Autowired
    private LivroRepository livroRepository;

    // TC004: Busca de exemplares pelo título
    public Livro buscarPorTitulo(String titulo) throws Exception {
        Optional<Livro> livroOpt = livroRepository.findByTitulo(titulo);
        
        if (livroOpt.isEmpty()) {
            throw new Exception("Livro não encontrado no catálogo");
        }
        
        return livroOpt.get();
    }

    // TC013: Adicionar um novo título ao acervo
    public Livro adicionarLivro(LivroDTO dto) throws Exception {
        if (livroRepository.existsByIsbn(dto.getIsbn())) {
            throw new Exception("Livro com este ISBN já está cadastrado.");
        }

        Livro novoLivro = new Livro();
        novoLivro.setTitulo(dto.getTitulo());
        novoLivro.setAutor(dto.getAutor());
        novoLivro.setIsbn(dto.getIsbn());
        
        // A quantidade disponível inicial é igual a quantidade total comprada/cadastrada
        novoLivro.setQuantidadeTotal(dto.getQuantidadeTotal());
        novoLivro.setQuantidadeDisponivel(dto.getQuantidadeTotal());
        
        novoLivro.setTemReserva(false);

        return livroRepository.save(novoLivro);
    }
}