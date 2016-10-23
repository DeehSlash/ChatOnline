package compartilhado.modelo;

import java.util.Date;

public interface Mensagem {
    
    public int getIdMensagem();
    public int getIdOrigem();
    public int getIdDestino();
    public char getDestinoTipo();
    public Date getDataMensagem();
}
