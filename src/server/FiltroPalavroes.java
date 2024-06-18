package server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FiltroPalavroes {
    private static final int NUMERO_THREADS = 4;
    private static final String CAMINHO_ARQUIVO_PALAVRAS = "src/palavras-moderacao.json";
    private static final List<String> palavrasInadequadas = new LinkedList<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        try {
            carregarPalavrasInadequadas();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void carregarPalavrasInadequadas() throws IOException {
        try (FileReader reader = new FileReader(CAMINHO_ARQUIVO_PALAVRAS)) {
            JsonNode jsonArray = objectMapper.readTree(reader);
            for (JsonNode node : jsonArray) {
                palavrasInadequadas.add(node.asText().toLowerCase());
            }
        }
    }

    public static List<String> encontrarPalavrasInapropriadas(String texto) {
        String[] palavras = texto.split("\\s+");
        ExecutorService executor = Executors.newFixedThreadPool(NUMERO_THREADS);
        List<Future<String>> futuros = new LinkedList<>();

        for (String palavra : palavras) {
            futuros.add(executor.submit(new VerificarPalavra(palavra)));
        }

        List<String> palavrasInapropriadas = new LinkedList<>();
        for (Future<String> futuro : futuros) {
            try {
                String resultado = futuro.get();
                if (resultado != null) {
                    palavrasInapropriadas.add(resultado);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return palavrasInapropriadas;
    }

    private static class VerificarPalavra implements Callable<String> {
        private final String palavra;

        VerificarPalavra(String palavra) {
            this.palavra = palavra.replaceAll("[,.;:?!…]", "").toLowerCase();
        }

        @Override
        public String call() {
            return palavrasInadequadas.contains(palavra) ? palavra : null;
        }
    }
}
