package br.com.evolvere.to;

import java.util.Date;

public class UsuarioTO {
    // Atributos
    private String nome;
    private Date dataDeNascimento;
    private String email;
    private String senha;

    // Construtor vazio
    public UsuarioTO() {}

    // Construtor com parâmetros
    public UsuarioTO(String nome, Date dataDeNascimento, String email, String senha) {
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.email = email;
        this.senha = senha;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    // toString
    @Override
    public String toString() {
        return "UsuarioTO{" +
                "nome='" + nome + '\'' +
                ", dataDeNascimento=" + dataDeNascimento +
                ", email='" + email + '\'' +
                '}';
    }
}
