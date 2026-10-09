package edu.semillerocicd.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RecuperacionForm {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Ingresa un correo válido")
    private String correo;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
