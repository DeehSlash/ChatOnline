package cliente.aplicacao;

import compartilhado.modelo.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class ConexaoCliente extends Thread {
    
    private String endereco;
    private int porta;
    private Socket conexao;
    private int idCliente;
    private PrintStream saida;
    private ObjectInputStream entradaObjeto;
    private ObjectOutputStream saidaObjeto;
    private DataInputStream entradaDados;
    private DataOutputStream saidaDados;

    public ConexaoCliente(String endereco, int porta){
        this.endereco = endereco;
        this.porta = porta;
    }
    
    public int getIdCliente(){ return idCliente; }
    public String getEndereco(){ return endereco; }
    public int getPorta(){ return porta; }
    public boolean getStatus(){ return !conexao.isClosed() && conexao.isConnected(); }
    
    public void conectar() throws IOException{
        conexao = new Socket(endereco, porta);
    }
    
    public void desconectar() throws IOException{
        conexao.close();
    }
    
    public void atualizarListaUsuarios() throws IOException, ClassNotFoundException{
        entradaObjeto = new ObjectInputStream(conexao.getInputStream());
        Principal.usuarios = (ArrayList<Usuario>)entradaObjeto.readObject();
    }
    
    public int autenticarUsuario(UsuarioAutenticacao usuario, boolean cadastro) throws IOException{ // serve tanto para cadastro quanto para autenticação
        entradaDados = new DataInputStream(conexao.getInputStream());
        saidaDados = new DataOutputStream(conexao.getOutputStream());
        saidaObjeto = new ObjectOutputStream(conexao.getOutputStream());
        saidaDados.writeBoolean(cadastro); // envia para o servidor se é cadastro ou login
        saidaObjeto.writeObject(usuario); // envia o usuário de autenticação para o servidor
        int status = entradaDados.readInt(); // recebe do servidor o status da autenticação
        if(status == 3)
            idCliente = entradaDados.readInt(); // recupera o id do usuário
        return status;
    }
    
    private void receberMensagem() throws IOException, ClassNotFoundException{
        entradaObjeto = new ObjectInputStream(conexao.getInputStream());
        Mensagem mensagem = (Mensagem)entradaObjeto.readObject();
    }
    
    public void enviarMensagem(String msg) throws IOException{
        saida = new PrintStream(conexao.getOutputStream());
        saida.println(msg);
    }
    
    @Override
    public void run(){
        try{
            int comando;
            while(!conexao.isClosed()){
                entradaDados = new DataInputStream(conexao.getInputStream());
                comando = entradaDados.readInt();
                switch(comando){
                    case 0: // caso mensagem recebida
                        break;
                    case 1: // caso atualização da lista de usuários
                        Principal.frmPrincipal.carregarLista();
                        break;
                    default:
                        desconectar();
                        break;
                }
            }
        } catch (/*ClassNotFoundException |*/ IOException ex){
            ex.printStackTrace();
        }
    }
}
