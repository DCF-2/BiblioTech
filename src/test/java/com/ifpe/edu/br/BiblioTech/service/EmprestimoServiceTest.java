package com.ifpe.edu.br.BiblioTech.service;

import com.ifpe.edu.br.BiblioTech.dto.SolicitacaoEmprestimoDTO;
import com.ifpe.edu.br.BiblioTech.model.Emprestimo;
import com.ifpe.edu.br.BiblioTech.model.Livro;
import com.ifpe.edu.br.BiblioTech.model.Usuario;
import com.ifpe.edu.br.BiblioTech.repository.EmprestimoRepository;
import com.ifpe.edu.br.BiblioTech.repository.LivroRepository;
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
class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private EmprestimoService emprestimoService;

    private Usuario usuarioMock;
    private Livro livroDisponivel;
    private Livro livroIndisponivel;

    @BeforeEach
    void setUp() {
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNome("Davi");

        livroDisponivel = new Livro();
        livroDisponivel.setId(1L);
        livroDisponivel.setTitulo("O Hobbit");
        livroDisponivel.setQuantidadeTotal(3);
        livroDisponivel.setQuantidadeDisponivel(3);

        livroIndisponivel = new Livro();
        livroIndisponivel.setId(2L);
        livroIndisponivel.setTitulo("Dom Casmurro");
        livroIndisponivel.setQuantidadeTotal(2);
        livroIndisponivel.setQuantidadeDisponivel(0); // 0 cópias (TC005)
    }

    @Test
    @DisplayName("TC006 - Tentar solicitar empréstimo com campos vazios")
    void deveValidarCamposObrigatorios() {
        SolicitacaoEmprestimoDTO dto = new SolicitacaoEmprestimoDTO();
        dto.setTituloLivro(""); // Vazio
        dto.setAutor("");       // Vazio

        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> emprestimoService.solicitarEmprestimo(usuarioMock, dto));
            
        assertEquals("Campo obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("TC005 - Tentar solicitar livro sem cópias disponíveis")
    void deveBloquearLivroSemEstoque() {
        SolicitacaoEmprestimoDTO dto = new SolicitacaoEmprestimoDTO();
        dto.setTituloLivro("Dom Casmurro");
        dto.setAutor("Machado de Assis");

        when(livroRepository.findByTitulo("Dom Casmurro")).thenReturn(Optional.of(livroIndisponivel));

        Exception exception = assertThrows(Exception.class, 
            () -> emprestimoService.solicitarEmprestimo(usuarioMock, dto));
            
        assertEquals("Livro indisponível para empréstimo no momento", exception.getMessage());
    }

    @Test
    @DisplayName("TC007 - Solicitar livro disponível com sucesso")
    void deveAprovarEmprestimoComSucesso() throws Exception {
        SolicitacaoEmprestimoDTO dto = new SolicitacaoEmprestimoDTO();
        dto.setTituloLivro("O Hobbit");
        dto.setAutor("J.R.R. Tolkien");

        when(livroRepository.findByTitulo("O Hobbit")).thenReturn(Optional.of(livroDisponivel));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenAnswer(i -> i.getArguments()[0]);

        String resultado = emprestimoService.solicitarEmprestimo(usuarioMock, dto);

        assertEquals("Solicitação aprovada. Retire o livro no balcão em até 24h.", resultado);
        assertEquals(2, livroDisponivel.getQuantidadeDisponivel()); // Estoque deve ter diminuído
        
        // Verifica se o save do empréstimo foi chamado 1 vez
        verify(emprestimoRepository, times(1)).save(any(Emprestimo.class)); 
    }
}