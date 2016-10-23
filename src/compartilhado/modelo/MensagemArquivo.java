package compartilhado.modelo;

import java.io.File;

public class MensagemArquivo implements Mensagem {

    private int idOrigem;
    private int idDestino;
    private int idMensagem;
    private char destinoTipo;
    private File arquivo;
    
    @Override
    public int getIdOrigem(){ return idOrigem; }
    @Override
    public int getIdDestino(){ return idDestino; }
    @Override
    public int getIdMensagem(){ return idMensagem; }
    @Override
    public char getDestinoTipo(){ return destinoTipo; }
    public File getArquivo(){ return arquivo; }
}
