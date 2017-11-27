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
        if(time.equals("azul")){
            if(jogo.getLimiteVeiculo("azul", 0, -jogo.getPasso()).intersects(jogo.getLimiteVeiculo("vermelho", 0, 0)))
                return;
        }else{
            if(jogo.getLimiteVeiculo("azul", 0, 0).intersects(jogo.getLimiteVeiculo("vermelho", 0, -jogo.getPasso())))
                return;
        }
        posicao.y -= jogo.getPasso(); // incrementa o y (anda para baixo)
        if(time.equals("azul")) // seta a posição no jogo
            jogo.setPosicaoAzul(posicao);
        else
            jogo.setPosicaoVermelho(posicao);
        jogo.atualizarPosicao(time); // atualiza todos os jogadores
    }

}
