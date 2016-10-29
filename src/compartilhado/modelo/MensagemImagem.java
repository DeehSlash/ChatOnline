package compartilhado.modelo;

import java.awt.Image;
import java.io.File;
import java.util.Date;

public class MensagemImagem extends Mensagem<Image> implements java.io.Serializable {
    
    public MensagemImagem(){
        super.tipoMensagem = 'I';
    }
    
    private Image imagem;
    
    @Override
    public int getIdOrigem(){ return idOrigem; }
    
    @Override
    public void setIdOrigem(int idOrigem) {
        this.idOrigem = idOrigem;
    }
    
    @Override
    public int getIdDestino(){ return idDestino; }
    
    @Override
    public void setIdDestino(int idDestino) {
        this.idDestino = idDestino;
    }
    
    @Override
    public int getIdMensagem(){ return idMensagem; }
    
    @Override
    public void setIdMensagem(int idMensagem) {
        this.idMensagem = idMensagem;
    }
    
    @Override
    public char getDestinoTipo(){ return destinoTipo; }
    
    @Override
    public void setDestinoTipo(char destinoTipo) {
        this.destinoTipo = destinoTipo;
    }
    
    @Override
    public Date getDataMensagem(){ return dataMensagem; }
    
    @Override
    public void setDataMensagem(Date dataMensagem) {
        this.dataMensagem = dataMensagem;
    }
    
    @Override
    public Image getMensagem(){ return imagem; }
    
    @Override
    public void setMensagem(Image mensagem) {
        this.imagem = mensagem;
    }
    
    @Override
    public char getTipoMensagem(){ return tipoMensagem; }
    
    @Override
    public void setTipoMensagem(char tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }
}
