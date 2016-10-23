package compartilhado.modelo;

import java.awt.Image;
import java.util.Date;

public class MensagemImagem implements Mensagem {
    
    private int idOrigem;
    private int idDestino;
    private int idMensagem;
    private char destinoTipo;
    private Date dataMensagem;
    private Image imagem;
    
    @Override
    public int getIdOrigem(){ return idOrigem; }
    @Override
    public int getIdDestino(){ return idDestino; }
    @Override
    public int getIdMensagem(){ return idMensagem; }
    @Override
    public char getDestinoTipo(){ return destinoTipo; }
    public Date getDataMensagem(){ return dataMensagem; }
    public Image getImagem(){ return imagem; }
}
