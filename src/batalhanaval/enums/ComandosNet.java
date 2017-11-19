package batalhanaval.enums;

public enum ComandosNet {
    
    SOLICITAR_CONEXAO("1"), RESPOSTA_SOLICITACAO_CONEXAO("2") , PRONTO("3"), DESCONECTAR("4"), JOGADA("5"), REPORTAR_JOGADA("6"), NAO_PRONTO("7"), NOME_REPETIDO("8");
    
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
