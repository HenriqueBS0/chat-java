package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import common.Contato;

public class AplicativoListaContatos {
    private JFrame frame;
    private DefaultListModel<Contato> modeloListaContatos;
    private JList<Contato> listaContatos;

    private Cliente cliente;

    public AplicativoListaContatos(String userHost) {
        cliente = new Cliente();
        cliente.aplicativoListaContatos = this;
        inicializarFrame();
        inicializarListaContatos();
        inicializarPainelSuperior();
        inicializarScrollPane();
        frame.setVisible(true);
        cliente.login(userHost);
    }

    private void inicializarFrame() {
        frame = new JFrame("EAÍ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setLayout(new BorderLayout());
    }

    private void inicializarPainelSuperior() {
        JPanel painelSuperior = new JPanel(new BorderLayout());
        JLabel rotuloTitulo = new JLabel("Contatos", SwingConstants.CENTER);
        JButton botaoAdicionar = criarBotaoAdicionar();

        painelSuperior.add(rotuloTitulo, BorderLayout.CENTER);
        painelSuperior.add(botaoAdicionar, BorderLayout.EAST);
        frame.add(painelSuperior, BorderLayout.NORTH);
    }

    private JButton criarBotaoAdicionar() {
        JButton botaoAdicionar = new JButton("add");
        botaoAdicionar.addActionListener(e -> abrirDialogoAdicionarContato());
        return botaoAdicionar;
    }

    private void inicializarListaContatos() {
        modeloListaContatos = new DefaultListModel<>();
        listaContatos = new JList<>(modeloListaContatos);
        listaContatos.setFont(new Font("Arial", Font.PLAIN, 16));
        listaContatos.setFixedCellHeight(30);
        listaContatos.setCellRenderer(new RenderizadorContato());
        listaContatos.addMouseListener(criarMouseListenerListaContatos());
    }

    private MouseAdapter criarMouseListenerListaContatos() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int indice = listaContatos.locationToIndex(e.getPoint());
                if (indice != -1) {
                    Contato usuarioSelecionado = modeloListaContatos.getElementAt(indice);
                    System.out.println(usuarioSelecionado.getName() + " - " + usuarioSelecionado.getHost());
                    new TelaConversa(usuarioSelecionado, cliente);
                    frame.dispose();
                }
            }
        };
    }

    private void inicializarScrollPane() {
        JScrollPane scrollPane = new JScrollPane(listaContatos);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void abrirDialogoAdicionarContato() {
        JDialog dialogo = new JDialog(frame, "Adicionar Contato", true);
        dialogo.setSize(300, 200);
        dialogo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = criarGridBagConstraints();

        JLabel rotuloNome = new JLabel("Nome:");
        JTextField campoNome = new JTextField(15);
        JLabel rotuloHost = new JLabel("Host:");
        JTextField campoHost = new JTextField(15);

        definirTamanhoPreferido(campoNome, campoHost);

        JButton botaoAdicionar = criarBotaoAdicionarContato(dialogo, campoNome, campoHost);
        JButton botaoCancelar = criarBotaoCancelar(dialogo);

        adicionarComponentesAoDialogo(dialogo, gbc, rotuloNome, campoNome, rotuloHost, campoHost, botaoAdicionar, botaoCancelar);

        dialogo.setLocationRelativeTo(frame);
        dialogo.setVisible(true);
    }

    private GridBagConstraints criarGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private void definirTamanhoPreferido(JTextField campoNome, JTextField campoHost) {
        campoNome.setPreferredSize(new Dimension(200, 24));
        campoHost.setPreferredSize(new Dimension(200, 24));
    }

    private JButton criarBotaoAdicionarContato(JDialog dialogo, JTextField campoNome, JTextField campoHost) {
        JButton botaoAdicionar = new JButton("Adicionar");
        botaoAdicionar.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            String host = campoHost.getText().trim();
            if (validarCampos(dialogo, nome, host)) {
                Contato contato = new Contato();
                contato.setHost(host);
                contato.setName(nome);
                adicionarContato(contato);
                this.cliente.usuario.addContato(contato);
                this.cliente.atualizarUsuario();
                dialogo.dispose();
            }
        });
        return botaoAdicionar;
    }

    private boolean validarCampos(JDialog dialogo, String nome, String host) {
        if (nome.isEmpty() || host.isEmpty()) {
            JOptionPane.showMessageDialog(dialogo, "Nome e Host não podem estar vazios", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private JButton criarBotaoCancelar(JDialog dialogo) {
        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(e -> dialogo.dispose());
        return botaoCancelar;
    }

    private void adicionarComponentesAoDialogo(JDialog dialogo, GridBagConstraints gbc, JLabel rotuloNome, JTextField campoNome, JLabel rotuloHost, JTextField campoHost, JButton botaoAdicionar, JButton botaoCancelar) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialogo.add(rotuloNome, gbc);

        gbc.gridx = 1;
        dialogo.add(campoNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialogo.add(rotuloHost, gbc);

        gbc.gridx = 1;
        dialogo.add(campoHost, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(botaoAdicionar);
        painelBotoes.add(botaoCancelar);
        dialogo.add(painelBotoes, gbc);
    }

    public void adicionarContato(Contato contato) {
        modeloListaContatos.addElement(contato);
    }

    private static class RenderizadorContato extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Contato) {
                Contato contato = (Contato) value;
                label.setText(contato.getName() + " - " + contato.getHost());
            }
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            return label;
        }
    }
}
