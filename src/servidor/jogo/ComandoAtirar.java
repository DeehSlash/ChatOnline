package servidor.jogo;

public class ComandoAtirar implements Comando{

    int id;
    String time;
    
    public ComandoAtirar(int id, String time){
        this.id = id;
        this.time = time;
    }
    
    @Override
    public void executar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
