package cliente.jogo;

import compartilhado.modelo.Usuario;
import java.util.ArrayList;
import javax.swing.JFrame;

public class FrameJogo extends javax.swing.JFrame {

    private PanelJogo jogo;
    private final ArrayList<Usuario> timeAzul;
    private final ArrayList<Usuario> timeVermelho;
    
    public FrameJogo(ArrayList<Usuario> timeAzul, ArrayList<Usuario> timeVermelho) {
        this.timeAzul = timeAzul;
        this.timeVermelho = timeVermelho;
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