package common;

import java.util.ArrayList;

public class Usuario {
    private String host;
    private ArrayList<Contato> contatos = new ArrayList<Contato>();

    public Usuario setHost(String host) {
        this.host = host;
        return this;
    }

    public String getHost() {
        return host;
    }

    public ArrayList<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(ArrayList<Contato> contatos) {
        this.contatos = contatos;
    }

    public void addContato(Contato contato) {
        this.contatos.add(contato);
    }
}