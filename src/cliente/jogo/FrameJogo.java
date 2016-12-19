package cliente.jogo;

import java.awt.Point;
import javax.swing.JFrame;

public class FrameJogo extends javax.swing.JFrame {

    int idGrupo;
    private PanelJogo jogo;
    
    public FrameJogo(int idGrupo) {
        this.idGrupo = idGrupo;
        inicializar();
    }
    
    private void inicializar(){
        jogo = new PanelJogo();
        add(jogo);
        setSize(516, 539);
        setResizable(true);
        setTitle("Jogo");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    public void atualizarPosicao(Point posicao, String time){
        if(time.equals("azul"))
            jogo.setPosicaoAzul(posicao);
        else
            jogo.setPosicaoVermelho(posicao);
        repaint();
    }
}