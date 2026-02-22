package com.ifpe.edu.br.BiblioTech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ifpe.edu.br.BiblioTech.model.Emprestimo;
import com.ifpe.edu.br.BiblioTech.model.Usuario;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    // TC014: Busca histórico ordenado do mais recente para o mais antigo
    List<Emprestimo> findByUsuarioOrderByDataEmprestimoDesc(Usuario usuario);
}