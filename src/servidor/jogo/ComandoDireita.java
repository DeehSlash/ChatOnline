package servidor.jogo;

public class ComandoDireita implements Comando {

    int id;
    
    public ComandoDireita(int id){
        this.id = id;
    }
    
    @Override
    public void executar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
