package com.fiap.embrace.embrace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class UsuarioLoginDTO {

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ter formato válido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    public UsuarioLoginDTO() {}

    public UsuarioLoginDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
