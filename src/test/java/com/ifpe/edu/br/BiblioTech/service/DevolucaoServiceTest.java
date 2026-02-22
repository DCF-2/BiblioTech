package com.ifpe.edu.br.BiblioTech.service;

import com.ifpe.edu.br.BiblioTech.model.Emprestimo;
import com.ifpe.edu.br.BiblioTech.model.Livro;
import com.ifpe.edu.br.BiblioTech.model.Multa;
import com.ifpe.edu.br.BiblioTech.model.Usuario;
import com.ifpe.edu.br.BiblioTech.repository.EmprestimoRepository;
import com.ifpe.edu.br.BiblioTech.repository.LivroRepository;
import com.ifpe.edu.br.BiblioTech.repository.MultaRepository;
import com.ifpe.edu.br.BiblioTech.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DevolucaoServiceTest {

    @Mock private EmprestimoRepository emprestimoRepository;
    @Mock private LivroRepository livroRepository;
    @Mock private MultaRepository multaRepository;
    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DevolucaoService devolucaoService;

    private Emprestimo emprestimoMock;
    private Livro livroMock;
    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setStatusAtividade("ATIVO");

        livroMock = new Livro();
        livroMock.setId(1L);
        livroMock.setQuantidadeDisponivel(2);

        emprestimoMock = new Emprestimo();
        emprestimoMock.setId(1L);
        emprestimoMock.setUsuario(usuarioMock);
        emprestimoMock.setLivro(livroMock);
        emprestimoMock.setDataDevolucaoPrevista(LocalDate.of(2026, 2, 20));
    }

    @Test
    @DisplayName("TC011 - Registar devolução no prazo sem multas")
    void deveRegistrarDevolucaoNoPrazo() throws Exception {
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimoMock));

        // Devolução no dia exato previsto
        String resultado = devolucaoService.registrarDevolucao(1L, LocalDate.of(2026, 2, 20));

        assertEquals("Devolução registrada com sucesso. Livro disponível para novo empréstimo.", resultado);
        assertEquals(3, livroMock.getQuantidadeDisponivel()); // Incrementou o stock
        assertEquals("FINALIZADO", emprestimoMock.getStatus());
        verify(multaRepository, never()).save(any(Multa.class));
    }

    @Test
    @DisplayName("TC012 - Registar devolução atrasada e calcular multa")
    void deveGerarMultaPorAtraso() throws Exception {
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimoMock));

        // Devolução com 1 dia de atraso (Previsto: 20/02, Entrega: 21/02)
        String resultado = devolucaoService.registrarDevolucao(1L, LocalDate.of(2026, 2, 21));

        assertEquals("Devolução em atraso (1 dia(s)). Multa de R$ 2,00 gerada para o usuário.", resultado);
        assertEquals("Com Pendência", usuarioMock.getStatusAtividade());
        verify(multaRepository, times(1)).save(any(Multa.class));
    }
}