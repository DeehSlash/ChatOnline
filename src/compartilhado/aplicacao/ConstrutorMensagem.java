package compartilhado.aplicacao;

import java.util.Date;
import compartilhado.modelo.*;

public class ConstrutorMensagem<T> {
    
    public static Mensagem criarMensagem(int idOrigem, int idDestino, int idGrupo, int idMensagem, char destinoTipo, Date dataMensagem, char tipoMensagem, T mensagem){
        Mensagem msg;
        msg = MensagemFactory.criarMensagem(tipoMensagem);
        msg.setIdOrigem(idOrigem);
        msg.setIdDestino(idDestino);
        msg.setIdGrupo(idGrupo);
        msg.setIdMensagem(idMensagem);
        msg.setDestinoTipo(destinoTipo);
        msg.setDataMensagem(dataMensagem);
        msg.setTipoMensagem(tipoMensagem);
        msg.setMensagem(mensagem);
        return msg;
    }
}