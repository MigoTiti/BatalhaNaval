package batalhanaval.enums;

public enum ComandosNet {
    
    CONECTADO("1"), PRONTO("2"), DESCONECTAR("3"), JOGADA("4"), REPORTAR_JOGADA("5"), NAO_PRONTO("6"), NOME_REPETIDO("7");
    
    public String comando;

    private ComandosNet(String comando) {
        this.comando = comando;
    }
    
    public byte[] getBytes() {
        return this.comando.getBytes();
    }

    @Override
    public String toString() {
        return this.comando;
    }
}
