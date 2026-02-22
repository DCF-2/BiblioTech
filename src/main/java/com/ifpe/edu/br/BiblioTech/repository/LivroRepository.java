package com.ifpe.edu.br.BiblioTech.repository;

import com.ifpe.edu.br.BiblioTech.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    // Busca um livro pelo título exato (TC004)
    Optional<Livro> findByTitulo(String titulo);
    
    // Verifica se já existe um livro com o mesmo código ISBN (Segurança para TC013)
    boolean existsByIsbn(String isbn);
}