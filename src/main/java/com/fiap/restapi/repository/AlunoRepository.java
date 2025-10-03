package com.fiap.restapi.repository;

import com.fiap.restapi.config.ConnectionFactory;
import com.fiap.restapi.model.Aluno;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AlunoRepository {

    public Aluno adicionar(Aluno aluno) {
        final String sql = "INSERT INTO ALUNO (NOME, CURSO) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[] { "ID" })) {

            ps.setString(1, aluno.getNome());
            ps.setString(2, aluno.getCurso());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    aluno.setId(rs.getLong(1));
                } else {
                    // fallback (raro, mas por via das dúvidas)
                    try (PreparedStatement ps2 = conn.prepareStatement(
                            "SELECT MAX(ID) FROM ALUNO")) {
                        try (ResultSet r2 = ps2.executeQuery()) {
                            if (r2.next()) aluno.setId(r2.getLong(1));
                        }
                    }
                }
            }
            return aluno;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar aluno: " + e.getMessage(), e);
        }
    }

    public Optional<Aluno> buscarPorId(Long id) {
        final String sql = "SELECT ID, NOME, CURSO FROM ALUNO WHERE ID = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Aluno(rs.getLong("ID"),
                                                 rs.getString("NOME"),
                                                 rs.getString("CURSO")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno por ID: " + e.getMessage(), e);
        }
    }

    public List<Aluno> listar() {
        final String sql = "SELECT ID, NOME, CURSO FROM ALUNO ORDER BY ID";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Aluno> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(new Aluno(rs.getLong("ID"),
                                    rs.getString("NOME"),
                                    rs.getString("CURSO")));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar alunos: " + e.getMessage(), e);
        }
    }

    public Optional<Aluno> atualizar(Long id, Aluno dados) {
        final String sql = "UPDATE ALUNO SET NOME = ?, CURSO = ? WHERE ID = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dados.getNome());
            ps.setString(2, dados.getCurso());
            ps.setLong(3, id);
            int linhas = ps.executeUpdate();
            if (linhas == 0) return Optional.empty();
            return buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar aluno: " + e.getMessage(), e);
        }
    }

    public boolean deletar(Long id) {
        final String sql = "DELETE FROM ALUNO WHERE ID = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar aluno: " + e.getMessage(), e);
        }
    }
}