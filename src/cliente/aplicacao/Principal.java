package cliente.aplicacao;

import cliente.frames.*;
import javax.swing.UIManager;

public class Principal {
    
    public static FrameLogin frmLogin;
    public static FramePrincipal frmPrincipal;
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        frmLogin = new FrameLogin();
        frmLogin.setVisible(true);
    }
    
    public static void rodar(ConexaoCliente conexao){
        Thread t = conexao;
        t.start();
    }
}
