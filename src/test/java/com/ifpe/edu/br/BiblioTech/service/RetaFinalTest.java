package com.ifpe.edu.br.BiblioTech.service;

import com.ifpe.edu.br.BiblioTech.model.Emprestimo;
import com.ifpe.edu.br.BiblioTech.model.Livro;
import com.ifpe.edu.br.BiblioTech.model.Usuario;
import com.ifpe.edu.br.BiblioTech.repository.EmprestimoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetaFinalTest {

    @Mock private EmprestimoRepository emprestimoRepository;
    @InjectMocks private EmprestimoService emprestimoService;
    @InjectMocks private AuthService authService;

    private Emprestimo emprestimoMock;
    private Livro livroMock;
    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);

        livroMock = new Livro();
        livroMock.setId(1L);
        livroMock.setTemReserva(false); // Default: sem reserva

        emprestimoMock = new Emprestimo();
        emprestimoMock.setId(1L);
        emprestimoMock.setUsuario(usuarioMock);
        emprestimoMock.setLivro(livroMock);
        emprestimoMock.setDataDevolucaoPrevista(LocalDate.of(2026, 3, 1));
    }

    @Test
    @DisplayName("TC008 - Deve renovar empréstimo estendendo o prazo em 7 dias")
    void deveRenovarEmprestimoSemReserva() throws Exception {
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimoMock));

        String resultado = emprestimoService.renovarEmprestimo(1L);

        assertEquals("Empréstimo renovado com sucesso.", resultado);
        assertEquals("Renovado", emprestimoMock.getStatus());
        assertEquals(LocalDate.of(2026, 3, 8), emprestimoMock.getDataDevolucaoPrevista()); // +7 dias
    }

    @Test
    @DisplayName("TC009 - Deve impedir renovação de livro com reserva")
    void deveImpedirRenovacaoComReserva() {
        livroMock.setTemReserva(true); // Simulando que outro usuário reservou
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimoMock));

        Exception exception = assertThrows(Exception.class, () -> emprestimoService.renovarEmprestimo(1L));
        assertEquals("Renovação não permitida. Este item possui reserva aguardando.", exception.getMessage());
    }

    @Test
    @DisplayName("TC014 - Deve consultar histórico ordenado de forma decrescente")
    void deveConsultarHistorico() {
        Emprestimo emp1 = new Emprestimo(); emp1.setDataEmprestimo(LocalDate.of(2026, 1, 10));
        Emprestimo emp2 = new Emprestimo(); emp2.setDataEmprestimo(LocalDate.of(2026, 2, 10)); // Mais recente

        // O repositório deve retornar o mais recente primeiro (emp2, depois emp1)
        when(emprestimoRepository.findByUsuarioOrderByDataEmprestimoDesc(usuarioMock))
                .thenReturn(Arrays.asList(emp2, emp1));

        List<Emprestimo> historico = emprestimoService.consultarHistorico(usuarioMock);

        assertEquals(2, historico.size());
        assertEquals(LocalDate.of(2026, 2, 10), historico.get(0).getDataEmprestimo());
    }

    @Test
    @DisplayName("TC015 - Deve simular logout destruindo a sessão")
    void deveRealizarLogout() {
        String resultado = authService.realizarLogout(usuarioMock);
        assertEquals("Sessão encerrada com sucesso. Cache limpo.", resultado);
    }
}