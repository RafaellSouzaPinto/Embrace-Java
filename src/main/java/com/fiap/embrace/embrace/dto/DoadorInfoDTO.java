package com.fiap.embrace.embrace.dto;

public class DoadorInfoDTO {
    private String nome;
    private String email;
    private String telefone;
    private int quantidade;

    public DoadorInfoDTO() {}

    public DoadorInfoDTO(String nome, String email, String telefone, int quantidade) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
