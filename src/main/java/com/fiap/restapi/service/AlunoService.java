package com.fiap.restapi.service;

import com.fiap.restapi.model.Aluno;
import com.fiap.restapi.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository repo;

    public AlunoService(AlunoRepository repo){
        this.repo = repo;
    }

    public Aluno adicionar(String nome, String curso){
        validar(nome, curso);
        Aluno aluno = new Aluno(null, nome.trim(), curso.trim());
        return repo.adicionar(aluno);
    }

    public Optional<Aluno> buscarPorId(Long id){
        if(id == null || id <= 0) throw new IllegalArgumentException("Id inválido");
        return repo.buscarPorId(id);
    }

    public List<Aluno> listar(){
        return repo.listar();
    }

    public Optional<Aluno> atualizar(Long id, String nome, String curso){
        if(id == null || id <= 0) throw new IllegalArgumentException("Id inválido");
        validar(nome, curso);
        return repo.atualizar(id, new Aluno(id, nome.trim(), curso.trim()));
    }

    public boolean deletar(Long id){
        if(id == null || id <= 0) throw new IllegalArgumentException("Id inválido");
        return repo.deletar(id);
    }

    private void validar(String nome, String curso){
        if(nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome é obrigatório");
        if(curso == null || curso.isBlank()) throw new IllegalArgumentException("Curso é obrigatório");
        if(nome.length() > 150) throw new IllegalArgumentException("Nome excede 150 caracteres");
        if(curso.length() > 150) throw new IllegalArgumentException("Curso excede 150 caracteres");
    }
}
