package batalhanaval.enums;

public enum ComandosNet {
    
    CONECTADO("1"), PRONTO("2"), DESCONECTAR("3"), JOGADA("4");
    
    public String comando;

    private ComandosNet(String comando) {
        this.comando = comando;
    }
    
    public byte[] getBytes() {
        return this.comando.getBytes();
    }
}
