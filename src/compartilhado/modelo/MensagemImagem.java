package compartilhado.modelo;

import java.awt.Image;

public class MensagemImagem implements Mensagem {
    
    private int idOrigem;
    private int idDestino;
    private int idMensagem;
    private char destinoTipo;
    private Image imagem;
    
    @Override
    public int getIdOrigem(){ return idOrigem; }
    @Override
    public int getIdDestino(){ return idDestino; }
    @Override
    public int getIdMensagem(){ return idMensagem; }
    @Override
    public char getDestinoTipo(){ return destinoTipo; }
    public Image getImagem(){ return imagem; }
}
