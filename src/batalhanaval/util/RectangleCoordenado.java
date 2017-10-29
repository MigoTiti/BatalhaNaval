package batalhanaval.util;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class RectangleCoordenado extends Rectangle{
    
    protected int xCoordenada;
    protected int yCoordenada;
    protected boolean ocupado = false;
    protected int rotacao = 1;

    public RectangleCoordenado(int x, int y, double width, double height, Paint fill) {
        super(width, height, fill);
        this.xCoordenada = x;
        this.yCoordenada = y;
    }
    
    public RectangleCoordenado(double x, double y, double width, double height) {
        super(x, y, width, height);
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

    public int getRotacao() {
        return rotacao;
    }

    public void setRotacao(int rotacao) {
        this.rotacao = rotacao;
    }
    
}
