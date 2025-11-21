package br.com.evolvere.to;

import java.time.LocalDate;

public class UsuarioTO {

    private int id;
    private String nome;
    private LocalDate dataDeNascimento;
    private String email;
    private String senha;

    public UsuarioTO() {}

    // Construtor com ID (usado quando vem do BD)
    public UsuarioTO(int id, String nome, LocalDate dataDeNascimento, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.email = email;
        this.senha = senha;
    }

    // Construtor sem ID (para criar antes de salvar)
    public UsuarioTO(String nome, LocalDate dataDeNascimento, String email, String senha) {
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.email = email;
        this.senha = senha;
    }

    // Getters e setters
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

    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
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
