package com.ifpe.edu.br.BiblioTech.service;

import com.ifpe.edu.br.BiblioTech.dto.SolicitacaoEmprestimoDTO;
import com.ifpe.edu.br.BiblioTech.model.Emprestimo;
import com.ifpe.edu.br.BiblioTech.model.Livro;
import com.ifpe.edu.br.BiblioTech.model.Usuario;
import com.ifpe.edu.br.BiblioTech.repository.EmprestimoRepository;
import com.ifpe.edu.br.BiblioTech.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroRepository livroRepository;

    public String solicitarEmprestimo(Usuario usuario, SolicitacaoEmprestimoDTO dto) throws Exception {
        
        // TC006: Validação de Campos Obrigatórios
        if (dto.getTituloLivro() == null || dto.getTituloLivro().trim().isEmpty() ||
            dto.getAutor() == null || dto.getAutor().trim().isEmpty()) {
            throw new IllegalArgumentException("Campo obrigatório");
        }

        Optional<Livro> livroOpt = livroRepository.findByTitulo(dto.getTituloLivro());
        if (livroOpt.isEmpty()) {
            throw new Exception("Livro não encontrado no catálogo.");
        }

        Livro livro = livroOpt.get();

        // TC005: Verificação de Disponibilidade
        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new Exception("Livro indisponível para empréstimo no momento");
        }

        // TC007: Aprovação do Empréstimo
        // 1. Baixa no estoque
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroRepository.save(livro);

        // 2. Criação do registro de empréstimo
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(7)); // Prazo de 7 dias
        emprestimo.setStatus("Aguardando Retirada");

        emprestimoRepository.save(emprestimo);

        return "Solicitação aprovada. Retire o livro no balcão em até 24h.";
    }
}