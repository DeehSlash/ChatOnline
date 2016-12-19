package servidor.jogo;

public class ComandoBaixo implements Comando {
    
    int id;
    
    public ComandoBaixo(int id){
        this.id = id;
    }
    
    @Override
    public void executar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
