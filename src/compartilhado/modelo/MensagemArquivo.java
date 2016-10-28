package compartilhado.modelo;

import java.io.File;
import java.util.Date;

public class MensagemArquivo extends Mensagem<File> {

    public MensagemArquivo(){
        super.tipoMensagem = 'A';
    }
    
    private File arquivo;
    
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
    public File getMensagem(){ return arquivo; }
    
    @Override
    public void setMensagem(File mensagem) {
        this.arquivo = mensagem;
    }
    
    @Override
    public char getTipoMensagem(){ return tipoMensagem; }
    
    @Override
    public void setTipoMensagem(char tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }
    
    @Override
    public int getIdGrupo() {return idGrupo;}
    
    @Override
    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

}
