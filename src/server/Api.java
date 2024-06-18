package server;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.CargaDados;
import common.Data;
import common.Mensagem;
import common.Usuario;

public class Api {

    private static final String CAMINHO_ARQUIVO = "src/data.json";

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Data data;

    static {
        try {
            carregarData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void carregarData() throws IOException {
        FileReader reader = new FileReader(CAMINHO_ARQUIVO);
        data = objectMapper.readValue(reader, Data.class);
        if(data == null) {
            data = new Data();
        }
        reader.close();
    }

    private static void salvarData() throws IOException {
        FileWriter writer = new FileWriter(CAMINHO_ARQUIVO);
        objectMapper.writeValue(writer, data);
    }

    static CargaDados call(CargaDados cargaDados) throws IOException {
        switch (cargaDados.getMetodo()) {
            case "login":
                return login(cargaDados);
            case "atualizarUsuario":
                return atualizarUsuario(cargaDados);
            case "enviarMensagem":
                return enviarMensagem(cargaDados);
            case "getMensagens":
                return getMensagens(cargaDados);
            default:
                CargaDados cargaDadosResposta = new CargaDados();
                cargaDadosResposta.setMetodo("metodo-desconhecido");
                return cargaDadosResposta;
        }
    }

    static CargaDados login(CargaDados cargaDados) throws IOException {
        CargaDados cargaDadosResposta = new CargaDados();
        cargaDadosResposta.setMetodo("loginResposta");

        Usuario usuario = objectMapper.readValue(objectMapper.writeValueAsString(cargaDados.getDados()), Usuario.class);

        if (!data.existeUsuario(usuario)) {
            data.adicionaUsuario(usuario);
            salvarData();
        }

        cargaDadosResposta.setDados(data.getUsuarioFromHost(usuario.getHost()));
        return cargaDadosResposta;
    }

    static CargaDados atualizarUsuario(CargaDados cargaDados) throws IOException {
        CargaDados cargaDadosResposta = new CargaDados();
        cargaDadosResposta.setMetodo("atualizarUsuarioResposta");

        Usuario usuario = objectMapper.readValue(objectMapper.writeValueAsString(cargaDados.getDados()), Usuario.class);

        data.atualizarUsuario(usuario);

        salvarData();

        cargaDadosResposta.setDados(usuario);
        return cargaDadosResposta;
    }

    static CargaDados enviarMensagem(CargaDados cargaDados) throws IOException {
        CargaDados cargaDadosResposta = new CargaDados();
        cargaDadosResposta.setMetodo("enviarMensagemResposta");

        Mensagem mensagem = objectMapper.readValue(objectMapper.writeValueAsString(cargaDados.getDados()), Mensagem.class);
        
        if(FiltroPalavroes.encontrarPalavrasInapropriadas(mensagem.getTexto()).size() > 0) {
            return cargaDadosResposta;
        }

        data.addMensagem(mensagem);

        salvarData();

        cargaDadosResposta.setDados(mensagem);
        return cargaDadosResposta;
    }

    static CargaDados getMensagens(CargaDados cargaDados) throws IOException {
        CargaDados cargaDadosResposta = new CargaDados();
        cargaDadosResposta.setMetodo("getMensagensResposta");

        Mensagem mensagem = objectMapper.readValue(objectMapper.writeValueAsString(cargaDados.getDados()), Mensagem.class);

        cargaDadosResposta.setDados(data.getMensagensDoisUsuarios(mensagem.getHostDestinatario(), mensagem.getHostRemetente()));
        return cargaDadosResposta;
    }
}
