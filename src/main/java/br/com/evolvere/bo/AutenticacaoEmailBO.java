package br.com.evolvere.bo;

import br.com.evolvere.dao.AutenticacaoEmailDAO;
import br.com.evolvere.to.AutenticacaoEmailTO;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Date;

public class AutenticacaoEmailBO {

    private final AutenticacaoEmailDAO dao;
    private static final SecureRandom random = new SecureRandom();

    public AutenticacaoEmailBO() {
        this.dao = new AutenticacaoEmailDAO();
    }

    // Gera um código numérico de 6 dígitos
    private String gerarCodigo() {
        int numero = 100000 + random.nextInt(900000);
        return String.valueOf(numero);
    }

    // Gera o código e grava no BD
    public AutenticacaoEmailTO gerarERegistrarCodigo(String email, String tipo, int minutosExpiracao) throws SQLException {
        String codigo = gerarCodigo();
        return dao.inserir(email, codigo, tipo, minutosExpiracao);
    }

    // Reenvia um novo código (basicamente gera outro e grava)
    public AutenticacaoEmailTO reenviarCodigo(String email, String tipo, int minutosExpiracao) throws SQLException {
        // Se quiser, aqui você poderia invalidar os códigos antigos não usados desse email/tipo
        // ex: dao.invalidarCodigosNaoUsados(email, tipo);
        return gerarERegistrarCodigo(email, tipo, minutosExpiracao);
    }

    // Valida o código (email + código + tipo)
    public AutenticacaoEmailTO validarCodigo(String email, String codigo, String tipo) throws Exception {
        AutenticacaoEmailTO to = dao.buscarAtivoPorEmailCodigoTipo(email, codigo, tipo);

        if (to == null) {
            throw new Exception("Código inválido ou não encontrado");
        }

        if (to.getDataExpiracao().before(new Date())) {
            throw new Exception("Código expirado");
        }

        if ("S".equalsIgnoreCase(to.getUsado())) {
            throw new Exception("Código já foi utilizado");
        }

        return to;
    }

    // Marca como usado
    public void marcarComoUsado(Long id) throws SQLException {
        dao.marcarComoUsado(id);
    }
}