package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicial {
    private JFrame frame;
    private JTextField campoHost;

    public TelaInicial() {
        initializeFrame();
        initializeUI();
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame("EAÍ - Tela Inicial");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridBagLayout());
    }

    private void initializeUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel rotuloHost = new JLabel("Seu Host:");
        campoHost = new JTextField(15);
        JButton botaoEntrar = new JButton("Entrar");

        botaoEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String host = campoHost.getText().trim();
                if (!host.isEmpty()) {
                    new AplicativoListaContatos(host);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Host não pode estar vazio", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(rotuloHost, gbc);

        gbc.gridx = 1;
        frame.add(campoHost, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(botaoEntrar, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaInicial::new);
    }
}