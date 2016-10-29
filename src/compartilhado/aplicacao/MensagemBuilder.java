package compartilhado.aplicacao;

import java.util.Date;
import compartilhado.modelo.*;
import java.util.Calendar;

public class MensagemBuilder<T> {
    
    private int idOrigem;
    private int idDestino;
    
    public MensagemBuilder(int idOrigem, int idDestino){
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
    }
    
    public Mensagem criarMensagem(int idMensagem, char destinoTipo, char tipoMensagem, T mensagem){
        Mensagem msg;
        msg = MensagemFactory.criarMensagem(tipoMensagem);
        msg.setIdOrigem(idOrigem);
        msg.setIdDestino(idDestino);
        msg.setIdMensagem(idMensagem);
        msg.setDestinoTipo(destinoTipo);
        msg.setDataMensagem(Calendar.getInstance().getTime());
        msg.setMensagem(mensagem);
        return msg;
    }
}