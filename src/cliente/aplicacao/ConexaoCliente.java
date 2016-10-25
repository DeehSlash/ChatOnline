package cliente.aplicacao;

import compartilhado.modelo.*;
import java.io.DataInputStream;
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
    
    public boolean autenticarUsuario(UsuarioAutenticacao usuario) throws IOException{ // serve tanto para cadastro quanto para autenticação
        saidaObjeto = new ObjectOutputStream(conexao.getOutputStream());
        saidaObjeto.writeObject(usuario); // envia o usuário de autenticação para o servidor
        entradaDados = new DataInputStream(conexao.getInputStream());
        boolean autenticou = entradaDados.readBoolean(); // recebe do servidor se a autenticação falhou ou funcionou
        if(autenticou)
            idCliente = entradaDados.readInt(); // recupera o id do usuário
        return autenticou;
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
