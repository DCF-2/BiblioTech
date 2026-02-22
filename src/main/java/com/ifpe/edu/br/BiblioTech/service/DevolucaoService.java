package com.ifpe.edu.br.BiblioTech.service;

import com.ifpe.edu.br.BiblioTech.model.Emprestimo;
import com.ifpe.edu.br.BiblioTech.model.Livro;
import com.ifpe.edu.br.BiblioTech.model.Multa;
import com.ifpe.edu.br.BiblioTech.model.Usuario;
import com.ifpe.edu.br.BiblioTech.repository.EmprestimoRepository;
import com.ifpe.edu.br.BiblioTech.repository.LivroRepository;
import com.ifpe.edu.br.BiblioTech.repository.MultaRepository;
import com.ifpe.edu.br.BiblioTech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class DevolucaoService {

    @Autowired private EmprestimoRepository emprestimoRepository;
    @Autowired private LivroRepository livroRepository;
    @Autowired private MultaRepository multaRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public String registrarDevolucao(Long emprestimoId, LocalDate dataEntregaReal) throws Exception {
        Optional<Emprestimo> empOpt = emprestimoRepository.findById(emprestimoId);
        if (empOpt.isEmpty()) {
            throw new Exception("Empréstimo não encontrado.");
        }

        Emprestimo emprestimo = empOpt.get();
        Livro livro = emprestimo.getLivro();
        Usuario usuario = emprestimo.getUsuario();

        // Atualiza os dados do empréstimo
        emprestimo.setDataDevolucaoReal(dataEntregaReal);
        emprestimo.setStatus("FINALIZADO");

        // Devolve o livro à prateleira (TC011)
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);

        String mensagemFinal = "Devolução registrada com sucesso. Livro disponível para novo empréstimo.";

        // Verifica Atraso (TC012)
        if (dataEntregaReal.isAfter(emprestimo.getDataDevolucaoPrevista())) {
            long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataDevolucaoPrevista(), dataEntregaReal);
            
            // Regra: 2 reais por dia de atraso
            BigDecimal valorMulta = new BigDecimal(diasAtraso * 2.0); 

            Multa multa = new Multa();
            multa.setUsuario(usuario);
            multa.setEmprestimo(emprestimo);
            multa.setValor(valorMulta);
            multa.setStatus("PENDENTE");
            multaRepository.save(multa);

            // Bloqueia o utilizador
            usuario.setStatusAtividade("Com Pendência");
            usuarioRepository.save(usuario);

            mensagemFinal = String.format("Devolução em atraso (%d dia(s)). Multa de R$ %.2f gerada para o usuário.", diasAtraso, valorMulta);
        }

        emprestimoRepository.save(emprestimo);
        livroRepository.save(livro);

        return mensagemFinal;
    }
}