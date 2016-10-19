package servidor.aplicacao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.UIManager;
import servidor.frames.*;

public class Principal {

    public static boolean executando;
    public static FrameInicio frmInicio;
    public static FramePrincipal frmPrincipal;
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        frmInicio = new FrameInicio();
        frmPrincipal = new FramePrincipal();
        frmInicio.setVisible(true);
    }
    
    public static void rodar(int porta) throws IOException{
        ServerSocket servidor = new ServerSocket(porta);
        while(executando){
            Socket conexao = servidor.accept();
            frmPrincipal.alterarUsuarios(true);
            Thread t = new ConexaoServidor(porta, conexao);
            t.start();
        }
        if(!executando)
            servidor.close();
    }
}
