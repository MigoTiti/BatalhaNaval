package batalhanaval.tabuleiros;

import batalhanaval.util.RectangleCoordenado;
import javafx.scene.layout.GridPane;

public class TabuleiroPronto {

    protected GridPane campoUsuario;
    protected GridPane campoAdversario;
    
    protected RectangleCoordenado[][] campoUsuarioMatriz;
    protected RectangleCoordenado[][] campoAdversarioMatriz;
    
    public static final int TAMANHO = 10;
    public static final int TAMANHO_CELULA = 40;
    
}
