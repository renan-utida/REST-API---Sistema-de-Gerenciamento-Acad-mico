package com.fiap.restapi.service;

import com.fiap.restapi.model.Professor;
import com.fiap.restapi.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ProfessorService {

    private final ProfessorRepository repo;

    // Regex simples para validar e-mail
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public ProfessorService(ProfessorRepository repo) {
        this.repo = repo;
    }

    public Professor adicionar(String nome, String departamento, String email, String titulacao) {
        validar(nome, departamento, email, titulacao);
        Professor professor = new Professor(
                null,
                nome.trim(),
                departamento.trim(),
                email.trim(),
                titulacao != null ? titulacao.trim() : null
        );
        return repo.adicionar(professor);
    }

    public Optional<Professor> buscarPorId(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id inválido");
        return repo.buscarPorId(id);
    }

    public List<Professor> listar() {
        return repo.listar();
    }

    public Optional<Professor> atualizar(Long id, String nome, String departamento, String email, String titulacao) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id inválido");
        validar(nome, departamento, email, titulacao);
        Professor professor = new Professor(
                id,
                nome.trim(),
                departamento.trim(),
                email.trim(),
                titulacao != null ? titulacao.trim() : null
        );
        return repo.atualizar(id, professor);
    }

    public boolean deletar(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id inválido");
        return repo.deletar(id);
    }

    private void validar(String nome, String departamento, String email, String titulacao) {
        // Validação de nome
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (nome.length() > 150) {
            throw new IllegalArgumentException("Nome excede 150 caracteres");
        }

        // Validação de departamento
        if (departamento == null || departamento.isBlank()) {
            throw new IllegalArgumentException("Departamento é obrigatório");
        }
        if (departamento.length() > 100) {
            throw new IllegalArgumentException("Departamento excede 100 caracteres");
        }

        // Validação de e-mail
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório");
        }
        if (email.length() > 150) {
            throw new IllegalArgumentException("E-mail excede 150 caracteres");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("E-mail com formato inválido");
        }

        // Validação de titulação (opcional, mas se fornecida deve ter limite)
        if (titulacao != null && titulacao.length() > 80) {
            throw new IllegalArgumentException("Titulação excede 80 caracteres");
        }
    }
}
