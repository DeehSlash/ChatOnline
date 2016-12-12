package servidor.aplicacao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.UIManager;
import servidor.frames.*;
import compartilhado.modelo.*;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class Principal {

    private static boolean executando;
    
    public static FrameInicio frmInicio;
    public static FramePrincipal frmPrincipal;
    
    private static ServerSocket servidor;
    public static GerenciadorBD gerenciador;
    
    public static ArrayList<Usuario> usuarios;
    public static ArrayList<Grupo> grupos;
    public static ArrayList<Conexao> conexoes;
    
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
    
    public static void iniciarServidor(int porta) throws IOException, SQLException, NotBoundException{
        int id = 0;
        conexoes = new ArrayList<>();
        servidor = new ServerSocket(porta);
        Principal.frmPrincipal.enviarLog("O servidor está sendo iniciado...");
        gerenciador = new GerenciadorBD("localhost/mensageiro", "root", "");
        frmPrincipal.enviarLog("Iniciando gerenciador de banco de dados...");
        usuarios = gerenciador.getListaUsuarios();
        frmPrincipal.enviarLog("Lista de usuários recuperada");
        grupos = gerenciador.getListaGrupos();
        frmPrincipal.enviarLog("Lista de grupos recuperada");
        executando = true;
        frmPrincipal.enviarLog("Servidor iniciado com sucesso na porta " + porta);   
        while(executando){
            Socket conexao = servidor.accept();
            Conexao cs = new Conexao(id, porta, conexao);
            conexoes.add(cs);
            id++;
            Thread t = cs;
            t.start();
        }
    }

    public static void pararServidor() throws IOException{
        executando = false;
        servidor.close();
        for (Conexao conexao : conexoes) {
            conexao.fecharConexao();
        }
    }
}
