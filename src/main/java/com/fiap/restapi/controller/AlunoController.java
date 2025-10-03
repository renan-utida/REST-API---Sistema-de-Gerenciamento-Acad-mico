package com.fiap.restapi.controller;

import com.fiap.restapi.model.Aluno;
import com.fiap.restapi.service.AlunoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Aluno> criar(@RequestBody Aluno dto){
        Aluno salvo = service.adicionar(dto.getNome(), dto.getCurso());
        return ResponseEntity.created(URI.create("/api/alunos/" + salvo.getId())).body(salvo);
    }

    // READ (list)
    @GetMapping
    public List<Aluno> listar(){
        return service.listar();
    }

    // READ (by id)
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscar(@PathVariable Long id){
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizar(@PathVariable Long id, @RequestBody Aluno dto){
        return service.atualizar(id, dto.getNome(), dto.getCurso())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        boolean removido = service.deletar(id);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}