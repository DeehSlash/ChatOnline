package servidor.jogo;

public class ComandoInvoker {

    int idGrupo;
    Comando comando;
    
    public ComandoInvoker(int id){
        idGrupo = id;
    }
    
    public boolean setComando(String nome, String time){
        switch(nome){
            case ".atirar":
                comando = new ComandoAtirar(idGrupo, time);
                break;
            case ".baixo":
                comando = new ComandoBaixo(idGrupo, time);
                break;
            case ".cima":
                comando = new ComandoCima(idGrupo, time);
                break;
            case ".direita":
                comando = new ComandoDireita(idGrupo, time);
                break;
            case ".esquerda":
                comando = new ComandoEsquerda(idGrupo, time);
                break;
            default:
                comando = null;
                return false;
        }
        return true;
    }
    
    public void executar(){
        if(comando != null)
            comando.executar();
    }
}
