package servidor.jogo;

import java.awt.Point;
import servidor.aplicacao.Principal;

public class ComandoBaixo implements Comando {
    
    int id;
    String time;
    
    public ComandoBaixo(int id, String time){
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
        // verifica x
        if((posicao.x + jogo.getTamVeiculo() + jogo.getPasso()) > jogo.getTamJanela().x)
            return;
        // verifica colisão
        if(((posicao.x + jogo.getTamVeiculo() + jogo.getPasso()) >= jogo.getPosicaoVermelho().x) &&
                ((posicao.x + jogo.getTamVeiculo() + jogo.getPasso()) <= jogo.getPosicaoVermelho().x + jogo.getTamVeiculo()))
            return;
        posicao.x += jogo.getPasso(); // incrementa o x (anda para baixo)
        if(time.equals("azul")) // seta a posição no jogo
            jogo.setPosicaoAzul(posicao);
        else
            jogo.setPosicaoVermelho(posicao);
    }

}
