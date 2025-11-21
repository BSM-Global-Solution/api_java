package br.com.evolvere.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection abrirConexao() {
        Connection conexao = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = System.getenv("DB_URL");
            String usuario = System.getenv("DB_USER");
            String senha = System.getenv("DB_PASSWORD");

            if (url == null || usuario == null || senha == null) {
                throw new RuntimeException("Variáveis de ambiente não configuradas");
            }

            conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexão com banco estabelecida!");

        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver do Oracle não encontrado!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.err.println("Erro de SQL na conexão!");
            e.printStackTrace();

        } catch (Exception e) {
            System.err.println("Erro inesperado na conexão!");
            e.printStackTrace();
        }

        return conexao;
    }

    public static void fecharConexao(Connection conexao) {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("Conexão fechada!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão!");
            e.printStackTrace();
        }
    }
}
