package br.com.evolvere.dao;

import br.com.evolvere.to.RecuperacaoSenhaTO;

import java.sql.*;
import java.util.Date;

public class RecuperacaoSenhaDAO {

    // Insere um novo registro de recuperação de senha
    public RecuperacaoSenhaTO inserir(String email, String token, int minutosExpiracao) throws SQLException {
        String sql = "INSERT INTO tb_recuperacao_senha (email, token, data_expiracao, utilizado) " +
                "VALUES (?, ?, ?, 0)";

        // data_expiracao = agora + X minutos
        long agoraMillis = System.currentTimeMillis();
        long expMillis = agoraMillis + (minutosExpiracao * 60L * 1000L);
        Timestamp expTimestamp = new Timestamp(expMillis);

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {

            stmt.setString(1, email);
            stmt.setString(2, token);
            stmt.setTimestamp(3, expTimestamp);

            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new SQLException("Nenhuma linha inserida em tb_recuperacao_senha");
            }

            // Recupera o ID gerado (IDENTITY em Oracle)
            Long idGerado = null;
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    // Em Oracle, getLong(1) funciona se o driver estiver configurado
                    idGerado = rs.getLong(1);
                }
            }

            RecuperacaoSenhaTO to = new RecuperacaoSenhaTO();
            to.setId(idGerado);
            to.setEmail(email);
            to.setToken(token);
            to.setDataExpiracao(new Date(expTimestamp.getTime()));
            to.setUtilizado(false);

            return to;
        }
    }

    // Busca token ativo por email+token
    public RecuperacaoSenhaTO buscarAtivoPorEmailEToken(String email, String token) throws SQLException {
        String sql = "SELECT id, email, token, data_expiracao, utilizado " +
                "FROM tb_recuperacao_senha " +
                "WHERE email = ? " +
                "  AND token = ? " +
                "  AND utilizado = 0";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, token);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("id");
                    String em = rs.getString("email");
                    String tk = rs.getString("token");
                    Timestamp tsExp = rs.getTimestamp("data_expiracao");
                    int utilizadoNum = rs.getInt("utilizado");

                    RecuperacaoSenhaTO to = new RecuperacaoSenhaTO();
                    to.setId(id);
                    to.setEmail(em);
                    to.setToken(tk);
                    to.setDataExpiracao(new Date(tsExp.getTime()));
                    to.setUtilizado(utilizadoNum == 1);

                    return to;
                }
            }
        }

        return null;
    }

    // Marca como utilizado
    public void marcarComoUtilizado(Long id) throws SQLException {
        String sql = "UPDATE tb_recuperacao_senha SET utilizado = 1 WHERE id = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}