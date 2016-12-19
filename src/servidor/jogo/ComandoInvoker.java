package servidor.jogo;

public class ComandoInvoker {

    int idGrupo;
    Comando comando;
    
    public ComandoInvoker(int id){
        idGrupo = id;
    }
    
    public boolean setComando(String nome){
        switch(nome){
            case ".atirar":
                comando = new ComandoAtirar(idGrupo);
                break;
            case ".baixo":
                comando = new ComandoBaixo(idGrupo);
                break;
            case ".cima":
                comando = new ComandoCima(idGrupo);
                break;
            case ".direita":
                comando = new ComandoDireita(idGrupo);
                break;
            case ".esquerda":
                comando = new ComandoEsquerda(idGrupo);
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
