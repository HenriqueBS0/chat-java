package common;

import java.util.ArrayList;

public class Data {
    private ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
    private ArrayList<Mensagem> mensagens = new ArrayList<Mensagem>();

    public boolean existeUsuario(Usuario usuario) {
        return getUsuarioFromHost(usuario.getHost()) != null;
    }

    public Usuario getUsuarioFromHost(String host) {
        for (Usuario usuario : usuarios) {
            if (host.equals(usuario.getHost())) {
                return usuario;
            }
        }
        return null;
    }

    public void adicionaUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public void atualizarUsuario(Usuario usuarioAtualizar) {
        for (int index = 0; index < this.getUsuarios().size(); index++) {
            Usuario usuario = this.getUsuarios().get(index); 
            if (usuarioAtualizar.getHost().equals(usuario.getHost())) {
                this.getUsuarios().set(index, usuarioAtualizar);
                return;
            }
        }   
    }

    public ArrayList<Mensagem> getMensagensDoisUsuarios(String hostUm, String hostDois) {
        ArrayList<Mensagem> mensagensEntreUsuarios = new ArrayList<>();

        for (Mensagem mensagem : this.mensagens) {
            if ((mensagem.getHostDestinatario().equals(hostUm) && mensagem.getHostRemetente().equals(hostDois)) ||
                (mensagem.getHostRemetente().equals(hostUm) && mensagem.getHostDestinatario().equals(hostDois))) {
                mensagensEntreUsuarios.add(mensagem);
            }
        }

        return mensagensEntreUsuarios;
    }

    public void addMensagem(Mensagem mensagem) {
        this.mensagens.add(mensagem);
    }

    public ArrayList<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public ArrayList<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(ArrayList<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }
}
