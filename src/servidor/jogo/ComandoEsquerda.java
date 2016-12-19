package servidor.jogo;

public class ComandoEsquerda implements Comando {

    int id;
    
    public ComandoEsquerda(int id){
        this.id = id;
    }
    
    @Override
    public void executar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
