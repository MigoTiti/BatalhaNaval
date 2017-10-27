package batalhanaval.telas;

import batalhanaval.BatalhaNavalMain;
import batalhanaval.tabuleiros.TabuleiroPronto;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class BatalhaTela extends TabuleiroPronto{
    
    public void iniciarTela(String ip, String nomeUsuario) {
        BorderPane root = new BorderPane();
        
        HBox hboxCentro = new HBox();
        
        VBox vboxUsuario = new VBox();
        VBox vboxAdversario = new VBox();
        
        vboxUsuario.setSpacing(20);
        vboxAdversario.setSpacing(20);
        
        vboxUsuario.setAlignment(Pos.CENTER);
        vboxAdversario.setAlignment(Pos.CENTER);
        
        Text helpText = new Text(nomeUsuario);
        helpText.setFill(Color.BLACK);
        helpText.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        
        Text helpText2 = new Text("Adversário");
        helpText2.setFill(Color.BLACK);
        helpText2.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        
        campoAdversario = new GridPane();
        campoUsuario = new GridPane();
        campoAdversario.setGridLinesVisible(true);
        campoUsuario.setGridLinesVisible(true);
        campoAdversario.setAlignment(Pos.CENTER);
        campoUsuario.setAlignment(Pos.CENTER);
        
        int tamanhoCelula = 30;
        
        for (int i = 0; i < TAMANHO; i++) {
            campoAdversario.getColumnConstraints().add(new ColumnConstraints(tamanhoCelula));
            campoUsuario.getColumnConstraints().add(new ColumnConstraints(tamanhoCelula));
            
            campoAdversario.getRowConstraints().add(new RowConstraints(tamanhoCelula));
            campoUsuario.getRowConstraints().add(new RowConstraints(tamanhoCelula));
        }
        
        vboxUsuario.getChildren().addAll(helpText, campoUsuario);
        vboxAdversario.getChildren().addAll(helpText2, campoAdversario);
        
        hboxCentro.getChildren().addAll(vboxUsuario, vboxAdversario);
        hboxCentro.setSpacing(50);
        hboxCentro.setAlignment(Pos.CENTER);
        
        root.setCenter(hboxCentro);
        
        BatalhaNavalMain.fxContainer.setScene(new Scene(root));
    }
}
