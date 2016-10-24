package compartilhado.modelo;

import java.awt.Image;
import java.io.File;
import java.util.Date;

public class MensagemImagem extends Mensagem<Image> {
    
    public static final char tipo = 'I';
    private Image imagem;
    
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
    public Image getMensagem(){ return imagem; }
    public char getTipoMensagem(){ return tipoMensagem; }
}
