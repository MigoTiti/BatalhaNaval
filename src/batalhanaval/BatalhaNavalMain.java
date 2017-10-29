package batalhanaval;

import batalhanaval.telas.BatalhaTela;
import batalhanaval.telas.ConectarTela;
import batalhanaval.telas.CriarPartidaTela;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class BatalhaNavalMain extends JApplet {

    private static final int JFXPANEL_WIDTH_INT = 1000;
    private static final int JFXPANEL_HEIGHT_INT = 700;
    public static final int PORTA_PADRAO = 12345;
    public static JFXPanel fxContainer;
    public static String nickName;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            }

            JFrame frame = new JFrame("BATALHA DOS BARCO");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JApplet applet = new BatalhaNavalMain();
            applet.init();

            frame.setContentPane(applet.getContentPane());

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setResizable(false);

            applet.start();
        });
    }

    @Override
    public void init() {
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        add(fxContainer, BorderLayout.CENTER);

        Platform.runLater(BatalhaNavalMain::createScene);
    }

    public static void createScene() {
        BorderPane root = new BorderPane();
        VBox vBoxCentro = new VBox();

        HBox hboxCentro2 = new HBox(10);
        hboxCentro2.setPadding(new Insets(15, 12, 15, 12));

        Text helpText = new Text("Endereço IP do servidor: ");
        helpText.setFill(Color.BLACK);
        helpText.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        Text helpText2 = new Text(".");
        helpText2.setFill(Color.BLACK);
        helpText2.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        Text helpText3 = new Text(".");
        helpText3.setFill(Color.BLACK);
        helpText3.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        Text helpText4 = new Text(".");
        helpText4.setFill(Color.BLACK);
        helpText4.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        TextField ipServidorPrimeiroOcteto = new TextField("127");
        TextField ipServidorSegundoOcteto = new TextField("0");
        TextField ipServidorTerceiroOcteto = new TextField("0");
        TextField ipServidorQuartoOcteto = new TextField("1");

        int tamanhoCampo = 40;
        ipServidorPrimeiroOcteto.setPrefWidth(tamanhoCampo);
        ipServidorSegundoOcteto.setPrefWidth(tamanhoCampo);
        ipServidorTerceiroOcteto.setPrefWidth(tamanhoCampo);
        ipServidorQuartoOcteto.setPrefWidth(tamanhoCampo);

        HBox hboxCentro1 = new HBox(10);
        hboxCentro1.setPadding(new Insets(15, 12, 15, 12));

        Text helpText6 = new Text("Nome de usuário: ");
        helpText6.setFill(Color.BLACK);
        helpText6.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        TextField nomeUsuario = new TextField("MigoTiti");

        Button acharPartidaButton = new Button("Conectar");
        acharPartidaButton.setOnAction((ActionEvent) -> {
            if (nomeUsuario.getText() != null && !nomeUsuario.getText().equals("")) {
                nickName = nomeUsuario.getText();
                new ConectarTela().iniciarTela(ipServidorPrimeiroOcteto.getText() + "." + ipServidorSegundoOcteto.getText() + "." + ipServidorTerceiroOcteto.getText() + "." + ipServidorQuartoOcteto.getText(), nickName);
            } else {
                enviarMensagemErro("Digite um nome de usuário");
            }
        });

        hboxCentro2.getChildren().addAll(helpText, ipServidorPrimeiroOcteto, helpText2, ipServidorSegundoOcteto, helpText3, ipServidorTerceiroOcteto, helpText4, ipServidorQuartoOcteto, acharPartidaButton);
        hboxCentro1.getChildren().addAll(helpText6, nomeUsuario);
        hboxCentro1.setAlignment(Pos.CENTER);
        hboxCentro2.setAlignment(Pos.CENTER);

        HBox hboxCentro3 = new HBox(10);
        hboxCentro3.setPadding(new Insets(15, 12, 15, 12));
        hboxCentro3.setAlignment(Pos.CENTER);

        Button criarPartidaButton = new Button("Criar partida");
        criarPartidaButton.setOnAction((event -> {
            if (nomeUsuario.getText() != null && !nomeUsuario.getText().equals("")) {
                nickName = nomeUsuario.getText();
                //new BatalhaTela().iniciarTela();
                new CriarPartidaTela().iniciarTela();
            } else {
                enviarMensagemErro("Digite um nome de usuário");
            }
        }));

        hboxCentro3.getChildren().addAll(criarPartidaButton);

        vBoxCentro.setAlignment(Pos.CENTER);
        vBoxCentro.getChildren().addAll(hboxCentro1, hboxCentro2, hboxCentro3);

        root.setCenter(vBoxCentro);

        fxContainer.setScene(new Scene(root));
    }

    public static void enviarMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
