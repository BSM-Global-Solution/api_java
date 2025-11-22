package br.com.evolvere.to;

import java.util.Date;

public class RecuperacaoSenhaTO {
    // Atributos
    private Long id;
    private String email;
    private String token;
    private Date dataExpiracao;
    private boolean utilizado;

    // Construtores
    public RecuperacaoSenhaTO() {
    }

    public RecuperacaoSenhaTO(Long id, String email, String token, Date dataExpiracao, boolean utilizado) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.dataExpiracao = dataExpiracao;
        this.utilizado = utilizado;
    }

    // get/set

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Date dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public boolean isUtilizado() {
        return utilizado;
    }

    public void setUtilizado(boolean utilizado) {
        this.utilizado = utilizado;
    }
}
