package batalhanaval.rede;

import batalhanaval.BatalhaNavalMain;
import batalhanaval.enums.ComandosNet;
import batalhanaval.telas.BatalhaTela;
import batalhanaval.telas.PreparacaoTela;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

public class Comunicacao {

    public static DatagramSocket socket = null;
    public static boolean conexaoAberta = false;
    public static InetAddress ipAEnviar = null;
    public static int portaAEnviar = batalhanaval.BatalhaNavalMain.PORTA_PADRAO;
    public static boolean vezDoUsuario = false;

    public static void persistirConexao() {
        while (conexaoAberta) {
            try {
                byte[] buffer = new byte[500];
                DatagramPacket pacoteResposta = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacoteResposta);
                String respostaString = new String(pacoteResposta.getData()).trim();

                StringTokenizer st = new StringTokenizer(respostaString, "&");
                String comando = st.nextToken();

                if (comando.equals(ComandosNet.DESCONECTAR.comando)) {
                    desconectar();
                    Platform.runLater(() -> {
                        BatalhaNavalMain.enviarMensagemErro("O outro jogador foi desconectado. Voltando ao menu principal.");
                        batalhanaval.BatalhaNavalMain.createScene();
                    });
                } else if (comando.equals(ComandosNet.PRONTO.comando)) {
                    PreparacaoTela.oponentePronto = true;
                } else if (comando.equals(ComandosNet.JOGADA.comando)) {
                    if (BatalhaTela.getInstance().isPronto()) {
                        int x = Integer.parseInt(st.nextToken());
                        int y = Integer.parseInt(st.nextToken());

                        if (BatalhaTela.campoUsuarioMatriz[x][y].isOcupado()) {
                            Platform.runLater(() -> {
                                BatalhaTela.campoUsuarioMatriz[x][y].setFill(BatalhaTela.COR_ACERTO);
                                BatalhaTela.getInstance().decrementarContagemUsuario();

                                if (BatalhaTela.getInstance().getContagemUsuario() == 0) {
                                    BatalhaNavalMain.enviarMensagemInfo("Você perdeu.");
                                } else {
                                    BatalhaNavalMain.enviarMensagemInfo("Sua vez");
                                }
                            });
                            enviarMensagem(ComandosNet.REPORTAR_JOGADA.comando + "&" + x + "&" + y + "&a");
                        } else {
                            Platform.runLater(() -> {
                                BatalhaTela.campoUsuarioMatriz[x][y].setFill(BatalhaTela.COR_ERRO);
                                BatalhaNavalMain.enviarMensagemInfo("Sua vez");
                            });
                            enviarMensagem(ComandosNet.REPORTAR_JOGADA.comando + "&" + x + "&" + y + "&e");
                        }

                        vezDoUsuario = true;
                    } else {
                        enviarMensagem(ComandosNet.NAO_PRONTO.comando);
                    }
                } else if (comando.equals(ComandosNet.REPORTAR_JOGADA.comando)) {
                    int x = Integer.parseInt(st.nextToken());
                    int y = Integer.parseInt(st.nextToken());

                    if (st.nextToken().equals("a")) {
                        Platform.runLater(() -> {
                            BatalhaTela.campoAdversarioMatriz[x][y].setFill(BatalhaTela.COR_ACERTO);
                            BatalhaTela.getInstance().decrementarContagemAdversario();

                            if (BatalhaTela.getInstance().getContagemAdversario() == 0) {
                                BatalhaNavalMain.enviarMensagemInfo("Você ganhou!");
                            }
                        });
                    } else {
                        Platform.runLater(() -> {
                            BatalhaTela.campoAdversarioMatriz[x][y].setFill(BatalhaTela.COR_ERRO);
                        });
                    }
                } else if (comando.equals(ComandosNet.NAO_PRONTO.comando)){
                    Platform.runLater(() -> {
                        BatalhaNavalMain.enviarMensagemErro("O outro jogador ainda não está pronto");
                    });
                    vezDoUsuario = true;
                } else {
                    System.out.println(comando);
                }
            } catch (IOException ex) {
                Logger.getLogger(Comunicacao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void desconectar() {
        if (socket != null)
            socket.close();
        socket = null;
        conexaoAberta = false;
        ipAEnviar = null;
        portaAEnviar = batalhanaval.BatalhaNavalMain.PORTA_PADRAO;
    }

    public static void enviarMensagem(String mensagemString) {
        try {
            byte[] mensagem = mensagemString.getBytes();
            DatagramPacket pacoteAEnviar = new DatagramPacket(mensagem, mensagem.length, Comunicacao.ipAEnviar, Comunicacao.portaAEnviar);
            socket.send(pacoteAEnviar);
        } catch (IOException ex) {
            Logger.getLogger(Comunicacao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
