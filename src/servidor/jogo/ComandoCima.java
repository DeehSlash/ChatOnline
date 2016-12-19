package servidor.jogo;

import java.awt.Point;
import servidor.aplicacao.Principal;

public class ComandoCima implements Comando {

    int id;
    String time;
    
    public ComandoCima(int id, String time){
        this.id = id;
        this.time = time;
    }
    
    @Override
    public void executar() {
        Jogo jogo = Principal.getJogo(id);
        Point posicao;
        if(time.equals("azul"))
            posicao = jogo.getPosicaoAzul();
        else
            posicao = jogo.getPosicaoVermelho();
        // verifica se vai passar dos limites
        if((posicao.y - jogo.getPasso()) < 0)
            return;
        // verifica colisão
        if(jogo.getLimite("azul").intersects(jogo.getLimite("vermelho")))
            return;
        posicao.y -= jogo.getPasso(); // incrementa o y (anda para baixo)
        if(time.equals("azul")) // seta a posição no jogo
            jogo.setPosicaoAzul(posicao);
        else
            jogo.setPosicaoVermelho(posicao);
        jogo.atualizarPosicao(time); // atualiza todos os jogadores
    }

}
