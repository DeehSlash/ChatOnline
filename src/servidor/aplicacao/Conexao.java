package servidor.aplicacao;

// IMPORTAÇÕES DO PROJETO
import compartilhado.aplicacao.IComunicadorCliente;
// IMPORTAÇÕES JAVA
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Conexao extends Thread {
    
    private int id; // id da conexão
    
    private final Socket cliente;
    public IComunicadorCliente comunicador;
    private ComunicadorServidor comunicadorServidor;
    
    private int idCliente;
    
    public Conexao(int id, Socket cliente){
        this.id = id;
        this.cliente = cliente;
    }
    
    public int getIdConexao(){ return id; }
    public int getIdCliente(){ return this.idCliente; }
    public void setIdCliente(int id){ this.idCliente = id; } 
    public boolean getStatus(){ return !cliente.isClosed() && cliente.isConnected(); }
    public void setComunicador(IComunicadorCliente comunicador){ this.comunicador = comunicador; };
    
    public void conectar() throws RemoteException, MalformedURLException, NotBoundException{
        // COMUNICADOR SERVIDOR (USADO PELO CLIENTE)
        comunicadorServidor = new ComunicadorServidor(id); // cria um novo comunicador
        Naming.rebind("//localhost:" + (Principal.porta + 1) + "/ComunicadorServidor", comunicadorServidor); // vincula o objeto comunicador ao endereço RMI
    }
    
    public void desconectar() throws IOException{
        Principal.frmPrincipal.enviarLog("Usuário " + Principal.usuarios.get(idCliente - 1).getUsuario() + " (" + idCliente + ") se desconectou");
        Principal.frmPrincipal.alterarUsuarios(false); // decrementa a qtd de usuários online
        int i = 0;
        for (Conexao conexao : Principal.conexoes) {
            if(conexao.getIdCliente() == getIdCliente()){
                Principal.conexoes.remove(i); // remove esta conexão
                break;
            }
            i++;
        }
        setOnline(false); // define o usuário como offline
        atualizarListaUsuarios(); // atualiza a lista de usuários
        cliente.close(); // fecha a conexão
    }
    
    public void atualizarListaUsuarios() throws RemoteException{
        for (Conexao conexao : Principal.conexoes) { // para cada conexão
            if(conexao.getIdCliente() != getIdCliente()) // exceto a própria
                conexao.comunicador.atualizarListaUsuarios(Principal.usuarios); // envia a lista de usuários atualizada
        }
    }
    
    private void setOnline(boolean online){
        Principal.usuarios.get(idCliente - 1).setOnline(online);
    }
    
    @Override
    public void run(){
    }
}
