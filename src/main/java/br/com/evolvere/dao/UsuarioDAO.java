package br.com.evolvere.dao;

import br.com.evolvere.to.UsuarioTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // LISTAR TODOS
    public List<UsuarioTO> findAll() {
        List<UsuarioTO> lista = new ArrayList<>();

        String sql = "SELECT nome, email, senha, data_nascimento FROM PACIENTE";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UsuarioTO u = new UsuarioTO(
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

    // SALVAR
    public UsuarioTO save(UsuarioTO usuario) {

        String sql = "INSERT INTO PACIENTE (nome, email, senha, data_nascimento) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setDate(4, new java.sql.Date(usuario.getDataDeNascimento().getTime()));

            stmt.executeUpdate();
            System.out.println("Usuário cadastrado: " + usuario.getNome());

            return usuario;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ATUALIZAR (baseado no email — chave lógica)
    public UsuarioTO update(UsuarioTO usuario) {

        String sql = "UPDATE PACIENTE SET nome = ?, senha = ?, data_nascimento = ? WHERE email = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getSenha());
            stmt.setDate(3, new java.sql.Date(usuario.getDataDeNascimento().getTime()));
            stmt.setString(4, usuario.getEmail());

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Usuário atualizado: " + usuario.getEmail());
                return usuario;
            }

            System.out.println("Nenhum usuário encontrado com email: " + usuario.getEmail());
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // DELETAR (por email)
    public boolean delete(String email) {

        String sql = "DELETE FROM PACIENTE WHERE email = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Usuário removido: " + email);
                return true;
            }

            System.out.println("Usuário não encontrado para remoção: " + email);
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // LOGIN
    public UsuarioTO login(String email, String senha) {

        String sql = "SELECT nome, email, senha, data_nascimento FROM PACIENTE WHERE email = ? AND senha = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UsuarioTO usuario = new UsuarioTO(
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
