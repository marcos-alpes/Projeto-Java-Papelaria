package com.papelaria.sgc.dto;

public class RespostaDTO {

    private String mensagem;

    public RespostaDTO(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}