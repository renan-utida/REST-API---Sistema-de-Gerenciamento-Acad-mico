package com.fiap.restapi.repository;

import com.fiap.restapi.config.ConnectionFactory;
import com.fiap.restapi.model.Professor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProfessorRepository {

    public Professor adicionar(Professor professor) {
        final String sql = "INSERT INTO PROFESSOR (NOME, DEPARTAMENTO, EMAIL, TITULACAO) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[] { "ID" })) {

            ps.setString(1, professor.getNome());
            ps.setString(2, professor.getDepartamento());
            ps.setString(3, professor.getEmail());
            ps.setString(4, professor.getTitulacao());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    professor.setId(rs.getLong(1));
                } else {
                    // fallback (raro, mas por via das dúvidas)
                    try (PreparedStatement ps2 = conn.prepareStatement(
                            "SELECT MAX(ID) FROM PROFESSOR")) {
                        try (ResultSet r2 = ps2.executeQuery()) {
                            if (r2.next()) professor.setId(r2.getLong(1));
                        }
                    }
                }
            }
            return professor;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar professor: " + e.getMessage(), e);
        }
    }

    public Optional<Professor> buscarPorId(Long id) {
        final String sql = "SELECT ID, NOME, DEPARTAMENTO, EMAIL, TITULACAO FROM PROFESSOR WHERE ID = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Professor(
                            rs.getLong("ID"),
                            rs.getString("NOME"),
                            rs.getString("DEPARTAMENTO"),
                            rs.getString("EMAIL"),
                            rs.getString("TITULACAO")
                    ));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar professor por ID: " + e.getMessage(), e);
        }
    }

    public List<Professor> listar() {
        final String sql = "SELECT ID, NOME, DEPARTAMENTO, EMAIL, TITULACAO FROM PROFESSOR ORDER BY ID";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Professor> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(new Professor(
                        rs.getLong("ID"),
                        rs.getString("NOME"),
                        rs.getString("DEPARTAMENTO"),
                        rs.getString("EMAIL"),
                        rs.getString("TITULACAO")
                ));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar professores: " + e.getMessage(), e);
        }
    }

    public Optional<Professor> atualizar(Long id, Professor dados) {
        final String sql = "UPDATE PROFESSOR SET NOME = ?, DEPARTAMENTO = ?, EMAIL = ?, TITULACAO = ? WHERE ID = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dados.getNome());
            ps.setString(2, dados.getDepartamento());
            ps.setString(3, dados.getEmail());
            ps.setString(4, dados.getTitulacao());
            ps.setLong(5, id);
            int linhas = ps.executeUpdate();
            if (linhas == 0) return Optional.empty();
            return buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar professor: " + e.getMessage(), e);
        }
    }

    public boolean deletar(Long id) {
        final String sql = "DELETE FROM PROFESSOR WHERE ID = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar professor: " + e.getMessage(), e);
        }
    }
}
