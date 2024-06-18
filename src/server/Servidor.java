package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.CargaDados;

public class Servidor {

    private ServerSocket servidor = null;
    private ExecutorService executor;
    private ObjectMapper objectMapper = new ObjectMapper();
    
    public final static Integer PORT = 7000;

    public Servidor() throws IOException {

        try {
            servidor = new ServerSocket(PORT);
            executor = Executors.newFixedThreadPool(10); // Pool de threads com 10 threads

            while (true) {
                final Socket conexao = servidor.accept();
                executor.submit(new ClienteHandler(conexao));
            }
        } catch (IOException e) {
            System.out.println("Algo errado aconteceu");
        } finally {
            if (servidor != null) {
                try {
                    servidor.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar o servidor");
                }
            }
            if (executor != null) {
                executor.shutdown();
            }
        }
    }

    private String call(CargaDados cargaDados) throws IOException {
        return objectMapper.writeValueAsString(Api.call(cargaDados));
    }

    public static void main(String[] args) {
        try {
            new Servidor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClienteHandler implements Runnable {
        private Socket conexao;

        public ClienteHandler(Socket conexao) {
            this.conexao = conexao;
        }

        @Override
        public void run() {
            try {
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(conexao.getInputStream()));
                PrintWriter saida = new PrintWriter(conexao.getOutputStream(), true);

                String texto;
                do {
                    texto = entrada.readLine();
                    if (texto != null) {
                        saida.println(call(objectMapper.readValue(texto, CargaDados.class)));
                    }
                } while (true);
            } catch (IOException e) {
                System.out.println("Erro na comunicação com o cliente");
            } finally {
                try {
                    if (conexao != null) {
                        conexao.close();
                    }
                } catch (IOException e) {
                    System.out.println("Erro ao fechar a conexão");
                }
            }
        }
    }
}
