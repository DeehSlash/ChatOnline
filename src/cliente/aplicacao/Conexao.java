package cliente.aplicacao;

// IMPORTAÇÕES DO PROJETO
import compartilhado.aplicacao.IComunicadorCliente;
import compartilhado.aplicacao.IComunicadorServidor;
import compartilhado.modelo.*;
// IMPORTAÇÕES JAVA
import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Conexao extends Thread {
    
    private String endereco;
    private int porta;
    
    private Socket conexao;
    public IComunicadorServidor comunicador; 
    private ComunicadorCliente comunicadorCliente;
    
    private Usuario cliente;

    public Conexao(String endereco, int porta){
        this.endereco = endereco;
        this.porta = porta;
    }
    
    public Usuario getCliente(){ return cliente; }
    public void setCliente(Usuario cliente){ this.cliente = cliente; }
    public String getEndereco(){ return endereco; }
    public int getPorta(){ return porta; }
    public boolean getStatus(){ return !conexao.isClosed() && conexao.isConnected(); }
    
    public void conectar() throws IOException, NotBoundException{
        conexao = new Socket(endereco, porta); // cria o socket
        comunicadorCliente = new ComunicadorCliente();
        // COMUNICADOR SERVIDOR (USADO PELO CLIENTE)
        Registry r = LocateRegistry.getRegistry(endereco, porta + 1);
        comunicador = (IComunicadorServidor) r.lookup("ComunicadorServidor");  // procura o comunicador no servidor
    }
    
    public void registrarCliente() throws RemoteException{
        comunicador.registrarCliente((IComunicadorCliente) UnicastRemoteObject.exportObject(comunicadorCliente, 0));
    }
    
    @Override
    public void run(){
    }
}
