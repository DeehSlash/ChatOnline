package compartilhado.aplicacao;

import compartilhado.modelo.*;
import java.util.Calendar;

public class MensagemBuilder<T> {
    
    private final int idOrigem;
    private final int idDestino;
    private final char tipoDestino;
    
    public MensagemBuilder(int idOrigem, int idDestino, char tipoDestino){
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
        this.tipoDestino = tipoDestino;
    }
    
    public Mensagem criarMensagem(int idMensagem, char tipoMensagem, T mensagem){
        Mensagem msg;
        msg = MensagemFactory.criarMensagem(tipoMensagem);
        msg.setIdOrigem(idOrigem);
        msg.setIdDestino(idDestino);
        msg.setIdMensagem(idMensagem);
        msg.setDestinoTipo(tipoDestino);
        msg.setDataMensagem(Calendar.getInstance().getTime());
        msg.setMensagem(mensagem);
        return msg;
    }
}