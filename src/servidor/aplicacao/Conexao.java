package servidor.aplicacao;

import compartilhado.aplicacao.IComunicadorCliente;
import compartilhado.modelo.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class Conexao extends Thread {
    
    private int id;
    private String endereco;
    private int porta;
    private final Socket conexao;
    public IComunicadorCliente comunicador;
    private ComunicadorServidor comunicadorServidor;
    private int idCliente;
    private BufferedReader entradaString;
    private ObjectInputStream entradaObjeto;
    private ObjectOutputStream saidaObjeto;
    private DataInputStream entradaDado;
    private DataOutputStream saidaDado;
    
    public Conexao(int id, String endereco, int porta, Socket cliente) throws IOException, NotBoundException{
        this.id = id;
        this.endereco = endereco;
        this.porta = porta;
        this.conexao = cliente;
    }
    
    public int getIdConexao(){ return id; }
    public int getIdCliente(){ return this.idCliente; }
    public void setIdCliente(int id){ this.idCliente = id; } 
    public boolean getStatus(){ return !conexao.isClosed() && conexao.isConnected(); }
    public void setComunicador(IComunicadorCliente comunicador){ this.comunicador = comunicador; };
    
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
        System.setProperty("java.rmi.server.hostname", endereco);
        comunicadorServidor = new ComunicadorServidor(id); // cria um novo comunicador
        Naming.rebind("//localhost:" + (porta + 1) + "/ComunicadorServidor", comunicadorServidor); // vincula o objeto comunicador ao endereço RMI
    }
    
    public void desconectar() throws IOException{
        Principal.frmPrincipal.enviarLog("Usuário " + Principal.usuarios.get(idCliente - 1).getUsuario() + " (" + idCliente + ") se desconectou");
        Principal.frmPrincipal.alterarUsuarios(false);
        int i = 0;
        for (Conexao conexao : Principal.conexoes) {
            if(conexao.getIdCliente() == getIdCliente()){
                Principal.conexoes.remove(i);
                break;
            }
            i++;
        }
        setOnline(false);
        atualizarListaUsuarios();
        conexao.close();
    }
    
    public void atualizarListaUsuarios() throws RemoteException{
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
