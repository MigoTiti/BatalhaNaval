package batalhanaval.util;

import javafx.scene.paint.Paint;

public class RectangleNavio extends RectangleCoordenado{
    
    private int tamanho;

    public RectangleNavio(double x1, double y1, int x, int y, double width, double height, Paint fill, int tamanho) {
        super(x, y, width, height, fill);
        super.setX(x1);
        super.setY(y1);
        this.tamanho = tamanho;
    }
    
    public double girar() {
        switch (rotacao) {
            case 1:
                rotacao++;
                break;
            case 2:
                rotacao++;
                break;
            case 3:
                rotacao++;
                break;
            case 4:
                rotacao = 1;
                break;
        }
        
        return 90;
    }
    
}
