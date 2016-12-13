package servidor.aplicacao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.UIManager;
import servidor.frames.*;
import compartilhado.modelo.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
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
    
    public static void iniciarServidor(String endereco, int porta) throws IOException, SQLException, NotBoundException{
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
        LocateRegistry.createRegistry(porta + 1); // inicia o registro RMI na porta informada + 1
        frmPrincipal.enviarLog("Registro RMI iniciado na porta 8081");
        executando = true;
        frmPrincipal.enviarLog("Servidor iniciado com sucesso na porta " + porta);   
        while(executando){
            Socket socket = servidor.accept();
            Conexao conexao = new Conexao(id, endereco, porta, socket);
            Principal.conexoes.add(conexao);
            conexao.conectar();
            id++;
            Thread t = conexao;
            t.start();
        }
    }

    public static void pararServidor() throws IOException{
        executando = false;
        servidor.close();
        for (Conexao conexao : conexoes) {
            conexao.desconectar();
        }
    }
}
