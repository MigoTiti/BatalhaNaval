package batalhanaval.telas;

import batalhanaval.enums.ComandosNet;
import batalhanaval.rede.Comunicacao;
import batalhanaval.tabuleiros.TabuleiroPreparacao;
import batalhanaval.util.RectangleCoordenado;
import batalhanaval.util.RectangleNavio;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

public class PreparacaoTela extends TabuleiroPreparacao {

    private int paContagem;
    private int ntContagem;
    private int ctContagem;
    private int subContagem;
    
    private final int contagemTotal;

    private final Text paContagemText;
    private final Text ntContagemText;
    private final Text ctContagemText;
    private final Text subContagemText;

    public static boolean oponentePronto = false;

    private final Comunicacao comunicador;
    
    public static final Color COR_BACKGROUND = Color.TRANSPARENT;
    
    private Rectangle preview;
    
    private StackPane stackPane;
    private HBox stub;
    
    private final Set<RectangleNavio> navios;

    public PreparacaoTela(Comunicacao comunicador) {
        this.comunicador = comunicador;
        
        paContagem = 1;
        ntContagem = 2;
        ctContagem = 3;
        subContagem = 4;
        
        contagemTotal = (paContagem * 5) + (ntContagem * 4) + (ctContagem * 3) + (subContagem * 2);
        
        navios = new HashSet<>();

        paContagemText = new Text(" x" + paContagem);
        ntContagemText = new Text(" x" + ntContagem);
        ctContagemText = new Text(" x" + ctContagem);
        subContagemText = new Text(" x" + subContagem);
    }

