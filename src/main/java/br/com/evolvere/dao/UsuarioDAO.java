package br.com.evolvere.dao;

import br.com.evolvere.to.UsuarioTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Listar todos
    public List<UsuarioTO> findAll() {
        List<UsuarioTO> lista = new ArrayList<>();

        String sql = "SELECT id, nome, email, senha, data_nascimento FROM USUARIO";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UsuarioTO u = new UsuarioTO(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDate("data_nascimento"),
                        rs.getString("email"),
                        rs.getString("senha")
                );
                lista.add(u);
            }

            System.out.println("Usuarios encontrados: " + lista.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    // Criar usuário / salvar usuário
    public UsuarioTO save(UsuarioTO usuario) {

        String sql = "INSERT INTO USUARIO (nome, email, senha, data_nascimento) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setDate(4, new java.sql.Date(usuario.getDataDeNascimento().getTime()));

            stmt.executeUpdate();

            // Recuperando ID gerado automaticamente
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }

            System.out.println("Usuário cadastrado: " + usuario.getNome());
            return usuario;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    // Atualizar por id
    public UsuarioTO update(UsuarioTO usuario) {

        String sql = "UPDATE USUARIO SET nome = ?, email = ?, senha = ?, data_nascimento = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setDate(4, new java.sql.Date(usuario.getDataDeNascimento().getTime()));
            stmt.setInt(5, usuario.getId());

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Usuário atualizado: ID " + usuario.getId());
                return usuario;
            }

            System.out.println("Nenhum usuário encontrado com ID: " + usuario.getId());
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    // Deletar (por id)
    public boolean delete(int id) {

        String sql = "DELETE FROM USUARIO WHERE id = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Usuário removido: ID " + id);
                return true;
            }

            System.out.println("Usuário não encontrado para remoção: ID " + id);
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Login
    public UsuarioTO login(String email, String senha) {

        String sql = "SELECT id, nome, email, senha, data_nascimento FROM USUARIO WHERE email = ? AND senha = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UsuarioTO usuario = new UsuarioTO(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDate("data_nascimento"),
                        rs.getString("email"),
                        rs.getString("senha")
                );

                System.out.println("Login ok: " + email);
                return usuario;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Login falhou: " + email);
        return null;
    }
}