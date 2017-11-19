package batalhanaval.rede;

import batalhanaval.telas.TelaInicial;
import batalhanaval.enums.ComandosNet;
import batalhanaval.telas.BatalhaTela;
import batalhanaval.telas.PreparacaoTela;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.StringTokenizer;
import javafx.application.Platform;

public class Comunicacao {

    private DatagramSocket socket;
    private boolean conexaoAberta;
    private InetAddress ipAEnviar;
    private int portaAEnviar;
    private boolean vezDoUsuario;

    public Comunicacao(int porta) {
        try {
            socket = new DatagramSocket(porta);
            conexaoAberta = false;
            ipAEnviar = null;
            portaAEnviar = batalhanaval.telas.TelaInicial.PORTA_PADRAO;
            vezDoUsuario = false;
        } catch (Exception ex) {
            Platform.runLater(() -> {
                TelaInicial.exibirException(ex);
            });
        }
    }

    public Comunicacao() {
        try {
            socket = new DatagramSocket();
            conexaoAberta = false;
            ipAEnviar = null;
            portaAEnviar = batalhanaval.telas.TelaInicial.PORTA_PADRAO;
            vezDoUsuario = false;
        } catch (Exception ex) {
            Platform.runLater(() -> {
                TelaInicial.exibirException(ex);
            });
        }
    }

    public void setIpAEnviar(InetAddress ip) {
        ipAEnviar = ip;
    }

    public void setPortaAEnviar(int porta) {
        portaAEnviar = porta;
    }

    public void setVezDoUsuario(boolean vez) {
        vezDoUsuario = vez;
    }

    public void setConexaoAberta(boolean conexao) {
        conexaoAberta = conexao;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public boolean isVezDoUsuario() {
        return vezDoUsuario;
    }

    public void persistirConexao() {
        while (conexaoAberta) {
            String respostaString = receberMensagem().trim();
            StringTokenizer st = new StringTokenizer(respostaString, "&");
            String comando = st.nextToken();
            if (comando.equals(ComandosNet.DESCONECTAR.comando)) {
                desconectar();
                enviarMensagem(ComandosNet.DESCONECTAR.comando + "&");
                Platform.runLater(() -> {
                    TelaInicial.createScene();
                });
            } else if (comando.equals(ComandosNet.PRONTO.comando)) {
                PreparacaoTela.oponentePronto = true;
            } else if (comando.equals(ComandosNet.JOGADA.comando)) {
                if (BatalhaTela.getInstance().isPronto()) {
                    int x = Integer.parseInt(st.nextToken());
                    int y = Integer.parseInt(st.nextToken());
                    
                    if (BatalhaTela.getInstance().getCampoUsuarioMatriz()[x][y].isOcupado()) {
                        Platform.runLater(() -> {
                            BatalhaTela.getInstance().getCampoUsuarioMatriz()[x][y].setFill(BatalhaTela.COR_ACERTO);
                            BatalhaTela.getInstance().decrementarContagemUsuario();
                            
                            if (BatalhaTela.getInstance().getContagemUsuario() == 0) {
                                TelaInicial.enviarMensagemInfo("Você perdeu.");
                            } else {
                                TelaInicial.enviarMensagemInfo("Sua vez");
                            }
                        });
                        enviarMensagem(ComandosNet.REPORTAR_JOGADA.comando + "&" + x + "&" + y + "&a");
                    } else {
                        Platform.runLater(() -> {
                            BatalhaTela.getInstance().getCampoUsuarioMatriz()[x][y].setFill(BatalhaTela.COR_ERRO);
                            TelaInicial.enviarMensagemInfo("Sua vez");
                        });
                        enviarMensagem(ComandosNet.REPORTAR_JOGADA.comando + "&" + x + "&" + y + "&e");
                    }

                    vezDoUsuario = true;
                } else {
                    enviarMensagem(ComandosNet.NAO_PRONTO.comando + "&");
                }
            } else if (comando.equals(ComandosNet.REPORTAR_JOGADA.comando)) {
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                
                if (st.nextToken().equals("a")) {
                    Platform.runLater(() -> {
                        BatalhaTela.getInstance().getCampoAdversarioMatriz()[x][y].setFill(BatalhaTela.COR_ACERTO);
                        BatalhaTela.getInstance().decrementarContagemAdversario();
                        
                        if (BatalhaTela.getInstance().getContagemAdversario() == 0) {
                            TelaInicial.enviarMensagemInfo("Você ganhou!");
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        BatalhaTela.getInstance().getCampoAdversarioMatriz()[x][y].setFill(BatalhaTela.COR_ERRO);
                    });
                }
            } else if (comando.equals(ComandosNet.NAO_PRONTO.comando)) {
                Platform.runLater(() -> {
                    TelaInicial.enviarMensagemErro("O outro jogador ainda não está pronto");
                });
                vezDoUsuario = true;
            } else {
                System.out.println(comando);
            }
        }
        
        socket.close();
        socket = null;
    }

    public String receberMensagem() {
        try {
            byte[] buffer = new byte[500];
            DatagramPacket pacoteResposta = new DatagramPacket(buffer, buffer.length);
            socket.receive(pacoteResposta);
            String respostaString = new String(pacoteResposta.getData());

            return respostaString;
        } catch (Exception ex) {
            Platform.runLater(() -> {
                TelaInicial.exibirException(ex);
            });
        }

        return null;
    }

    public void enviarMensagem(String mensagemString) {
        try {
            byte[] mensagem = mensagemString.getBytes();
            DatagramPacket pacoteAEnviar = new DatagramPacket(mensagem, mensagem.length, ipAEnviar, portaAEnviar);
            socket.send(pacoteAEnviar);
        } catch (Exception ex) {
            Platform.runLater(() -> {
                TelaInicial.exibirException(ex);
            });
        }
    }

    public void desconectar() {
        conexaoAberta = false;
    }
}
