package compartilhado.modelo;

import java.util.Date;

public class MensagemVideo extends Mensagem<String> {
 
    public static final char tipo = 'V';
    //private Video video; verificar qual classe usar para vídeo
    
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
    public String getMensagem(){ return "OK";}
    @Override
    public char getTipoMensagem(){ return tipoMensagem; }
}
