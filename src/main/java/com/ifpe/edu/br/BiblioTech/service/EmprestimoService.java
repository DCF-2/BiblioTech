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
import java.util.List;
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

    // TC008 e TC009: Regras de Renovação
    public String renovarEmprestimo(Long emprestimoId) throws Exception {
        Optional<Emprestimo> empOpt = emprestimoRepository.findById(emprestimoId);
        if (empOpt.isEmpty()) {
            throw new Exception("Empréstimo não encontrado.");
        }

        Emprestimo emprestimo = empOpt.get();
        Livro livro = emprestimo.getLivro();

        // TC009: Verifica se tem reserva
        if (livro.isTemReserva()) {
            throw new Exception("Renovação não permitida. Este item possui reserva aguardando.");
        }

        // TC008: Renova por mais 7 dias a partir da data de devolução prevista atual
        emprestimo.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista().plusDays(7));
        emprestimo.setStatus("Renovado");
        emprestimoRepository.save(emprestimo);

        return "Empréstimo renovado com sucesso.";
    }

    // TC014: Consulta de Histórico
    public List<Emprestimo> consultarHistorico(Usuario usuario) {
        return emprestimoRepository.findByUsuarioOrderByDataEmprestimoDesc(usuario);
    }
}