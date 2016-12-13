package servidor.aplicacao;

import compartilhado.aplicacao.IComunicadorCliente;
import compartilhado.modelo.*;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Conexao extends Thread {
    
    private int id;
    private final Socket conexao;
    public IComunicadorCliente comunicador;
    private ComunicadorServidor comunicadorServidor;
    private int idCliente;
    private BufferedReader entradaString;
    private ObjectInputStream entradaObjeto;
    private ObjectOutputStream saidaObjeto;
    private DataInputStream entradaDado;
    private DataOutputStream saidaDado;
    
    public Conexao(int id, int porta, Socket cliente) throws IOException, NotBoundException{
        this.conexao = cliente;
        this.id = id;
    }
    
    public int getIdConexao(){ return id; }
    public int getIdCliente(){ return this.idCliente; }
    public void setIdCliente(int id){ this.idCliente = id; } 
    public boolean getStatus(){ return !conexao.isClosed() && conexao.isConnected(); }
    
    private ObjectInputStream getEntradaObjeto() throws IOException{
        return entradaObjeto = new ObjectInputStream(conexao.getInputStream());
    }
    private ObjectOutputStream getSaidaObjeto() throws IOException{
        return saidaObjeto = new ObjectOutputStream(conexao.getOutputStream());
    }
    
    private DataInputStream getEntradaDado() throws IOException{
        return entradaDado = new DataInputStream(conexao.getInputStream());
    }
    
    private DataOutputStream getSaidaDado() throws IOException{
        return saidaDado = new DataOutputStream(conexao.getOutputStream());
    }
    
    public void conectar() throws RemoteException, MalformedURLException, NotBoundException{
        // COMUNICADOR SERVIDOR (USADO PELO CLIENTE)
        comunicadorServidor = new ComunicadorServidor(id); // cria um novo comunicador
        LocateRegistry.createRegistry(8081); // inicia o registro RMI na porta 8081
        Naming.rebind("//:8081/ComunicadorServidor", comunicadorServidor); // vincula o objeto comunicador ao endereço RMI
        // COMUNICADOR CLIENTE (USADO PELO SERVIDOR)
        comunicador = (IComunicadorCliente) Naming.lookup("//" + conexao.getInetAddress().getHostAddress() + ":8082/ComunicadorCliente"); // procura o comunicador no cliente
    }
    
    public void desconectar() throws IOException{
        Principal.frmPrincipal.enviarLog("Usuário " + Principal.usuarios.get(idCliente - 1).getUsuario() + " (" + idCliente + ") se desconectou");
        Principal.frmPrincipal.alterarUsuarios(false);
        int i = 0;
        for (Conexao conexao : Principal.conexoes) {
            if(conexao.getIdCliente() == getIdCliente())
                break;
            i++;
        }
        Principal.conexoes.remove(i);
        setOnline(false);
        atualizarListaUsuarios();
        conexao.close();
    }
    
    private void atualizarListaUsuarios() throws IOException{
        for (Conexao conexao : Principal.conexoes) {
            if(conexao.getIdCliente() != getIdCliente())
                conexao.comunicador.atualizarListaUsuarios(Principal.usuarios); // envia a lista atualizada para o usuário
        }
    }
    
    private void setOnline(boolean online){
        Principal.usuarios.get(idCliente - 1).setOnline(online);
    }
    
    private void alterarUsuario() throws IOException, ClassNotFoundException, SQLException{
        Usuario usuario = (Usuario)getEntradaObjeto().readObject();
        Principal.gerenciador.alterarUsuario(usuario);
        Principal.usuarios.set(usuario.getId() - 1, usuario);
        atualizarListaUsuarios();
    }
    
    private void criarGrupo() throws IOException, ClassNotFoundException, SQLException{
        Grupo grupo = (Grupo) getEntradaObjeto().readObject();
        Principal.gerenciador.criarGrupo(grupo);
        Principal.grupos.add(grupo);
        Principal.frmPrincipal.enviarLog("Grupo" + grupo.getNome() + " foi criado");
    }
    
    private void receberIdGrupoDisponivel() throws IOException, SQLException{
        int id = Principal.gerenciador.receberIdGrupoDisponivel();
        getSaidaDado().writeInt(id);
    }
    
    @Override
    public void run(){
    }
}
