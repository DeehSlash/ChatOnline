package servidor.aplicacao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.UIManager;
import servidor.frames.*;

public class Principal {

    public static boolean executando;
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        FrameInicio frame = new FrameInicio();
        frame.setVisible(true);
        
    }
    
    public static void rodar(int porta) throws IOException{
        ServerSocket servidor = new ServerSocket(porta);
        while(executando){
            Socket conexao = servidor.accept();
            Thread t = new ConexaoServidor(porta, conexao);
            t.start();
        }
    }
}
