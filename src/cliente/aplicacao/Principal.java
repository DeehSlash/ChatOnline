package cliente.aplicacao;

import cliente.frames.FrameLogin;
import javax.swing.UIManager;

public class Principal {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        FrameLogin login = new FrameLogin();
        login.setVisible(true);
    }
}
