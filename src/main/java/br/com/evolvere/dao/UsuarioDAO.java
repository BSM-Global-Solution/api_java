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

    // Verificar se ID já existe
    public boolean idExists(int id) {
        String sql = "SELECT 1 FROM tb_usuario WHERE id = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Erro ao verificar ID!");
            e.printStackTrace();
            return false;
        }
    }

    // Criar usuário
    public UsuarioTO save(UsuarioTO usuario) {

        String sql = "INSERT INTO tb_usuario (id, nome, data_nascimento, email, senha) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 1. Gerar ID aleatório de 6 dígitos e garantir que é único
            int idGerado = gerarIdUnico(conn);
            usuario.setId(idGerado);

            // 2. Preencher parâmetros
            stmt.setInt(1, usuario.getId());
            stmt.setString(2, usuario.getNome());
            stmt.setDate(3, java.sql.Date.valueOf(usuario.getDataDeNascimento()));
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getSenha());

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Usuário cadastrado com ID: " + usuario.getId());
                return usuario;
            }

            System.err.println("Nenhum usuário inserido!");
            return null;

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

    // Buscar usuário por e-mail
    public UsuarioTO buscarPorEmail(String email) {
        String sql = "SELECT id, nome, email, senha, data_nascimento FROM tb_usuario WHERE email = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UsuarioTO(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getDate("data_nascimento").toLocalDate(),
                            rs.getString("email"),
                            rs.getString("senha")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por e-mail!");
            e.printStackTrace();
        }

        return null;
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

    // Gera um ID de 6 dígitos e garante que é único na tabela
    private int gerarIdUnico(Connection conn) throws SQLException {
        String sqlCheck = "SELECT 1 FROM tb_usuario WHERE id = ?";

        java.util.Random random = new java.util.Random();

        while (true) {
            // gera número entre 100000 e 999999
            int usuario = 100000 + random.nextInt(900000);

            try (PreparedStatement stmt = conn.prepareStatement(sqlCheck)) {
                stmt.setInt(1, usuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        return usuario;
                    }
                }
            }
        }
    }
}
