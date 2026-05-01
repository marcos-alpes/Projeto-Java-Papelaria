package com.papelaria.sgc.dto;

import java.time.LocalDateTime;

public class RespostaDTO {

    private String mensagem;
    private String token;
    private LocalDateTime expiraEm;

    public RespostaDTO(String mensagem) {
        this.mensagem = mensagem;
    }

    public RespostaDTO(String mensagem, String token, LocalDateTime expiraEm) {
        this.mensagem = mensagem;
        this.token = token;
        this.expiraEm = expiraEm;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiraEm() {
        return expiraEm;
    }

    public void setExpiraEm(LocalDateTime expiraEm) {
        this.expiraEm = expiraEm;
    }
}
