package com.ifpe.edu.br.BiblioTech.repository;

import com.ifpe.edu.br.BiblioTech.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método mágico do Spring Data para buscar pelo campo 'login'
    Optional<Usuario> findByLogin(String login);
}