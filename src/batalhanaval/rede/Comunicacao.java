package batalhanaval.rede;

import batalhanaval.enums.ComandosNet;
import batalhanaval.telas.PreparacaoTela;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Comunicacao {

    public static DatagramSocket socket = null;
    public static boolean conexaoAberta = false;
    public static InetAddress ipAEnviar = null;
    public static int portaAEnviar = batalhanaval.BatalhaNavalMain.PORTA_PADRAO;
            
    public static void persistirConexao() {
        while (conexaoAberta) {
            try {
                byte[] buffer = new byte[500];
                DatagramPacket pacoteResposta = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacoteResposta);

                String respostaString = new String(pacoteResposta.getData()).trim();
                if (respostaString.equals(ComandosNet.DESCONECTAR.comando)) {
                    batalhanaval.BatalhaNavalMain.createScene();
                    desconectar();
                } else if (respostaString.equals(ComandosNet.PRONTO.comando)){
                    PreparacaoTela.oponentePronto = true;
                } else {
                    System.out.println(respostaString);
                }
            } catch (IOException ex) {
                Logger.getLogger(Comunicacao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void desconectar() {
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
