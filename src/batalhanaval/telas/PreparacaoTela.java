package batalhanaval.telas;

import batalhanaval.BatalhaNavalMain;
import batalhanaval.tabuleiros.TabuleiroPreparacao;
import batalhanaval.util.RectangleCoordenado;
import java.net.URL;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PreparacaoTela extends TabuleiroPreparacao {

    private int paContagem;
    private int ntContagem;
    private int ctContagem;
    private int subContagem;

    private final Text paContagemText;
    private final Text ntContagemText;
    private final Text ctContagemText;
    private final Text subContagemText;

    Rectangle preview;
    HBox hBoxVideo;
    StackPane stackPane;

    public PreparacaoTela() {
        paContagem = 1;
        ntContagem = 2;
        ctContagem = 3;
        subContagem = 4;

        paContagemText = new Text(" x" + paContagem);
        ntContagemText = new Text(" x" + ntContagem);
        ctContagemText = new Text(" x" + ctContagem);
        subContagemText = new Text(" x" + subContagem);
    }

    public void iniciarTela(String ip, String nomeUsuario) {
        BorderPane root = new BorderPane();

        HBox hBoxTop = new HBox();

        hBoxTop.setSpacing(20);
        hBoxTop.setAlignment(Pos.CENTER);
        hBoxTop.setPadding(new Insets(20));

        Text helpText = new Text("Posicione os navios no campo");
        helpText.setFill(Color.BLACK);
        helpText.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        Button iniciar = new Button("Iniciar partida");
        Button voltar = new Button("Sair da partida");
        voltar.setOnAction((ActionEvent) -> {
            BatalhaNavalMain.createScene();
        });
        //voltar.setAlignment(Pos.CENTER_LEFT);
        //iniciar.setAlignment(Pos.CENTER_RIGHT);

        campo = new GridPane();
        campo.setAlignment(Pos.CENTER);

        for (int i = 0; i < TAMANHO; i++) {
            campo.getColumnConstraints().add(new ColumnConstraints(TAMANHO_CELULA));
            campo.getRowConstraints().add(new RowConstraints(TAMANHO_CELULA));
        }

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                campo.add(gerarRect(i, j), i, j);
            }
        }

        VBox vBoxDireita = new VBox();
        vBoxDireita.setAlignment(Pos.CENTER);
        vBoxDireita.setPadding(new Insets(20));

        GridPane telaSelecao = new GridPane();
        telaSelecao.setAlignment(Pos.CENTER);
        //telaSelecao.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        telaSelecao.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        telaSelecao.setGridLinesVisible(true);

        int tamanhoCelulaSelecao = 100;

        telaSelecao.getColumnConstraints().add(new ColumnConstraints(tamanhoCelulaSelecao));

        for (int i = 0; i < 4; i++) {
            telaSelecao.getRowConstraints().add(new RowConstraints(tamanhoCelulaSelecao));
        }

        BorderPane b1 = new BorderPane(new Text("Porta-aviões"));
        b1.setOnDragDetected((event) -> {
            if (paContagem > 0) {
                Dragboard db = telaSelecao.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString("5");
                db.setContent(content);
            }

            event.consume();
        });
        b1.setBottom(paContagemText);

        BorderPane b2 = new BorderPane(new Text("Navio-tanque"));
        b2.setOnDragDetected((event) -> {
            if (ntContagem > 0) {
                Dragboard db = telaSelecao.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString("4");
                db.setContent(content);
            }

            event.consume();
        });
        b2.setBottom(ntContagemText);

        BorderPane b3 = new BorderPane(new Text("Contratorpedeiro"));
        b3.setOnDragDetected((event) -> {
            if (ctContagem > 0) {
                Dragboard db = telaSelecao.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString("3");
                db.setContent(content);
            }

            event.consume();
        });
        b3.setBottom(ctContagemText);

        BorderPane b4 = new BorderPane(new Text("Submarino"));
        b4.setOnDragDetected((event) -> {
            if (subContagem > 0) {
                Dragboard db = telaSelecao.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString("2");
                db.setContent(content);
            }

            event.consume();
        });
        b4.setBottom(subContagemText);

        telaSelecao.add(b1, 0, 0);
        telaSelecao.add(b2, 0, 1);
        telaSelecao.add(b3, 0, 2);
        telaSelecao.add(b4, 0, 3);

        VBox vBoxCentro = new VBox();
        vBoxCentro.setAlignment(Pos.CENTER);
        vBoxCentro.getChildren().addAll(campo);

        HBox hBoxCentro = new HBox();
        hBoxCentro.setAlignment(Pos.CENTER);
        hBoxCentro.getChildren().addAll(vBoxCentro);
        //campo.setStyle("-fx-background-image: url('" + imagem + "');");

        MediaPlayer oracleVid = new MediaPlayer(
                new Media(getVideo().toString())
        );

        oracleVid.setMute(true);
        oracleVid.setCycleCount(MediaPlayer.INDEFINITE);
        oracleVid.play();
        oracleVid.setStartTime(Duration.seconds(0));
        oracleVid.setStopTime(Duration.seconds(17.5));

        MediaView mv = new MediaView(oracleVid);
        mv.setFitHeight(TAMANHO * TAMANHO_CELULA);

        hBoxVideo = new HBox(mv);
        hBoxVideo.setAlignment(Pos.CENTER);

        stackPane = new StackPane(hBoxVideo, hBoxCentro);

        Rectangle clipRect = new Rectangle(TAMANHO * TAMANHO_CELULA, 1000);
        clipRect.setTranslateX(227);

        //clipRect.setTranslateY(227);
        stackPane.setClip(clipRect);
        stackPane.setAlignment(Pos.TOP_LEFT);

        vBoxDireita.getChildren().addAll(telaSelecao);
        hBoxTop.getChildren().addAll(voltar, helpText, iniciar);

        root.setCenter(stackPane);
        root.setTop(hBoxTop);
        root.setRight(vBoxDireita);

        BatalhaNavalMain.fxContainer.setScene(new Scene(root));
    }

    private RectangleCoordenado gerarRect(int x, int y) {
        RectangleCoordenado rect = new RectangleCoordenado(x, y, TAMANHO_CELULA - 1, TAMANHO_CELULA - 1, Color.TRANSPARENT);
        rect.setStroke(Color.ORANGE);
        rect.setStrokeWidth(1);

        rect.setOnDragEntered((event) -> {
            int tamanho = Integer.parseInt(event.getDragboard().getString());

            boolean aceita = (rect.getxCoordenada() + tamanho) <= (TabuleiroPreparacao.TAMANHO);

            if (aceita) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            
            if (aceita) {
                if (tamanho == 5) {
                    preview = new Rectangle(rect.getLayoutX(), rect.getLayoutY(), (TAMANHO_CELULA - 1) * 5, TAMANHO_CELULA - 1);
                    preview.setFill(new ImagePattern(new Image(getPortaAvioes().toString())));
                    preview.setTranslateX((TAMANHO_CELULA * (rect.getxCoordenada() + 5)) + 30);
                    preview.setTranslateY(TAMANHO_CELULA * (rect.getyCoordenada() + 3));
                    stackPane.getChildren().add(preview);
                    preview.toBack();
                    hBoxVideo.toBack();
                }
            } else {
                stackPane.getChildren().remove(preview);
                preview = null;
            }

            event.consume();
        });

        rect.setOnDragExited((event) -> {
            String tamanho = event.getDragboard().getString();

            stackPane.getChildren().remove(preview);
            preview = null;
            
            event.consume();
        });

        rect.setOnDragOver((event) -> {
            int tamanho = Integer.parseInt(event.getDragboard().getString());

            boolean aceita = (rect.getxCoordenada() + tamanho) <= (TabuleiroPreparacao.TAMANHO);

            if (aceita) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        });

        rect.setOnDragDropped((event) -> {
            String tamanho = event.getDragboard().getString();

            switch (tamanho) {
                case "5":
                    decrementarPaContador();
                    break;
                case "4":
                    decrementarNtContador();
                    break;
                case "3":
                    decrementarCtContador();
                    break;
                case "2":
                    decrementarSubContador();
                    break;
            }

            event.setDropCompleted(true);
            event.consume();
        });

        return rect;
    }

    private URL getVideo() {
        return getClass().getResource("recursos/backgroundloop.mp4");
    }

    private URL getPortaAvioes() {
        return getClass().getResource("recursos/carrier.PNG");
    }

    private URL getNavioTanque() {
        return getClass().getResource("recursos/backgroundloop.mp4");
    }

    private URL getContraTorpedo() {
        return getClass().getResource("recursos/backgroundloop.mp4");
    }

    private URL getSubmarino() {
        return getClass().getResource("recursos/backgroundloop.mp4");
    }

    private void previewBarco(int x, int y, int tamanho) {

    }

    private void decrementarPaContador() {
        if (paContagem > 0) {
            paContagem--;
            paContagemText.setText(" x" + paContagem);

            if (paContagem == 0) {
                paContagemText.setFill(Color.RED);
            }
        }
    }

    private void decrementarNtContador() {
        if (ntContagem > 0) {
            ntContagem--;
            ntContagemText.setText(" x" + ntContagem);

            if (ntContagem == 0) {
                ntContagemText.setFill(Color.RED);
            }
        }
    }

    private void decrementarCtContador() {
        if (ctContagem > 0) {
            ctContagem--;
            ctContagemText.setText(" x" + ctContagem);

            if (ctContagem == 0) {
                ctContagemText.setFill(Color.RED);
            }
        }
    }

    private void decrementarSubContador() {
        if (subContagem > 0) {
            subContagem--;
            subContagemText.setText(" x" + subContagem);

            if (subContagem == 0) {
                subContagemText.setFill(Color.RED);
            }
        }
    }
}
