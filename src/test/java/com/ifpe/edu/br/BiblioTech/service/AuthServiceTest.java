package com.ifpe.edu.br.BiblioTech.service;

import com.ifpe.edu.br.BiblioTech.dto.LoginDTO;
import com.ifpe.edu.br.BiblioTech.model.Usuario;
import com.ifpe.edu.br.BiblioTech.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthService authService;

    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setLogin("aluno@email.com");
        usuarioMock.setSenha("123456");
        usuarioMock.setCargo("ALUNO");
    }

    @Test
    @DisplayName("TC001 - Deve realizar login com sucesso")
    void deveLogarComSucesso() throws Exception {
        // Cenário
        LoginDTO dto = new LoginDTO();
        dto.setLogin("aluno@email.com");
        dto.setSenha("123456");

        when(usuarioRepository.findByLogin("aluno@email.com")).thenReturn(Optional.of(usuarioMock));

        // Execução
        Usuario resultado = authService.logar(dto);

        // Verificação
        assertNotNull(resultado);
        assertEquals("aluno@email.com", resultado.getLogin());
    }

    @Test
    @DisplayName("TC002 - Deve rejeitar login com senha inválida")
    void deveRejeitarSenhaInvalida() {
        LoginDTO dto = new LoginDTO();
        dto.setLogin("aluno@email.com");
        dto.setSenha("senhaerrada"); // Senha incorreta

        when(usuarioRepository.findByLogin("aluno@email.com")).thenReturn(Optional.of(usuarioMock));

        Exception exception = assertThrows(Exception.class, () -> authService.logar(dto));
        assertEquals("Senha Inválida", exception.getMessage());
    }

    @Test
    @DisplayName("TC003 - Deve validar campos vazios")
    void deveValidarCamposVazios() {
        LoginDTO dto = new LoginDTO();
        dto.setLogin(""); // Vazio
        dto.setSenha("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.logar(dto));
        assertEquals("Preencha os campos obrigatórios", exception.getMessage());
    }
}