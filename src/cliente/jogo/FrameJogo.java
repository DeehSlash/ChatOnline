package cliente.jogo;

import java.awt.Dimension;
import javax.swing.JFrame;

public class FrameJogo extends javax.swing.JFrame {

    public FrameJogo() {
        inicializar();
    }
    
    private void inicializar(){
        add(new PanelJogo());
        setSize(516, 539);
        setResizable(true);
        setTitle("Jogo");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}