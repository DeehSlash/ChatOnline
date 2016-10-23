package compartilhado.modelo;

public class MensagemVideo implements Mensagem {
    
    private int idOrigem;
    private int idDestino;
    private int idMensagem;
    private char destinoTipo;
    //private Video video; verificar qual classe usar para vídeo
    
    @Override
    public int getIdOrigem(){ return idOrigem; }
    @Override
    public int getIdDestino(){ return idDestino; }
    @Override
    public int getIdMensagem(){ return idMensagem; }
    @Override
    public char getDestinoTipo(){ return destinoTipo; }
    //public Video getVideo(){ return video; } implementar método
}
