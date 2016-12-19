package cliente.jogo;

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
}