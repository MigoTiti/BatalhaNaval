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

public class CriarPartidaTela {

    public void iniciarTela() {
        Text texto = new Text("Aguardando oponente");
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
            System.out.println("Servidor");
            ouvirConexoes();
        }).start();
    }

    private static void ouvirConexoes() {
        try {
            socket = new DatagramSocket(BatalhaNavalMain.PORTA_PADRAO);
            byte[] mensagemAReceber = new byte[500];
            DatagramPacket pacoteAReceber = new DatagramPacket(mensagemAReceber, mensagemAReceber.length);
            socket.receive(pacoteAReceber);
            String mensagemRecebida = new String(pacoteAReceber.getData());
            
            Comunicacao.ipAEnviar = pacoteAReceber.getAddress();
            Comunicacao.portaAEnviar = pacoteAReceber.getPort();
            
            System.out.println(mensagemRecebida);

            DatagramPacket pacoteResposta = new DatagramPacket(ComandosNet.CONECTADO.getBytes(), ComandosNet.CONECTADO.getBytes().length, Comunicacao.ipAEnviar, Comunicacao.portaAEnviar);
            socket.send(pacoteResposta);

            new PreparacaoTela().iniciarTela();
            Comunicacao.conexaoAberta = true;
            Comunicacao.persistirConexao();
        } catch (IOException ex) {
            Logger.getLogger(ConectarTela.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
