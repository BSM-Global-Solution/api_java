package br.com.evolvere.dao;

import br.com.evolvere.to.UsuarioTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Listar todos
    public List<UsuarioTO> findAll() {
        List<UsuarioTO> lista = new ArrayList<>();

        String sql = "SELECT id, nome, email, senha, data_nascimento FROM tb_usuario";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UsuarioTO u = new UsuarioTO(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDate("data_nascimento").toLocalDate(),
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


    // Verificar se e-mail já existe
    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM tb_usuario WHERE email = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.err.println("Erro ao verificar email!");
            e.printStackTrace();
            return false;
        }
    }


    // Criar usuário
    public UsuarioTO save(UsuarioTO usuario) {

        String sql = "INSERT INTO tb_usuario (nome, data_nascimento, email, senha) " +
                "VALUES (?, ?, ?, ?) RETURNING id INTO ?";

        try (Connection conn = ConnectionFactory.abrirConexao()) {

            oracle.jdbc.OraclePreparedStatement stmt =
                    (oracle.jdbc.OraclePreparedStatement) conn.prepareStatement(sql);

            stmt.setString(1, usuario.getNome());
            stmt.setDate(2, java.sql.Date.valueOf(usuario.getDataDeNascimento()));
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());

            stmt.registerReturnParameter(5, java.sql.Types.INTEGER);

            stmt.executeUpdate();

            ResultSet rs = stmt.getReturnResultSet();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }

            System.out.println("Usuário cadastrado com ID: " + usuario.getId());
            return usuario;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar usuário!");
            e.printStackTrace();
            return null;
        }
    }


    // Atualizar por id
    public UsuarioTO update(UsuarioTO usuario) {

        String sql = "UPDATE tb_usuario SET nome = ?, email = ?, senha = ?, data_nascimento = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setDate(4, Date.valueOf(usuario.getDataDeNascimento()));
            stmt.setInt(5, usuario.getId());

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Usuário atualizado: ID " + usuario.getId());
                return usuario;
            }

            System.out.println("Nenhum usuário encontrado com ID: " + usuario.getId());
            return null;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário!");
            e.printStackTrace();
            return null;
        }
    }


    // Remover
    public boolean delete(int id) {

        String sql = "DELETE FROM tb_usuario WHERE id = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Usuário removido: ID " + id);
                return true;
            }

            System.out.println("Usuário não encontrado: ID " + id);
            return false;

        } catch (SQLException e) {
            System.err.println("Erro ao remover usuário!");
            e.printStackTrace();
            return false;
        }
    }


    // Login
    public UsuarioTO login(String email, String senha) {

        String sql = "SELECT id, nome, email, senha, data_nascimento FROM tb_usuario WHERE email = ? AND senha = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new UsuarioTO(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getString("email"),
                        rs.getString("senha")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao realizar login!");
            e.printStackTrace();
        }

        return null;
    }
}