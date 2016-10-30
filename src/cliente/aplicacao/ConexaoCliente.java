package cliente.aplicacao;

import compartilhado.modelo.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ConexaoCliente extends Thread {
    
    private String endereco;
    private int porta;
    private Socket conexao;
    private int idCliente;
    private ObjectInputStream entradaObjeto;
    private ObjectOutputStream saidaObjeto;
    private DataInputStream entradaDado;
    private DataOutputStream saidaDado;

    public ConexaoCliente(String endereco, int porta){
        this.endereco = endereco;
        this.porta = porta;
    }
    
    public int getIdCliente(){ return idCliente; }
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
    
    public void conectar() throws IOException{
        conexao = new Socket(endereco, porta);
    }
    
    public void desconectar() throws IOException{
        getSaidaDado().writeInt(2); // envia pedido para desconectar
    }
    
    public void atualizarListaUsuarios() throws IOException, ClassNotFoundException{
        Principal.usuarios = (ArrayList<Usuario>)getEntradaObjeto().readObject();
    }
    
    public int autenticarUsuario(UsuarioAutenticacao usuario, boolean cadastro) throws IOException{ // serve tanto para cadastro quanto para autenticação
        getSaidaDado().writeBoolean(cadastro); // envia para o servidor se é cadastro ou login
        getSaidaObjeto().writeObject(usuario); // envia o usuário de autenticação para o servidor
        int status = getEntradaDado().readInt(); // recebe do servidor o status da autenticação
        if(status == 3)
            idCliente = getEntradaDado().readInt(); // recupera o id do usuário
        return status;
    }
    
    public void alterarUsuario(Usuario usuario) throws IOException{
        getSaidaDado().writeInt(1);
        getSaidaObjeto().writeObject(usuario);
    }
    
    private void receberMensagem() throws IOException, ClassNotFoundException{
        Mensagem mensagem = (Mensagem)getEntradaObjeto().readObject();
        Principal.frmPrincipal.receberMensagem(mensagem);
    }
    
    public void enviarMensagem(Mensagem mensagem) throws IOException{
        getSaidaDado().writeInt(0); // envia para o servidor comando 0 (enviar mensagem)
        getSaidaObjeto().writeObject(mensagem); // envia para o servidor a mensagem
    }
    
    @Override
    public void run(){
        try{
            int comando;
            while(!conexao.isClosed()){
                comando = getEntradaDado().readInt();
                switch(comando){
                    case 0: // caso mensagem recebida
                        receberMensagem();
                        break;
                    case 1: // caso atualização da lista de usuários
                        Principal.frmPrincipal.carregarLista();
                        Principal.frmPrincipal.atualizarConversas();
                        break;
                    case 2: // encerrar conexão
                        conexao.close();
                        break;
                    default:
                        desconectar();
                        break;
                }
            }
        } catch (ClassNotFoundException | IOException ex){
            ex.printStackTrace();
        }
    }
}
