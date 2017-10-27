package batalhanaval.util;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class RectangleCoordenado extends Rectangle{
    
    private int xCoordenada;
    private int yCoordenada;
    private boolean ocupado = false;

    public RectangleCoordenado(int x, int y, double width, double height, Paint fill) {
        super(width, height, fill);
        this.xCoordenada = x;
        this.yCoordenada = y;
    }

    public int getxCoordenada() {
        return xCoordenada;
    }

    public int getyCoordenada() {
        return yCoordenada;
    }

    public void setxCoordenada(int xCoordenada) {
        this.xCoordenada = xCoordenada;
    }

    public void setyCoordenada(int yCoordenada) {
        this.yCoordenada = yCoordenada;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public boolean isOcupado() {
        return ocupado;
    }
}
