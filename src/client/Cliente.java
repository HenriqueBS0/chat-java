package client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.CargaDados;
import common.Contato;
import common.Mensagem;
import common.Usuario;
import server.Servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {

    public Usuario usuario = new Usuario();

    public AplicativoListaContatos aplicativoListaContatos;
    public TelaConversa telaConversa;

    private Socket cliente = null;
    private PrintStream saida = null;
    private Scanner entradaServidor = null;
    private ObjectMapper objectMapper = new ObjectMapper();

    public Cliente() {
        try {
            cliente = new Socket("127.0.0.1", Servidor.PORT);
            saida = new PrintStream(cliente.getOutputStream());
            entradaServidor = new Scanner(cliente.getInputStream());

            // Thread para ler continuamente do servidor
            new Thread(() -> {
                while (entradaServidor.hasNextLine()) {
                    String linha = entradaServidor.nextLine();
                    try {
                        call(objectMapper.readValue(linha, CargaDados.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            System.out.println("Algo errado aconteceu");
            e.printStackTrace();
        }
    }

    // Método que será chamado ao receber uma linha do servidor
    private void call(CargaDados cargaDados) {
        switch (cargaDados.getMetodo()) {
            case "loginResposta":
                loginResposta(cargaDados);
                break;
            case "atualizarUsuarioResposta":
                atualizarUsuarioResposta(cargaDados);
                break;
            case "enviarMensagemResposta":
                enviarMensagemResposta(cargaDados);
                break;
            case "getMensagensResposta":
                getMensagensResposta(cargaDados);
                break;
        }
    }

    // Método para enviar uma string ao servidor
    public void send(CargaDados cargaDados) {
        if (saida != null) {
            try {
                saida.println(objectMapper.writeValueAsString(cargaDados));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Saída não está disponível.");
        }
    }

    public void login(String host) {
        usuario.setHost(host);
        CargaDados cargaDados = new CargaDados();
        cargaDados.setMetodo("login");
        cargaDados.setDados(usuario);
        send(cargaDados);
    }

    public void atualizarUsuario() {
        CargaDados cargaDados = new CargaDados();
        cargaDados.setMetodo("atualizarUsuario");
        cargaDados.setDados(usuario);
        send(cargaDados);
    }

    public void enviarMensagem(Mensagem mensagem) {
        CargaDados cargaDados = new CargaDados();
        cargaDados.setMetodo("enviarMensagem");
        cargaDados.setDados(mensagem);
        send(cargaDados);
    }

    public void getMensagens(String hostRemetente, String hostDestinatario) {
        Mensagem mensagem = new Mensagem();
        mensagem.setHostRemetente(hostRemetente);
        mensagem.setHostDestinatario(hostDestinatario);
        CargaDados cargaDados = new CargaDados();
        cargaDados.setMetodo("getMensagens");
        cargaDados.setDados(mensagem);
        send(cargaDados);
    }

    public void loginResposta(CargaDados cargaDados) {
        try {
            this.usuario = objectMapper.readValue(objectMapper.writeValueAsString(cargaDados.getDados()), Usuario.class);

            for (Contato contato : this.usuario.getContatos()) {
                this.aplicativoListaContatos.adicionarContato(contato);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.usuario);
    }

    public void atualizarUsuarioResposta(CargaDados cargaDados) {
        try {
            this.usuario = objectMapper.readValue(objectMapper.writeValueAsString(cargaDados.getDados()), Usuario.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.usuario);
    }

    public void enviarMensagemResposta(CargaDados cargaDados) {
        try {
            if(cargaDados.getDados() == null) {
                return;
            }

            Mensagem mensagem = objectMapper.readValue(objectMapper.writeValueAsString(cargaDados.getDados()), Mensagem.class);
            this.telaConversa.adicionarMensagem(mensagem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getMensagensResposta(CargaDados cargaDados) {
        try {

        ArrayList<Mensagem> mensagens = objectMapper.readValue(
            objectMapper.writeValueAsString(cargaDados.getDados()), 
            new TypeReference<ArrayList<Mensagem>>() {}
        );

        for (Mensagem mensagem : mensagens) {
            this.telaConversa.adicionarMensagem(mensagem);
        }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
