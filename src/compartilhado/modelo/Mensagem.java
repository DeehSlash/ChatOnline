package compartilhado.modelo;

import java.awt.Image;
import java.io.File;
import java.util.Date;

public abstract class Mensagem<T>{
    
    protected int idOrigem;
    protected int idDestino;
    protected int idMensagem;
    protected char destinoTipo;
    protected Date dataMensagem;
    protected char tipoMensagem;
    
    public abstract int getIdOrigem();
    public abstract int getIdDestino();
    public abstract int getIdMensagem();
    public abstract char getDestinoTipo();
    public abstract Date getDataMensagem();
    public abstract T getMensagem();
    public abstract char getTipoMensagem();
    
}
