package com.ifpe.edu.br.BiblioTech.service;

import com.ifpe.edu.br.BiblioTech.dto.LoginDTO;
import com.ifpe.edu.br.BiblioTech.model.Usuario;
import com.ifpe.edu.br.BiblioTech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario logar(LoginDTO loginDTO) throws Exception {
        // TC003: Validação de Campos Vazios
        if (loginDTO.getLogin() == null || loginDTO.getLogin().trim().isEmpty() ||
            loginDTO.getSenha() == null || loginDTO.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Preencha os campos obrigatórios");
        }

        // Busca usuário no banco
        Optional<Usuario> usuarioOpt = usuarioRepository.findByLogin(loginDTO.getLogin());

        // Verifica se usuário existe
        if (usuarioOpt.isEmpty()) {
            throw new Exception("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        // TC002: Validação de Senha (comparação simples para fins didáticos)
        if (!usuario.getSenha().equals(loginDTO.getSenha())) {
            throw new Exception("Senha Inválida");
        }

        return usuario;
    }
}