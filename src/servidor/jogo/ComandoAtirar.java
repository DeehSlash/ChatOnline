package servidor.jogo;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import servidor.aplicacao.Principal;

public class ComandoAtirar implements Comando{

    int id;
    String time;
    Timer timer;
    
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
        timer = new Timer(3, (ActionEvent e) -> {
            boolean colisao = false;
            boolean limiteX = false;
            boolean limiteY = false;
            if(time.equals("azul")){
                colisao = jogo.getLimiteTiro(time).intersects(jogo.getLimiteVeiculo("vermelho", 0, 0));
                limiteX = jogo.getPosicaoTiroAzul().getX() < 0 || jogo.getPosicaoTiroAzul().getX() > jogo.getTamJanela().x;
                limiteY = jogo.getPosicaoTiroAzul().getY() < 0 || jogo.getPosicaoTiroAzul().getY() > jogo.getTamJanela().y;
            }else if (time.equals("vermelho")){
                colisao = jogo.getLimiteTiro(time).intersects(jogo.getLimiteVeiculo("azul", 0, 0));
                limiteX = jogo.getPosicaoTiroVermelho().getX() < 0 || jogo.getPosicaoTiroVermelho().getX() > jogo.getTamJanela().x;
                limiteY = jogo.getPosicaoTiroVermelho().getY() < 0 || jogo.getPosicaoTiroVermelho().getY() > jogo.getTamJanela().y;
            }
            if(colisao || limiteX || limiteY){
                timer.stop();
                if(time.equals("azul"))
                    jogo.setTiroAzul(false);
                else
                    jogo.setTiroVermelho(false);
                if(colisao){
                    if(time.equals("azul")){
                        jogo.setVidaVermelho(jogo.getVidaVermelho() - 1);
                        jogo.atualizarVida(jogo.getVidaVermelho(), time);
                    }else{
                        jogo.setVidaAzul(jogo.getVidaAzul() - 1);
                        jogo.atualizarVida(jogo.getVidaAzul(), time);
                    }
                }
                return;
            }
            // incrementa o eixo apropriado e renderiza novamente
            if(time.equals("azul")){
                jogo.setPosicaoTiroAzul(new Point(jogo.getPosicaoTiroAzul().x, jogo.getPosicaoTiroAzul().y - 1));
            }else{
                jogo.setPosicaoTiroVermelho(new Point(jogo.getPosicaoTiroVermelho().x, jogo.getPosicaoTiroVermelho().y + 1));
            }
        });
    }

}
