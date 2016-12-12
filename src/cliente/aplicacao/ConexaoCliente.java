package cliente.aplicacao;

import compartilhado.aplicacao.IComunicadorServidor;
import compartilhado.modelo.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

public class ConexaoCliente extends Thread {
    
    private String endereco;
    private int porta;
    private Socket conexao;
    public IComunicadorServidor comunicador; 
    public ComunicadorCliente comunicadorCliente;
    private Usuario cliente;
    private ObjectInputStream entradaObjeto;
    private ObjectOutputStream saidaObjeto;
    private DataInputStream entradaDado;
    private DataOutputStream saidaDado;

    public ConexaoCliente(String endereco, int porta){
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
        conexao = new Socket(endereco, porta);
        comunicadorCliente = new ComunicadorCliente();
        LocateRegistry.createRegistry(8082);
        Naming.rebind("//127.0.0.1:8082/ComunicadorCliente", comunicadorCliente);
        comunicador = (IComunicadorServidor) Naming.lookup("//" + endereco + ":8081/ComunicadorServidor"); 
    }
    
    public void desconectar() throws IOException{
        getSaidaDado().writeInt(2); // envia pedido para desconectar
    }
    
    public void atualizarListaUsuarios() throws IOException, ClassNotFoundException{
        Principal.usuarios = (ArrayList<Usuario>) getEntradaObjeto().readObject();
    }
    
    public ArrayList receberListaMensagens(int idOrigem, int idDestino) throws IOException, ClassNotFoundException{
        getSaidaDado().writeInt(idOrigem);
        getSaidaDado().writeInt(idDestino);
        ArrayList<Mensagem> mensagens = (ArrayList<Mensagem>) getEntradaObjeto().readObject();
        return mensagens;
    }
    
    public int autenticarUsuario(UsuarioAutenticacao usuario, boolean cadastro) throws IOException{ // serve tanto para cadastro quanto para autenticação
        getSaidaDado().writeBoolean(cadastro); // envia para o servidor se é cadastro ou login
        getSaidaObjeto().writeObject(usuario); // envia o usuário de autenticação para o servidor
        int status = getEntradaDado().readInt(); // recebe do servidor o status da autenticação
        //if(status == 3)
            //idCliente = getEntradaDado().readInt(); // recupera o id do usuário
        return status;
    }
    
    public void alterarUsuario(Usuario usuario) throws IOException{
        getSaidaDado().writeInt(1);
        getSaidaObjeto().writeObject(usuario);
    }
    
    private void receberMensagem() throws IOException, ClassNotFoundException{
        Mensagem mensagem = (Mensagem) getEntradaObjeto().readObject();
        Principal.frmPrincipal.receberMensagem(mensagem);
    }
    
    public void enviarMensagem(Mensagem mensagem) throws IOException{
        getSaidaDado().writeInt(0); // envia para o servidor comando 0 (enviar mensagem)
        getSaidaObjeto().writeObject(mensagem); // envia para o servidor a mensagem
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
