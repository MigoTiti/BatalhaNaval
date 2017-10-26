package batalhanaval;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
            
            applet.start();
        });
    }
    
    @Override
    public void init() {
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        add(fxContainer, BorderLayout.CENTER);
        // create JavaFX scene
        Platform.runLater(this::createScene);
    }
    
    private void createScene() {
        
        BorderPane root = new BorderPane();
        
        HBox hboxCentro = new HBox();
        hboxCentro.setPadding(new Insets(15, 12, 15, 12));
        hboxCentro.setSpacing(10);
        
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
        
        TextField ipServidorPrimeiroOcteto = new TextField();
        TextField ipServidorSegundoOcteto = new TextField();
        TextField ipServidorTerceiroOcteto = new TextField();
        TextField ipServidorQuartoOcteto = new TextField();
        
        int tamanhoCampo = 40;
        ipServidorPrimeiroOcteto.setPrefWidth(tamanhoCampo);
        ipServidorSegundoOcteto.setPrefWidth(tamanhoCampo);
        ipServidorTerceiroOcteto.setPrefWidth(tamanhoCampo);
        ipServidorQuartoOcteto.setPrefWidth(tamanhoCampo);

        HBox hboxTop = new HBox();
        hboxTop.setPadding(new Insets(15, 12, 15, 12));
        hboxTop.setSpacing(10);
        
        Text helpText6 = new Text("Nome de usuário: ");
        helpText6.setFill(Color.BLACK);
        helpText6.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        TextField nomeUsuario = new TextField();
        
        Button acharPartidaButton = new Button("Conectar");
        acharPartidaButton.setOnAction((ActionEvent) -> {
            nickName = nomeUsuario.getText();
            new BatalhaTela().iniciarTela(ipServidorPrimeiroOcteto.getText() + "." + ipServidorSegundoOcteto.getText() + "." + ipServidorTerceiroOcteto.getText() + "." + ipServidorQuartoOcteto.getText(), nickName);
        });
        
        hboxCentro.getChildren().addAll(helpText, ipServidorPrimeiroOcteto, helpText2, ipServidorSegundoOcteto, helpText3, ipServidorTerceiroOcteto, helpText4, ipServidorQuartoOcteto, acharPartidaButton);
        hboxTop.getChildren().addAll(helpText6, nomeUsuario);
        hboxTop.setAlignment(Pos.CENTER);
        hboxCentro.setAlignment(Pos.CENTER);
        root.setTop(hboxTop);
        root.setCenter(hboxCentro);
        
        fxContainer.setScene(new Scene(root));
    }
    
}
