package compartilhado.modelo;

import java.util.Date;

public abstract class Mensagem<T>{
    
    protected int idOrigem;
    protected int idDestino;
    protected int idGrupo;
    protected int idMensagem;
    protected char destinoTipo;
    protected Date dataMensagem;
    protected char tipoMensagem;
    
    public abstract int getIdOrigem();
    public abstract void setIdOrigem(int idOrigem);
    public abstract int getIdDestino();
    public abstract void setIdDestino(int idDestino);
    public abstract int getIdMensagem();
    public abstract void setIdMensagem(int idMensagem);
    public abstract char getDestinoTipo();
    public abstract void setDestinoTipo(char destinoTipo);
    public abstract Date getDataMensagem();
    public abstract void setDataMensagem(Date dataMensagem);
    public abstract T getMensagem();
    public abstract void setMensagem(T mensagem);
    public abstract char getTipoMensagem();
    public abstract void setTipoMensagem(char tipoMensagem);
    public abstract int getIdGrupo();
    public abstract void setIdGrupo(int idGrupo);
}
