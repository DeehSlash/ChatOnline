package cliente.aplicacao;

import compartilhado.modelo.UsuarioAutenticacao;
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
    public boolean getStatus(){ return conexao.isConnected(); }
    
    public void conectar() throws IOException{
        conexao = new Socket(endereco, porta);
    }
    
    public void desconectar() throws IOException{
        saida.close();
        conexao.close();
    }
    
    public void atualizarListaUsuarios() throws IOException, ClassNotFoundException{
        entradaObjeto = new ObjectInputStream(conexao.getInputStream());
        Principal.usuarios = (ArrayList)entradaObjeto.readObject();
        Principal.frmPrincipal.carregarLista();
    }
    
    public boolean autenticarUsuario(UsuarioAutenticacao usuario) throws IOException{ // serve tanto para cadastro quanto para autenticação
        saidaObjeto = new ObjectOutputStream(conexao.getOutputStream());
        saidaObjeto.writeObject(usuario);
        entradaDados = new DataInputStream(conexao.getInputStream());
        idCliente = entradaDados.readInt(); // recupera o id do usuário
        return entradaDados.readBoolean();
    }
    
    public void enviarMensagem(String msg) throws IOException{
        saida = new PrintStream(conexao.getOutputStream());
        saida.println(msg);
    }
    
    public void enviarIdCliente() throws IOException{
        saida = new PrintStream(conexao.getOutputStream());
        saida.println(Integer.toString(idCliente));
    }
    
    @Override
    public void run(){
        try{
            atualizarListaUsuarios();
            int comando;
            while(!conexao.isClosed()){
                entradaDados = new DataInputStream(conexao.getInputStream());
                comando = entradaDados.readInt();
                switch(comando){
                    case 0: // caso mensagem recebida
                        break;
                    case 1: // caso atualização da lista de usuários
                        atualizarListaUsuarios();
                        break;
                }
            }
        } catch (ClassNotFoundException | IOException ex){
            ex.printStackTrace();
        }
    }
}
