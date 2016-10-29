package servidor.aplicacao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.UIManager;
import servidor.frames.*;
import compartilhado.modelo.*;
import java.sql.SQLException;

public class Principal {

    private static boolean executando;
    public static FrameInicio frmInicio;
    public static FramePrincipal frmPrincipal;
    private static ServerSocket servidor;
    public static GerenciadorBD gerenciador;
    public static ArrayList<Usuario> usuarios;
    public static ArrayList<ConexaoServidor> conexoes;
    
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
    
    public static void iniciarServidor(int porta) throws IOException, SQLException{
        conexoes = new ArrayList<>();
        servidor = new ServerSocket(porta);
        Principal.frmPrincipal.enviarLog("O servidor está sendo iniciado...");
        gerenciador = new GerenciadorBD("localhost/mensageiro", "root", "");
        frmPrincipal.enviarLog("Iniciando gerenciador de banco de dados...");
        usuarios = gerenciador.getListaUsuarios();
        frmPrincipal.enviarLog("Lista de usuários recuperada");
        executando = true;
        frmPrincipal.enviarLog("Servidor iniciado com sucesso na porta " + porta);   
        while(executando){
            Socket conexao = servidor.accept();
            ConexaoServidor cs = new ConexaoServidor(porta, conexao);
            conexoes.add(cs);
            Thread t = cs;
            t.start();
        }
    }

    public static void pararServidor() throws IOException{
        executando = false;
        servidor.close();
    }
}
