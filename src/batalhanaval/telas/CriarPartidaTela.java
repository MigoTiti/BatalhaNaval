package batalhanaval.telas;

import batalhanaval.enums.ComandosNet;
import batalhanaval.rede.Comunicacao;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.StringTokenizer;
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

    public void iniciarTela(String nickname) {
        Text texto = new Text("Aguardando oponente");
        texto.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        ProgressIndicator pi = new ProgressIndicator();

        VBox vBoxCentro = new VBox(texto, pi);
        vBoxCentro.setSpacing(20);
        vBoxCentro.setAlignment(Pos.CENTER);
        BorderPane painel = new BorderPane(vBoxCentro);

        Button voltar = new Button("Voltar");
        voltar.setOnAction(event -> {
            TelaInicial.createScene();
        });

        HBox hBoxBaixo = new HBox(voltar);
        hBoxBaixo.setPadding(new Insets(15));

        painel.setBottom(hBoxBaixo);

        TelaInicial.fxContainer.setScene(new Scene(painel));

        new Thread(() -> {
            System.out.println("Servidor");
            ouvirConexoes(nickname);
        }).start();
    }

    private void ouvirConexoes(String nickname) {
        try {
            Comunicacao comunicador = new Comunicacao(TelaInicial.PORTA_PADRAO);

            byte[] mensagemAReceber = new byte[500];
            DatagramPacket pacoteAReceber = new DatagramPacket(mensagemAReceber, mensagemAReceber.length);
            comunicador.getSocket().receive(pacoteAReceber);
            String mensagemRecebida = new String(pacoteAReceber.getData());

            StringTokenizer st = new StringTokenizer(mensagemRecebida, "&");
            String comando = st.nextToken();

            comunicador.setIpAEnviar(pacoteAReceber.getAddress());
            comunicador.setPortaAEnviar(pacoteAReceber.getPort());

            while (true) {
                if (comando.equals("1")) {
                    String nickAdversario = st.nextToken();
                    String resposta;

                    if (nickAdversario.equals(nickname)) {
                        resposta = ComandosNet.NOME_REPETIDO.comando + "&";

                        comunicador.enviarMensagem(resposta);
                        mensagemRecebida = comunicador.receberMensagem();

                        st = new StringTokenizer(mensagemRecebida, "&");
                        comando = st.nextToken();
                    } else {
                        resposta = ComandosNet.CONECTADO.comando + "&" + nickname + "&" + nickAdversario + "&";
                        comunicador.enviarMensagem(resposta);

                        new PreparacaoTela(comunicador).iniciarTela(nickname, nickAdversario);
                        comunicador.setVezDoUsuario(true);
                        comunicador.setConexaoAberta(true);
                        comunicador.persistirConexao();
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ConectarTela.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