    public void iniciarTela(String nickname, String nicknameAdversario) {
        BorderPane root = new BorderPane();

        HBox hBoxTop = new HBox();

        hBoxTop.setSpacing(20);
        hBoxTop.setAlignment(Pos.CENTER);
        hBoxTop.setPadding(new Insets(20));

        Text helpText = new Text("Posicione os navios no campo");
        helpText.setFill(Color.BLACK);
        helpText.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        Button iniciar = new Button("Iniciar partida");
        iniciar.setOnAction(evento -> {
            if (paContagem == 0 && ntContagem == 0 && ctContagem == 0 && subContagem == 0) {
                new Thread(() -> {
                    comunicador.enviarMensagem(ComandosNet.PRONTO.comando + "&");
                    BatalhaTela.getInstance().iniciarTela(navios, contagemTotal, nickname, nicknameAdversario, comunicador);
                }).start();
            } else {
                TelaInicial.enviarMensagemErro("Posicione todos os navios");
            }
        });

        Button voltar = new Button("Sair da partida");
        voltar.setOnAction((ActionEvent) -> {
            comunicador.enviarMensagem(ComandosNet.DESCONECTAR.comando);
            comunicador.desconectar();
            TelaInicial.createScene();
        });

        hBoxTop.getChildren().addAll(voltar, helpText, iniciar);

        campo = new GridPane();
        campo.setAlignment(Pos.CENTER);

        campoMatriz = new RectangleCoordenado[TAMANHO][TAMANHO];

        for (int i = 0; i < TAMANHO; i++) {
            campo.getColumnConstraints().add(new ColumnConstraints(TAMANHO_CELULA));
            campo.getRowConstraints().add(new RowConstraints(TAMANHO_CELULA));
        }

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                RectangleCoordenado rect = gerarRect(i, j);
                campo.add(rect, i, j);
                campoMatriz[i][j] = rect;
            }
        }

        VBox vBoxCentro = new VBox();
        vBoxCentro.setAlignment(Pos.CENTER);
        vBoxCentro.getChildren().addAll(campo);

        HBox hBoxCentro = new HBox(vBoxCentro);
        hBoxCentro.setAlignment(Pos.CENTER);

        stub = new HBox(new Rectangle(TAMANHO * TAMANHO_CELULA, TAMANHO * TAMANHO_CELULA, Color.GREY));
        stub.setAlignment(Pos.CENTER);
        
        stackPane = new StackPane(stub, hBoxCentro);

        stackPane.setAlignment(Pos.TOP_LEFT);

        VBox vBoxDireita = new VBox();
        vBoxDireita.setAlignment(Pos.CENTER);
        vBoxDireita.setPadding(new Insets(20));

        vBoxDireita.getChildren().addAll(iniciarTelaSelecao());

        root.setCenter(stackPane);
        root.setTop(hBoxTop);
        root.setRight(vBoxDireita);

        TelaInicial.fxContainer.setScene(new Scene(root));
    }

    private GridPane iniciarTelaSelecao() {
        GridPane telaSelecao = new GridPane();
        telaSelecao.setAlignment(Pos.CENTER);
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

        return telaSelecao;
    }

    private RectangleCoordenado gerarRect(int x, int y) {
        RectangleCoordenado rect = new RectangleCoordenado(x, y, TAMANHO_CELULA - 1, TAMANHO_CELULA - 1, COR_BACKGROUND);
        rect.setStroke(Color.ORANGE);
        rect.setStrokeWidth(1);

        rect.setOnDragEntered((event) -> {
            int tamanho = Integer.parseInt(event.getDragboard().getString());

            boolean aceita = (rect.getxCoordenada() + tamanho) <= (TabuleiroPreparacao.TAMANHO);

            if (aceita) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            if (aceita) {
                switch (tamanho) {
                    case 5:
                        preview = new Rectangle(rect.getLayoutX(), rect.getLayoutY(), (TAMANHO_CELULA - 1) * tamanho, TAMANHO_CELULA - 1);
                        preview.setFill(new ImagePattern(new Image(getPortaAvioes().toString())));
                        preview.setTranslateX((TAMANHO_CELULA * (rect.getxCoordenada() + 5)) + 34);
                        preview.setTranslateY(TAMANHO_CELULA * (rect.getyCoordenada() + 3));
                        stackPane.getChildren().add(preview);
                        preview.toBack();
                        stub.toBack();
                        break;
                    case 4:
                        preview = new Rectangle(rect.getLayoutX(), rect.getLayoutY(), (TAMANHO_CELULA - 1) * tamanho, TAMANHO_CELULA - 1);
                        preview.setFill(new ImagePattern(new Image(getNavioTanque().toString())));
                        preview.setTranslateX((TAMANHO_CELULA * (rect.getxCoordenada() + 5)) + 34);
                        preview.setTranslateY(TAMANHO_CELULA * (rect.getyCoordenada() + 3));
                        stackPane.getChildren().add(preview);
                        preview.toBack();
                        stub.toBack();
                        break;
                    case 3:
                        preview = new Rectangle(rect.getLayoutX(), rect.getLayoutY(), (TAMANHO_CELULA - 1) * tamanho, TAMANHO_CELULA - 1);
                        preview.setFill(new ImagePattern(new Image(getContraTorpedo().toString())));
                        preview.setTranslateX((TAMANHO_CELULA * (rect.getxCoordenada() + 5)) + 34);
                        preview.setTranslateY(TAMANHO_CELULA * (rect.getyCoordenada() + 3));
                        stackPane.getChildren().add(preview);
                        preview.toBack();
                        stub.toBack();
                        break;
                    case 2:
                        preview = new Rectangle(rect.getLayoutX(), rect.getLayoutY(), (TAMANHO_CELULA - 1) * tamanho, TAMANHO_CELULA - 1);
                        preview.setFill(new ImagePattern(new Image(getSubmarino().toString())));
                        preview.setTranslateX((TAMANHO_CELULA * (rect.getxCoordenada() + 5)) + 34);
                        preview.setTranslateY(TAMANHO_CELULA * (rect.getyCoordenada() + 3));
                        stackPane.getChildren().add(preview);
                        preview.toBack();
                        stub.toBack();
                        break;
                }
            } else {
                stackPane.getChildren().remove(preview);
                preview = null;
            }

            event.consume();
        });

        rect.setOnDragExited((event) -> {
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

        rect.setOnDragDropped((DragEvent event) -> {
            int tamanho = Integer.parseInt(event.getDragboard().getString());

            switch (tamanho) {
                case 5:
                    decrementarPaContador();

                    RectangleNavio novo = new RectangleNavio(rect.getLayoutX(), rect.getLayoutY(), x, y, (TAMANHO_CELULA - 1) * tamanho, TAMANHO_CELULA - 1, new ImagePattern(new Image(getPortaAvioes().toString())), 5);
                    novo.setOnMouseClicked((evento) -> {
                        switch (novo.getRotacao()) {
                            case 1:
                                if (y + 4 <= TAMANHO - 1 && !campoMatriz[x][y + 1].isOcupado() && !campoMatriz[x][y + 2].isOcupado() && !campoMatriz[x][y + 3].isOcupado() && !campoMatriz[x][y + 4].isOcupado()) {
                                    campoMatriz[x][y + 1].setOcupado(true);
                                    campoMatriz[x][y + 1].setFill(Color.RED);
                                    campoMatriz[x][y + 2].setOcupado(true);
                                    campoMatriz[x][y + 2].setFill(Color.RED);
                                    campoMatriz[x][y + 3].setOcupado(true);
                                    campoMatriz[x][y + 3].setFill(Color.RED);
                                    campoMatriz[x][y + 4].setOcupado(true);
                                    campoMatriz[x][y + 4].setFill(Color.RED);

                                    campoMatriz[x + 1][y].setOcupado(false);
                                    campoMatriz[x + 1][y].setFill(COR_BACKGROUND);
                                    campoMatriz[x + 2][y].setOcupado(false);
                                    campoMatriz[x + 2][y].setFill(COR_BACKGROUND);
                                    campoMatriz[x + 3][y].setOcupado(false);
                                    campoMatriz[x + 3][y].setFill(COR_BACKGROUND);
                                    campoMatriz[x + 4][y].setOcupado(false);
                                    campoMatriz[x + 4][y].setFill(COR_BACKGROUND);
                                    novo.getTransforms().add(new Rotate(novo.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                            case 2:
                                if (x - 4 >= 0 && !campoMatriz[x - 1][y].isOcupado() && !campoMatriz[x - 2][y].isOcupado() && !campoMatriz[x - 3][y].isOcupado() && !campoMatriz[x - 4][y].isOcupado()) {
                                    campoMatriz[x - 1][y].setOcupado(true);
                                    campoMatriz[x - 1][y].setFill(Color.RED);
                                    campoMatriz[x - 2][y].setOcupado(true);
                                    campoMatriz[x - 2][y].setFill(Color.RED);
                                    campoMatriz[x - 3][y].setOcupado(true);
                                    campoMatriz[x - 3][y].setFill(Color.RED);
                                    campoMatriz[x - 4][y].setOcupado(true);
                                    campoMatriz[x - 4][y].setFill(Color.RED);

                                    campoMatriz[x][y + 1].setOcupado(false);
                                    campoMatriz[x][y + 1].setFill(COR_BACKGROUND);
                                    campoMatriz[x][y + 2].setOcupado(false);
                                    campoMatriz[x][y + 2].setFill(COR_BACKGROUND);
                                    campoMatriz[x][y + 3].setOcupado(false);
                                    campoMatriz[x][y + 3].setFill(COR_BACKGROUND);
                                    campoMatriz[x][y + 4].setOcupado(false);
                                    campoMatriz[x][y + 4].setFill(COR_BACKGROUND);
                                    novo.getTransforms().add(new Rotate(novo.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                            case 3:
                                if (y - 4 >= 0 && !campoMatriz[x][y - 1].isOcupado() && !campoMatriz[x][y - 2].isOcupado() && !campoMatriz[x][y - 3].isOcupado() && !campoMatriz[x][y - 4].isOcupado()) {
                                    campoMatriz[x][y - 1].setOcupado(true);
                                    campoMatriz[x][y - 1].setFill(Color.RED);
                                    campoMatriz[x][y - 2].setOcupado(true);
                                    campoMatriz[x][y - 2].setFill(Color.RED);
                                    campoMatriz[x][y - 3].setOcupado(true);
                                    campoMatriz[x][y - 3].setFill(Color.RED);
                                    campoMatriz[x][y - 4].setOcupado(true);
                                    campoMatriz[x][y - 4].setFill(Color.RED);

                                    campoMatriz[x - 1][y].setOcupado(false);
                                    campoMatriz[x - 1][y].setFill(COR_BACKGROUND);
                                    campoMatriz[x - 2][y].setOcupado(false);
                                    campoMatriz[x - 2][y].setFill(COR_BACKGROUND);
                                    campoMatriz[x - 3][y].setOcupado(false);
                                    campoMatriz[x - 3][y].setFill(COR_BACKGROUND);
                                    campoMatriz[x - 4][y].setOcupado(false);
                                    campoMatriz[x - 4][y].setFill(COR_BACKGROUND);
                                    novo.getTransforms().add(new Rotate(novo.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                            case 4:
                                if (x + 4 <= TAMANHO - 1 && !campoMatriz[x + 1][y].isOcupado() && !campoMatriz[x + 2][y].isOcupado() && !campoMatriz[x + 3][y].isOcupado() && !campoMatriz[x + 4][y].isOcupado()) {
                                    campoMatriz[x + 1][y].setOcupado(true);
                                    campoMatriz[x + 1][y].setFill(Color.RED);
                                    campoMatriz[x + 2][y].setOcupado(true);
                                    campoMatriz[x + 2][y].setFill(Color.RED);
                                    campoMatriz[x + 3][y].setOcupado(true);
                                    campoMatriz[x + 3][y].setFill(Color.RED);
                                    campoMatriz[x + 4][y].setOcupado(true);
                                    campoMatriz[x + 4][y].setFill(Color.RED);

                                    campoMatriz[x][y - 1].setOcupado(false);
                                    campoMatriz[x][y - 1].setFill(COR_BACKGROUND);
                                    campoMatriz[x][y - 2].setOcupado(false);
                                    campoMatriz[x][y - 2].setFill(COR_BACKGROUND);
                                    campoMatriz[x][y - 3].setOcupado(false);
                                    campoMatriz[x][y - 3].setFill(COR_BACKGROUND);
                                    campoMatriz[x][y - 4].setOcupado(false);
                                    campoMatriz[x][y - 4].setFill(COR_BACKGROUND);
                                    novo.getTransforms().add(new Rotate(novo.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                        }
                    });
                    novo.setTranslateX((TAMANHO_CELULA * (rect.getxCoordenada() + 5)) + 34);
                    novo.setTranslateY(TAMANHO_CELULA * (rect.getyCoordenada() + 3));

                    for (int i = x; i < x + tamanho; i++) {
                        campoMatriz[i][y].setFill(Color.RED);
                        campoMatriz[i][y].setOcupado(true);
                    }

                    stackPane.getChildren().add(novo);
                    navios.add(novo);

                    stackPane.getChildren().remove(preview);
                    preview = null;
                    break;
                case 4:
                    decrementarNtContador();

                    RectangleNavio novo2 = new RectangleNavio(rect.getLayoutX(), rect.getLayoutY(), x, y, (TAMANHO_CELULA - 1) * tamanho, TAMANHO_CELULA - 1, new ImagePattern(new Image(getNavioTanque().toString())), 4);
                    novo2.setOnMouseClicked((evento) -> {
                        switch (novo2.getRotacao()) {
                            case 1:
                                if (y + 3 <= TAMANHO - 1 && !campoMatriz[x][y + 1].isOcupado() && !campoMatriz[x][y + 2].isOcupado() && !campoMatriz[x][y + 3].isOcupado()) {
                                    campoMatriz[x][y + 1].setOcupado(true);
                                    campoMatriz[x][y + 1].setFill(Color.RED);
                                    campoMatriz[x][y + 2].setOcupado(true);
                                    campoMatriz[x][y + 2].setFill(Color.RED);
                                    campoMatriz[x][y + 3].setOcupado(true);
                                    campoMatriz[x][y + 3].setFill(Color.RED);

                                    campoMatriz[x + 1][y].setOcupado(false);
                                    campoMatriz[x + 1][y].setFill(COR_BACKGROUND);
                                    campoMatriz[x + 2][y].setOcupado(false);
                                    campoMatriz[x + 2][y].setFill(COR_BACKGROUND);
                                    campoMatriz[x + 3][y].setOcupado(false);
                                    campoMatriz[x + 3][y].setFill(COR_BACKGROUND);
                                    novo2.getTransforms().add(new Rotate(novo2.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                            case 2:
                                if (x - 3 >= 0 && !campoMatriz[x - 1][y].isOcupado() && !campoMatriz[x - 2][y].isOcupado() && !campoMatriz[x - 3][y].isOcupado()) {
                                    campoMatriz[x - 1][y].setOcupado(true);
                                    campoMatriz[x - 1][y].setFill(Color.RED);
                                    campoMatriz[x - 2][y].setOcupado(true);
                                    campoMatriz[x - 2][y].setFill(Color.RED);
                                    campoMatriz[x - 3][y].setOcupado(true);
                                    campoMatriz[x - 3][y].setFill(Color.RED);

                                    campoMatriz[x][y + 1].setOcupado(false);
                                    campoMatriz[x][y + 1].setFill(COR_BACKGROUND);
                                    campoMatriz[x][y + 2].setOcupado(false);
                                    campoMatriz[x][y + 2].setFill(COR_BACKGROUND);
                                    campoMatriz[x][y + 3].setOcupado(false);
                                    campoMatriz[x][y + 3].setFill(COR_BACKGROUND);
                                    novo2.getTransforms().add(new Rotate(novo2.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                            case 3:
                                if (y - 3 >= 0 && !campoMatriz[x][y - 1].isOcupado() && !campoMatriz[x][y - 2].isOcupado() && !campoMatriz[x][y - 3].isOcupado()) {
                                    campoMatriz[x][y - 1].setOcupado(true);
                                    campoMatriz[x][y - 1].setFill(Color.RED);
                                    campoMatriz[x][y - 2].setOcupado(true);
                                    campoMatriz[x][y - 2].setFill(Color.RED);
                                    campoMatriz[x][y - 3].setOcupado(true);
                                    campoMatriz[x][y - 3].setFill(Color.RED);

                                    campoMatriz[x - 1][y].setOcupado(false);
                                    campoMatriz[x - 1][y].setFill(COR_BACKGROUND);
                                    campoMatriz[x - 2][y].setOcupado(false);
                                    campoMatriz[x - 2][y].setFill(COR_BACKGROUND);
                                    campoMatriz[x - 3][y].setOcupado(false);
                                    campoMatriz[x - 3][y].setFill(COR_BACKGROUND);
                                    novo2.getTransforms().add(new Rotate(novo2.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                            case 4:
                                if (x + 3 <= TAMANHO - 1 && !campoMatriz[x + 1][y].isOcupado() && !campoMatriz[x + 2][y].isOcupado() && !campoMatriz[x + 3][y].isOcupado()) {
                                    campoMatriz[x + 1][y].setOcupado(true);
                                    campoMatriz[x + 1][y].setFill(Color.RED);
                                    campoMatriz[x + 2][y].setOcupado(true);
                                    campoMatriz[x + 2][y].setFill(Color.RED);
                                    campoMatriz[x + 3][y].setOcupado(true);
                                    campoMatriz[x + 3][y].setFill(Color.RED);

                                    campoMatriz[x][y - 1].setOcupado(false);
                                    campoMatriz[x][y - 1].setFill(COR_BACKGROUND);
                                    campoMatriz[x][y - 2].setOcupado(false);
                                    campoMatriz[x][y - 2].setFill(COR_BACKGROUND);
                                    campoMatriz[x][y - 3].setOcupado(false);
                                    campoMatriz[x][y - 3].setFill(COR_BACKGROUND);
                                    novo2.getTransforms().add(new Rotate(novo2.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                        }
                    });
                    novo2.setTranslateX((TAMANHO_CELULA * (rect.getxCoordenada() + 5)) + 34);
                    novo2.setTranslateY(TAMANHO_CELULA * (rect.getyCoordenada() + 3));

                    for (int i = x; i < x + tamanho; i++) {
                        campoMatriz[i][y].setFill(Color.RED);
                        campoMatriz[i][y].setOcupado(true);
                    }

                    stackPane.getChildren().add(novo2);
                    navios.add(novo2);

                    stackPane.getChildren().remove(preview);
                    preview = null;

                    break;
                case 3:
                    decrementarCtContador();

                    RectangleNavio novo3 = new RectangleNavio(rect.getLayoutX(), rect.getLayoutY(), x, y, (TAMANHO_CELULA - 1) * tamanho, TAMANHO_CELULA - 1, new ImagePattern(new Image(getContraTorpedo().toString())), 3);
                    novo3.setOnMouseClicked((evento) -> {
                        switch (novo3.getRotacao()) {
                            case 1:
                                if (y + 2 <= TAMANHO - 1 && !campoMatriz[x][y + 1].isOcupado() && !campoMatriz[x][y + 2].isOcupado()) {
                                    campoMatriz[x][y + 1].setOcupado(true);
                                    campoMatriz[x][y + 1].setFill(Color.RED);
                                    campoMatriz[x][y + 2].setOcupado(true);
                                    campoMatriz[x][y + 2].setFill(Color.RED);

                                    campoMatriz[x + 1][y].setOcupado(false);
                                    campoMatriz[x + 1][y].setFill(COR_BACKGROUND);
                                    campoMatriz[x + 2][y].setOcupado(false);
                                    campoMatriz[x + 2][y].setFill(COR_BACKGROUND);
                                    novo3.getTransforms().add(new Rotate(novo3.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                            case 2:
                                if (x - 2 >= 0 && !campoMatriz[x - 1][y].isOcupado() && !campoMatriz[x - 2][y].isOcupado()) {
                                    campoMatriz[x - 1][y].setOcupado(true);
                                    campoMatriz[x - 1][y].setFill(Color.RED);
                                    campoMatriz[x - 2][y].setOcupado(true);
                                    campoMatriz[x - 2][y].setFill(Color.RED);

                                    campoMatriz[x][y + 1].setOcupado(false);
                                    campoMatriz[x][y + 1].setFill(COR_BACKGROUND);
                                    campoMatriz[x][y + 2].setOcupado(false);
                                    campoMatriz[x][y + 2].setFill(COR_BACKGROUND);
                                    novo3.getTransforms().add(new Rotate(novo3.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                            case 3:
                                if (y - 2 >= 0 && !campoMatriz[x][y - 1].isOcupado() && !campoMatriz[x][y - 2].isOcupado()) {
                                    campoMatriz[x][y - 1].setOcupado(true);
                                    campoMatriz[x][y - 1].setFill(Color.RED);
                                    campoMatriz[x][y - 2].setOcupado(true);
                                    campoMatriz[x][y - 2].setFill(Color.RED);

                                    campoMatriz[x - 1][y].setOcupado(false);
                                    campoMatriz[x - 1][y].setFill(COR_BACKGROUND);
                                    campoMatriz[x - 2][y].setOcupado(false);
                                    campoMatriz[x - 2][y].setFill(COR_BACKGROUND);
                                    novo3.getTransforms().add(new Rotate(novo3.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                            case 4:
                                if (x + 2 <= TAMANHO - 1 && !campoMatriz[x + 1][y].isOcupado() && !campoMatriz[x + 2][y].isOcupado()) {
                                    campoMatriz[x + 1][y].setOcupado(true);
                                    campoMatriz[x + 1][y].setFill(Color.RED);
                                    campoMatriz[x + 2][y].setOcupado(true);
                                    campoMatriz[x + 2][y].setFill(Color.RED);

                                    campoMatriz[x][y - 1].setOcupado(false);
                                    campoMatriz[x][y - 1].setFill(COR_BACKGROUND);
                                    campoMatriz[x][y - 2].setOcupado(false);
                                    campoMatriz[x][y - 2].setFill(COR_BACKGROUND);
                                    novo3.getTransforms().add(new Rotate(novo3.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                        }
                    });
                    novo3.setTranslateX((TAMANHO_CELULA * (rect.getxCoordenada() + 5)) + 34);
                    novo3.setTranslateY(TAMANHO_CELULA * (rect.getyCoordenada() + 3));

                    for (int i = x; i < x + tamanho; i++) {
                        campoMatriz[i][y].setFill(Color.RED);
                        campoMatriz[i][y].setOcupado(true);
                    }

                    stackPane.getChildren().add(novo3);
                    navios.add(novo3);

                    stackPane.getChildren().remove(preview);
                    preview = null;

                    break;
                case 2:
                    decrementarSubContador();

                    RectangleNavio novo4 = new RectangleNavio(rect.getLayoutX(), rect.getLayoutY(), x, y, (TAMANHO_CELULA - 1) * tamanho, TAMANHO_CELULA - 1, new ImagePattern(new Image(getSubmarino().toString())), 2);
                    novo4.setOnMouseClicked((evento) -> {
                        switch (novo4.getRotacao()) {
                            case 1:
                                if (y + 1 <= TAMANHO - 1 && !campoMatriz[x][y + 1].isOcupado()) {
                                    campoMatriz[x][y + 1].setOcupado(true);
                                    campoMatriz[x][y + 1].setFill(Color.RED);

                                    campoMatriz[x + 1][y].setOcupado(false);
                                    campoMatriz[x + 1][y].setFill(COR_BACKGROUND);
                                    novo4.getTransforms().add(new Rotate(novo4.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                            case 2:
                                if (x - 1 >= 0 && !campoMatriz[x - 1][y].isOcupado()) {
                                    campoMatriz[x - 1][y].setOcupado(true);
                                    campoMatriz[x - 1][y].setFill(Color.RED);

                                    campoMatriz[x][y + 1].setOcupado(false);
                                    campoMatriz[x][y + 1].setFill(COR_BACKGROUND);
                                    novo4.getTransforms().add(new Rotate(novo4.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                            case 3:
                                if (y - 1 >= 0 && !campoMatriz[x][y - 1].isOcupado()) {
                                    campoMatriz[x][y - 1].setOcupado(true);
                                    campoMatriz[x][y - 1].setFill(Color.RED);

                                    campoMatriz[x - 1][y].setOcupado(false);
                                    campoMatriz[x - 1][y].setFill(COR_BACKGROUND);
                                    novo4.getTransforms().add(new Rotate(novo4.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                            case 4:
                                if (x + 1 <= TAMANHO - 1 && !campoMatriz[x + 1][y].isOcupado()) {
                                    campoMatriz[x + 1][y].setOcupado(true);
                                    campoMatriz[x + 1][y].setFill(Color.RED);

                                    campoMatriz[x][y - 1].setOcupado(false);
                                    campoMatriz[x][y - 1].setFill(COR_BACKGROUND);
                                    novo4.getTransforms().add(new Rotate(novo4.girar(), rect.getLayoutX() + 20, rect.getLayoutY() + 22));
                                }
                                break;
                        }
                    });
                    novo4.setTranslateX((TAMANHO_CELULA * (rect.getxCoordenada() + 5)) + 34);
                    novo4.setTranslateY(TAMANHO_CELULA * (rect.getyCoordenada() + 3));

                    for (int i = x; i < x + tamanho; i++) {
                        campoMatriz[i][y].setFill(Color.RED);
                        campoMatriz[i][y].setOcupado(true);
                    }

                    stackPane.getChildren().add(novo4);
                    navios.add(novo4);

                    stackPane.getChildren().remove(preview);
                    preview = null;

                    break;
            }

            event.setDropCompleted(true);
            event.consume();
        });

        return rect;
    }

    private URL getPortaAvioes() {
        return getClass().getResource("recursos/carrier.PNG");
    }

    private URL getNavioTanque() {
        return getClass().getResource("recursos/cruiser.PNG");
    }

    private URL getContraTorpedo() {
        return getClass().getResource("recursos/destroyer.PNG");
    }

    private URL getSubmarino() {
        return getClass().getResource("recursos/submarino.PNG");
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
