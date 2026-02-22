package com.ifpe.edu.br.BiblioTech.service;

import com.ifpe.edu.br.BiblioTech.dto.LivroDTO;
import com.ifpe.edu.br.BiblioTech.model.Livro;
import com.ifpe.edu.br.BiblioTech.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcervoServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private AcervoService acervoService;

    private Livro livroMock;

    @BeforeEach
    void setUp() {
        livroMock = new Livro();
        livroMock.setId(1L);
        livroMock.setTitulo("Engenharia de Software Moderna");
        livroMock.setAutor("Valdemar Setzer");
        livroMock.setIsbn("123456789");
        livroMock.setQuantidadeTotal(5);
        livroMock.setQuantidadeDisponivel(5);
    }

    @Test
    @DisplayName("TC004 - Deve encontrar um livro existente pelo título exato")
    void deveEncontrarLivroPorTitulo() throws Exception {
        when(livroRepository.findByTitulo("Engenharia de Software Moderna"))
                .thenReturn(Optional.of(livroMock));

        Livro resultado = acervoService.buscarPorTitulo("Engenharia de Software Moderna");

        assertNotNull(resultado);
        assertEquals("Engenharia de Software Moderna", resultado.getTitulo());
        assertEquals(5, resultado.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("TC013 - Funcionário adiciona um novo título ao acervo")
    void deveAdicionarNovoLivro() throws Exception {
        LivroDTO dto = new LivroDTO();
        dto.setTitulo("IA Explicada");
        dto.setAutor("Fulano");
        dto.setIsbn("999888");
        dto.setQuantidadeTotal(5);

        Livro livroSalvo = new Livro();
        livroSalvo.setId(2L);
        livroSalvo.setTitulo(dto.getTitulo());
        livroSalvo.setAutor(dto.getAutor());
        livroSalvo.setIsbn(dto.getIsbn());
        livroSalvo.setQuantidadeTotal(dto.getQuantidadeTotal());
        livroSalvo.setQuantidadeDisponivel(dto.getQuantidadeTotal()); // Garantindo a regra de negócio

        when(livroRepository.existsByIsbn("999888")).thenReturn(false);
        when(livroRepository.save(any(Livro.class))).thenReturn(livroSalvo);

        Livro resultado = acervoService.adicionarLivro(dto);

        assertNotNull(resultado);
        assertEquals("IA Explicada", resultado.getTitulo());
        assertEquals(5, resultado.getQuantidadeDisponivel());
    }
}