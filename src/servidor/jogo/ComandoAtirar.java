package servidor.jogo;

import servidor.aplicacao.Principal;

public class ComandoAtirar implements Comando{

    int id;
    String time;
    
    public ComandoAtirar(int id, String time){
        this.id = id;
        this.time = time;
    }
    
    @Override
    public void executar() {
        Jogo jogo = Principal.getJogo(id);
        if(time.equals("azul") && jogo.getTiroAzul())
            return;
        if(time.equals("vermelho") && jogo.getTiroVermelho())
            return;
        jogo.criarTiro(time);
    }

}
