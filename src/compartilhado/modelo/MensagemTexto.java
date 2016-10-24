package compartilhado.modelo;

import java.util.Date;

public class MensagemTexto extends Mensagem<String> {
    
    public static final char tipo = 'T';
    private String mensagem;
    
    @Override
    public int getIdOrigem(){ return idOrigem; }
    @Override
    public int getIdDestino(){ return idDestino; }
    @Override
    public int getIdMensagem(){ return idMensagem; }
    @Override
    public char getDestinoTipo(){ return destinoTipo; }
    @Override
    public Date getDataMensagem(){ return dataMensagem; }
    @Override
    public String getMensagem(){ return mensagem; }
    public char getTipoMensagem(){ return tipoMensagem; }
}
