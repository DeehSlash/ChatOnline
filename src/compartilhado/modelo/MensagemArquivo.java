package compartilhado.modelo;

import java.io.File;
import java.util.Date;

public class MensagemArquivo extends Mensagem<File> {

    private File arquivo;
    
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
    public File getMensagem(){ return arquivo; }
    public char getTipoMensagem(){ return tipoMensagem; }
}
