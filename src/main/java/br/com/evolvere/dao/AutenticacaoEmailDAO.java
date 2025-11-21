package br.com.evolvere.dao;

import br.com.evolvere.to.AutenticacaoEmailTO;

import java.sql.*;
import java.util.Date;

public class AutenticacaoEmailDAO {

    // Insere um novo registro de autenticação e retorna o TO preenchido
    public AutenticacaoEmailTO inserir(String email, String codigo, String tipo, int minutosExpiracao) throws SQLException {

        String sql = """
            INSERT INTO tb_autenticacao_email
                (EMAIL, CODIGO, TIPO, USADO, DATA_CRIACAO, DATA_EXPIRACAO)
            VALUES
                (?, ?, ?, 'N', SYSDATE, ?)
            """;

        try (Connection conn = ConnectionFactory.abrirConexao()) {

            // Para retornar o ID gerado
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID"});

            ps.setString(1, email);
            ps.setString(2, codigo);
            ps.setString(3, tipo);

            long agora = System.currentTimeMillis();
            long expMillis = agora + (long) minutosExpiracao * 60 * 1000;
            Timestamp dataExp = new Timestamp(expMillis);
            ps.setTimestamp(4, dataExp);

            ps.executeUpdate();

            AutenticacaoEmailTO to = new AutenticacaoEmailTO();
            to.setEmail(email);
            to.setCodigo(codigo);
            to.setTipo(tipo);
            to.setUsado("N");
            to.setDataCriacao(new Date(agora));
            to.setDataExpiracao(new Date(expMillis));

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    to.setId(rs.getLong(1));
                }
            }

            System.out.println("Registro de autenticação criado. ID = " + to.getId());
            return to;
        }
    }

    // Busca um registro ativo (não usado) pelo email + código + tipo
    public AutenticacaoEmailTO buscarAtivoPorEmailCodigoTipo(String email, String codigo, String tipo) throws SQLException {

        String sql = """
            SELECT ID, EMAIL, CODIGO, TIPO, USADO, DATA_CRIACAO, DATA_EXPIRACAO
            FROM tb_autenticacao_email
            WHERE EMAIL = ? AND CODIGO = ? AND TIPO = ? AND USADO = 'N'
            """;

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, codigo);
            ps.setString(3, tipo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AutenticacaoEmailTO to = new AutenticacaoEmailTO();
                    to.setId(rs.getLong("ID"));
                    to.setEmail(rs.getString("EMAIL"));
                    to.setCodigo(rs.getString("CODIGO"));
                    to.setTipo(rs.getString("TIPO"));
                    to.setUsado(rs.getString("USADO"));
                    to.setDataCriacao(rs.getTimestamp("DATA_CRIACAO"));
                    to.setDataExpiracao(rs.getTimestamp("DATA_EXPIRACAO"));
                    return to;
                }
            }
        }

        return null;
    }

    // Marca o registro como usado
    public void marcarComoUsado(Long id) throws SQLException {

        String sql = "UPDATE tb_autenticacao_email SET USADO = 'S' WHERE ID = ?";

        try (Connection conn = ConnectionFactory.abrirConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int linhas = ps.executeUpdate();

            System.out.println("Registros de autenticação marcados como usados: " + linhas);
        }
    }
}
