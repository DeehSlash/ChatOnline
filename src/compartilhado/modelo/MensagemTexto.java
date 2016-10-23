package compartilhado.modelo;

import java.util.Date;

public class MensagemTexto implements Mensagem {
    
    private int idOrigem;
    private int idDestino;
    private int idMensagem;
    private char destinoTipo;
    private Date dataMensagem;
    private String mensagem;
    
    @Override
    public int getIdOrigem(){ return idOrigem; }
    @Override
    public int getIdDestino(){ return idDestino; }
    @Override
    public int getIdMensagem(){ return idMensagem; }
    @Override
    public char getDestinoTipo(){ return destinoTipo; }
    public Date getDataMensagem(){ return dataMensagem; }
    public String getMensagem(){ return mensagem; }
}
