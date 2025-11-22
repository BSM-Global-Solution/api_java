package br.com.evolvere.bo;

import br.com.evolvere.dao.RecuperacaoSenhaDAO;
import br.com.evolvere.dao.UsuarioDAO;
import br.com.evolvere.to.RecuperacaoSenhaTO;
import br.com.evolvere.to.UsuarioTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public class RecuperacaoSenhaBO {

    private final RecuperacaoSenhaDAO recDao;
    private final UsuarioDAO usuarioDAO;

    public RecuperacaoSenhaBO() {
        this.recDao = new RecuperacaoSenhaDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    // Gera token aleatório – pode ser UUID reduzido, por ex
    private String gerarToken() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Inicia o processo de recuperação de senha:
     * - Verifica se o e-mail existe
     * - Gera token
     * - Salva em tb_recuperacao_senha
     */
    public RecuperacaoSenhaTO solicitarRecuperacao(String email, int minutosExpiracao) throws Exception {
        if (!usuarioDAO.emailExists(email)) {
            throw new Exception("E-mail não encontrado.");
        }

        String token = gerarToken();

        try {
            return recDao.inserir(email, token, minutosExpiracao);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Erro ao registrar recuperação de senha.");
        }
    }

    /**
     * Valida o token e troca a senha do usuário
     */
    public void redefinirSenha(String email, String token, String novaSenha) throws Exception {
        RecuperacaoSenhaTO rec;
        try {
            rec = recDao.buscarAtivoPorEmailEToken(email, token);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Erro ao buscar token de recuperação.");
        }

        if (rec == null) {
            throw new Exception("Token inválido ou já utilizado.");
        }

        // Verifica expiração
        if (rec.getDataExpiracao().before(new Date())) {
            throw new Exception("Token expirado.");
        }

        // Busca o usuário pelo login
        UsuarioTO usuario = usuarioDAO.login(email, novaSenha); // isso aqui não faz sentido para busca
        // Melhor criar um método que busque por e-mail:
        usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario == null) {
            throw new Exception("Usuário não encontrado para o e-mail informado.");
        }

        // Atualiza a senha do usuário
        usuario.setSenha(novaSenha);
        usuarioDAO.update(usuario);

        // Marca o token como utilizado
        try {
            recDao.marcarComoUtilizado(rec.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            // Não dou throw aqui para não "desfazer" a troca da senha
        }
    }
}