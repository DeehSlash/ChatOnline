package servidor.jogo;

public class ComandoAtirar implements Comando{

    int id;
    
    public ComandoAtirar(int id){
        this.id = id;
    }
    
    @Override
    public void executar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
