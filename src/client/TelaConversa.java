package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import common.Contato;
import common.Mensagem;

public class TelaConversa {
    private JFrame frame;
    private JTextArea areaHistorico;
    private JTextField campoMensagem;
    private Contato contato;
    public Cliente cliente;

    public TelaConversa(Contato contato, Cliente cliente) {
        this.cliente = cliente;
        cliente.telaConversa = this;
        this.contato = contato;
        initializeFrame();
        initializeUI();
        frame.setVisible(true);
        cliente.getMensagens(cliente.usuario.getHost(), contato.getHost());
    }

    private void initializeFrame() {
        frame = new JFrame("Conversa com " + contato.getName() + " - " + contato.getHost());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());
    }

    private void initializeUI() {
        // Painel superior com nome e host do contato e botão Voltar
        JPanel painelSuperior = new JPanel(new BorderLayout());
        JLabel rotuloContato = new JLabel(contato.getName() + " - " + contato.getHost(), SwingConstants.CENTER);
        JButton botaoVoltar = new JButton("Voltar");

        botaoVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AplicativoListaContatos(cliente.usuario.getHost());
                frame.dispose();
            }
        });

        painelSuperior.add(rotuloContato, BorderLayout.CENTER);
        painelSuperior.add(botaoVoltar, BorderLayout.WEST);
        frame.add(painelSuperior, BorderLayout.NORTH);

        // Área de histórico de mensagens
        areaHistorico = new JTextArea();
        areaHistorico.setEditable(false);
        JScrollPane scrollPaneHistorico = new JScrollPane(areaHistorico);
        frame.add(scrollPaneHistorico, BorderLayout.CENTER);

        // Painel inferior com campo de texto e botão de enviar
        JPanel painelInferior = new JPanel(new BorderLayout());
        campoMensagem = new JTextField();
        JButton botaoEnviar = new JButton("Enviar");

        botaoEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensagem();
            }
        });

        painelInferior.add(campoMensagem, BorderLayout.CENTER);
        painelInferior.add(botaoEnviar, BorderLayout.EAST);
        frame.add(painelInferior, BorderLayout.SOUTH);
    }

    private void enviarMensagem() {
        String mensagem = campoMensagem.getText().trim();
        if (!mensagem.isEmpty()) {
            Mensagem mensagemOBJ = new Mensagem();
            mensagemOBJ.setHostDestinatario(contato.getHost());
            mensagemOBJ.setHostRemetente(cliente.usuario.getHost());
            mensagemOBJ.setTexto(mensagem);
            cliente.enviarMensagem(mensagemOBJ);
            campoMensagem.setText("");
        }
    }

    public void adicionarMensagem(Mensagem mensagem) {
        String quem = mensagem.getHostDestinatario().equals(this.contato.getHost()) ? "Contato" : "Você";
        areaHistorico.append(quem + ": "+ mensagem.getTexto() + "\n");
    }
}
