package com.ifpe.edu.br.BiblioTech.repository;

import com.ifpe.edu.br.BiblioTech.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
}