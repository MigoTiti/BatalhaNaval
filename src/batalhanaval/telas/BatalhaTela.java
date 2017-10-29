package batalhanaval.telas;

import batalhanaval.BatalhaNavalMain;
import batalhanaval.tabuleiros.TabuleiroPronto;
import batalhanaval.util.RectangleNavio;
import java.net.URL;
import java.util.Set;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BatalhaTela extends TabuleiroPronto {

    private HBox hBoxVideoUsuario;
    private HBox hBoxVideoAdversario;

    public void iniciarTela(Set<RectangleNavio> naviosUsuario) {
        BorderPane root = new BorderPane();

        VBox vboxUsuario = new VBox();
        VBox vboxAdversario = new VBox();

        vboxUsuario.setSpacing(20);
        vboxAdversario.setSpacing(20);

        vboxUsuario.setAlignment(Pos.CENTER);
        vboxAdversario.setAlignment(Pos.CENTER);

        Text helpText = new Text(BatalhaNavalMain.nickName);
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

        for (int i = 0; i < TAMANHO; i++) {
            campoAdversario.getColumnConstraints().add(new ColumnConstraints(TAMANHO_CELULA));
            campoUsuario.getColumnConstraints().add(new ColumnConstraints(TAMANHO_CELULA));

            campoAdversario.getRowConstraints().add(new RowConstraints(TAMANHO_CELULA));
            campoUsuario.getRowConstraints().add(new RowConstraints(TAMANHO_CELULA));
        }

        StackPane campoUsuarioPronto = new StackPane();
        StackPane campoAdversarioPronto = new StackPane();

        VBox vBoxCampoUsuario = new VBox();
        vBoxCampoUsuario.setAlignment(Pos.CENTER);
        vBoxCampoUsuario.getChildren().addAll(campoUsuario);

        VBox vBoxCampoAdversario = new VBox();
        vBoxCampoAdversario.setAlignment(Pos.CENTER);
        vBoxCampoAdversario.getChildren().addAll(campoAdversario);

        HBox hBoxCampoUsuario = new HBox(vBoxCampoUsuario);
        hBoxCampoUsuario.setAlignment(Pos.CENTER);

        HBox hBoxCampoAdversario = new HBox(vBoxCampoAdversario);
        hBoxCampoAdversario.setAlignment(Pos.CENTER);

        MediaPlayer videoBackground = new MediaPlayer(
                new Media(getVideo().toString())
        );

        videoBackground.setMute(true);
        videoBackground.setCycleCount(MediaPlayer.INDEFINITE);
        videoBackground.play();
        videoBackground.setStartTime(Duration.seconds(0));
        videoBackground.setStopTime(Duration.seconds(19));

        MediaView videoBackgroundUsuario = new MediaView(videoBackground);
        videoBackgroundUsuario.setFitHeight(TAMANHO * TAMANHO_CELULA);
        videoBackgroundUsuario.setFitWidth(TAMANHO * TAMANHO_CELULA);
        videoBackgroundUsuario.setPreserveRatio(false);

        MediaView videoBackgroundAdversario = new MediaView(videoBackground);
        videoBackgroundAdversario.setFitHeight(TAMANHO * TAMANHO_CELULA);
        videoBackgroundAdversario.setFitWidth(TAMANHO * TAMANHO_CELULA);
        videoBackgroundAdversario.setPreserveRatio(false);

        hBoxVideoUsuario = new HBox(videoBackgroundUsuario);
        hBoxVideoUsuario.setAlignment(Pos.CENTER);
        
        hBoxVideoAdversario = new HBox(videoBackgroundAdversario);
        hBoxVideoAdversario.setAlignment(Pos.CENTER);

        campoUsuarioPronto.getChildren().addAll(hBoxVideoUsuario, hBoxCampoUsuario);
        
        naviosUsuario.stream().forEach((rectangleNavio) -> {
            rectangleNavio.setTranslateX(rectangleNavio.getTranslateX() - ((TAMANHO_CELULA * 6) - 7));
            rectangleNavio.setTranslateY(rectangleNavio.getTranslateY() - (TAMANHO_CELULA * 3));
            campoUsuarioPronto.getChildren().add(rectangleNavio);
        });
        
        campoAdversarioPronto.getChildren().addAll(hBoxVideoAdversario, hBoxCampoAdversario);

        vboxUsuario.getChildren().addAll(helpText, campoUsuarioPronto);
        vboxAdversario.getChildren().addAll(helpText2, campoAdversarioPronto);
        
        HBox hBoxCompleto = new HBox(vboxUsuario, vboxAdversario);
        hBoxCompleto.setSpacing(50);
        hBoxCompleto.setAlignment(Pos.CENTER);
        campoUsuarioPronto.setAlignment(Pos.TOP_LEFT);
        campoAdversarioPronto.setAlignment(Pos.TOP_LEFT);
        
        root.setCenter(hBoxCompleto);

        BatalhaNavalMain.fxContainer.setScene(new Scene(root));
    }

    private URL getVideo() {
        return getClass().getResource("recursos/background.mp4");
    }
}
