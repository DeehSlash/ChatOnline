package cliente.aplicacao;

import java.util.ArrayList;
import javax.swing.UIManager;
import cliente.frames.*;
import compartilhado.modelo.*;

public class Principal {
    
    public static FrameLogin frmLogin;
    public static FramePrincipal frmPrincipal;
    public static ArrayList<Usuario> usuarios;
    public static ArrayList<Mensagem> mensagens;
    
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
