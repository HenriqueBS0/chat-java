package common;

public class Mensagem {
    private String hostRemetente;
    private String hostDestinatario;
    private String texto;

    public String getHostRemetente() {
        return hostRemetente;
    }

    public void setHostRemetente(String hostRemetente) {
        this.hostRemetente = hostRemetente;
    }

    public String getHostDestinatario() {
        return hostDestinatario;
    }

    public void setHostDestinatario(String hostDestinatario) {
        this.hostDestinatario = hostDestinatario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}