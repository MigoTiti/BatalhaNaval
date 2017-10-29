package batalhanaval.telas;

import batalhanaval.BatalhaNavalMain;
import batalhanaval.enums.ComandosNet;
import batalhanaval.rede.Comunicacao;
import static batalhanaval.rede.Comunicacao.socket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ConectarTela {

    public void iniciarTela(String ip, String nickname) {
        Text texto = new Text("Tentando conexão com: " + ip);
        texto.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        ProgressIndicator pi = new ProgressIndicator();

        VBox vBoxCentro = new VBox(texto, pi);
        vBoxCentro.setSpacing(20);
        vBoxCentro.setAlignment(Pos.CENTER);
        BorderPane painel = new BorderPane(vBoxCentro);

        Button voltar = new Button("Voltar");
        voltar.setOnAction(event -> {
            BatalhaNavalMain.createScene();
            socket = null;
        });

        HBox hBoxBaixo = new HBox(voltar);
        hBoxBaixo.setPadding(new Insets(15));

        painel.setBottom(hBoxBaixo);

        BatalhaNavalMain.fxContainer.setScene(new Scene(painel));

        new Thread(() -> {
            System.out.println("Cliente");
            conectar(ip);
        }).start();
    }

    private static void conectar(String ip) {
        try {
            byte[] mensagem = ComandosNet.CONECTADO.getBytes();
            Comunicacao.ipAEnviar = InetAddress.getByName(ip);
            DatagramPacket pacoteAEnviar = new DatagramPacket(mensagem, mensagem.length, Comunicacao.ipAEnviar, Comunicacao.portaAEnviar);
            socket = new DatagramSocket();
            socket.send(pacoteAEnviar);
            
            byte[] buffer = new byte[500];
            DatagramPacket pacoteResposta = new DatagramPacket(buffer, buffer.length);
            socket.receive(pacoteResposta);

            String respostaString = new String(pacoteResposta.getData());

            System.out.println(respostaString);
            new PreparacaoTela().iniciarTela();
            
            Comunicacao.conexaoAberta = true;
            Comunicacao.persistirConexao();
        } catch (IOException ex) {
            Logger.getLogger(ConectarTela.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
