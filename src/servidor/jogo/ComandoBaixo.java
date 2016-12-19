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
        // verifica se vai passar dos limites
        if((posicao.y + jogo.getTamVeiculo() + jogo.getPasso()) > jogo.getTamJanela().y)
            return;
        // verifica colisão
        Point comparacao = new Point();
        if(time.equals("azul")){
            comparacao.x = jogo.getPosicaoVermelho().x;
            comparacao.y = jogo.getPosicaoVermelho().y;
        }else if(time.equals("vermelho")){
            comparacao.x = jogo.getPosicaoAzul().x;
            comparacao.y = jogo.getPosicaoAzul().y;
        }
        boolean colisaoVertical = ((posicao.y + jogo.getTamVeiculo() + jogo.getPasso()) >= comparacao.y) &&
                ((posicao.y + jogo.getTamVeiculo() + jogo.getPasso()) <= (comparacao.y + jogo.getTamVeiculo()));
        boolean colisaoHorizontal = ((posicao.x + jogo.getTamVeiculo() + jogo.getPasso()) >= comparacao.x) &&
                ((posicao.x + jogo.getTamVeiculo() + jogo.getPasso()) <= (comparacao.x + jogo.getTamVeiculo()));
        if(colisaoVertical && colisaoHorizontal)
            return;
        posicao.y += jogo.getPasso(); // incrementa o y (anda para baixo)
        if(time.equals("azul")) // seta a posição no jogo
            jogo.setPosicaoAzul(posicao);
        else
            jogo.setPosicaoVermelho(posicao);
        jogo.atualizarPosicao(time); // atualiza todos os jogadores
    }

}
