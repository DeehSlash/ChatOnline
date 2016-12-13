package cliente.aplicacao;

import compartilhado.aplicacao.IComunicadorServidor;
import compartilhado.modelo.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Conexao extends Thread {
    
    private String endereco;
    private int porta;
    private Socket conexao;
    public IComunicadorServidor comunicador; 
    private ComunicadorCliente comunicadorCliente;
    private Usuario cliente;
    private ObjectInputStream entradaObjeto;
    private ObjectOutputStream saidaObjeto;
    private DataInputStream entradaDado;
    private DataOutputStream saidaDado;

    public Conexao(String endereco, int porta){
        this.endereco = endereco;
        this.porta = porta;
    }
    
    public Usuario getCliente(){ return cliente; }
    public void setCliente(Usuario cliente){ this.cliente = cliente; }
    public String getEndereco(){ return endereco; }
    public int getPorta(){ return porta; }
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
    
    public void conectar() throws IOException, NotBoundException{
        conexao = new Socket(endereco, porta); // cria o socket
        // COMUNICADOR CLIENTE (USADO PELO SERVIDOR)
        //comunicadorCliente = new ComunicadorCliente(); // cria um novo comunicador
        //LocateRegistry.createRegistry(8082); // inicia o registro RMI na porta 8082
        //Naming.rebind("//localhost:8082/ComunicadorCliente", comunicadorCliente); // vincula o objeto comunicador ao endereço RMI
        // COMUNICADOR SERVIDOR (USADO PELO CLIENTE)
        Registry r = LocateRegistry.getRegistry(endereco, porta + 1);
        comunicador = (IComunicadorServidor) r.lookup("ComunicadorServidor");  // procura o comunicador no servidor
    }
    
    public void alterarUsuario(Usuario usuario) throws IOException{
        getSaidaDado().writeInt(1);
        getSaidaObjeto().writeObject(usuario);
    }

    public void criarGrupo(Grupo grupo) throws IOException{
        getSaidaDado().writeInt(3); // envia para o servidor comando 3 (criar grupo)
        getSaidaObjeto().writeObject(grupo); // envia para o servidor o grpo
    }
    
    public int receberIdGrupoDisponivel() throws IOException{
        getSaidaDado().writeInt(4); // envia para o servidor comando 4 (recuperar id de grupo disponível)
        int id = getEntradaDado().readInt(); // recebe do servidor a id
        return id;
    }
    
    @Override
    public void run(){
    }
}
