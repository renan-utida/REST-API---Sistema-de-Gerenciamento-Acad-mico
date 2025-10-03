package com.fiap.restapi.controller;

import com.fiap.restapi.model.Professor;
import com.fiap.restapi.service.ProfessorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Professor> criar(@RequestBody Professor dto) {
        Professor salvo = service.adicionar(
                dto.getNome(),
                dto.getDepartamento(),
                dto.getEmail(),
                dto.getTitulacao()
        );
        return ResponseEntity.created(URI.create("/api/professores/" + salvo.getId())).body(salvo);
    }

    // READ (list)
    @GetMapping
    public List<Professor> listar() {
        return service.listar();
    }

    // READ (by id)
    @GetMapping("/{id}")
    public ResponseEntity<Professor> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizar(@PathVariable Long id, @RequestBody Professor dto) {
        return service.atualizar(
                        id,
                        dto.getNome(),
                        dto.getDepartamento(),
                        dto.getEmail(),
                        dto.getTitulacao()
                )
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removido = service.deletar(id);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}