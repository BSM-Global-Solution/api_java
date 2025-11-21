package br.com.evolvere.to;

import java.util.Date;

public class UsuarioTO {
    // Atributos
    private int id;
    private String nome;
    private Date dataDeNascimento;
    private String email;
    private String senha;

    // Construtores
    public UsuarioTO() {}

    // Construtor usado quando os dados já vêm do banco (com ID)
    public UsuarioTO(int id, String nome, Date dataDeNascimento, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.email = email;
        this.senha = senha;
    }

    // Construtor usado para criar o usuário sem ID (antes de salvar)
    public UsuarioTO(String nome, Date dataDeNascimento, String email, String senha) {
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.email = email;
        this.senha = senha;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataDeNascimento=" + dataDeNascimento +
                ", email='" + email + '\'' +
                '}';
    }
}
