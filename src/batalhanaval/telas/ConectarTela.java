package batalhanaval.telas;

import batalhanaval.enums.ComandosNet;
import batalhanaval.rede.Comunicacao;
import java.net.InetAddress;
import java.util.Optional;
import java.util.StringTokenizer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ConectarTela {

    private Comunicacao comunicador;
    
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
            TelaInicial.createScene();
            if (comunicador != null)
                comunicador.desconectar();
        });

        HBox hBoxBaixo = new HBox(voltar);
        hBoxBaixo.setPadding(new Insets(15));

        painel.setBottom(hBoxBaixo);
        
        TelaInicial.fxContainer.setScene(new Scene(painel));

        new Thread(() -> {
            System.out.println("Cliente");
            conectar(ip, nickname);
        }).start();
    }

    private void conectar(String ip, String nickname) {
        try {
            comunicador = new Comunicacao();
            comunicador.setIpAEnviar(InetAddress.getByName(ip));

            String mensagemString = ComandosNet.SOLICITAR_CONEXAO.comando + "&" + nickname + "&";
            comunicador.enviarMensagem(mensagemString);

            while (true) {
                String respostaString = comunicador.receberMensagem();

                StringTokenizer st = new StringTokenizer(respostaString, "&");
                String comando = st.nextToken();

                if (comando.equals(ComandosNet.RESPOSTA_SOLICITACAO_CONEXAO.comando)) {
                    String nickAdversario = st.nextToken();
                    nickname = st.nextToken();
                    
                    new PreparacaoTela(comunicador).iniciarTela(nickname, nickAdversario);

                    comunicador.setConexaoAberta(true);
                    comunicador.persistirConexao();
                    break;
                } else if (comando.equals(ComandosNet.NOME_REPETIDO.comando)) {
                    Platform.runLater(() -> {
                        while (true) {
                            TextInputDialog dialog = new TextInputDialog("");
                            dialog.setTitle("Nome incorreto");
                            dialog.setHeaderText("O apelido escolhido já está sendo usado");
                            dialog.setContentText("Escolha outro apelido: ");
                            Optional<String> result = dialog.showAndWait();
                            if (result.isPresent()) {
                                comunicador.enviarMensagem(ComandosNet.SOLICITAR_CONEXAO.comando 
                                        + "&" + result.get() + "&");
                                break;
                            }
                        }
                    });
                }
            }
                
        } catch (Exception ex) {
            Platform.runLater(() -> {
                TelaInicial.exibirException(ex);
            });
        }
    }
}
