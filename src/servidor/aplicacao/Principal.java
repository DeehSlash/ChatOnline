package servidor.aplicacao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.UIManager;
import servidor.frames.*;
import compartilhado.modelo.*;

public class Principal {

    private static boolean executando;
    public static FrameInicio frmInicio;
    public static FramePrincipal frmPrincipal;
    private static ServerSocket servidor;
    public static ArrayList<Usuario> usuarios;
    
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
    
    public static void iniciarServidor(int porta) throws IOException{
        servidor = new ServerSocket(porta);
        executando = true;
        while(executando){
            Socket conexao = servidor.accept();
            frmPrincipal.alterarUsuarios(true);
            Thread t = new ConexaoServidor(porta, conexao);
            t.start();
        }
    }

    public static void pararServidor() throws IOException{
        executando = false;
        servidor.close();
    }
}
