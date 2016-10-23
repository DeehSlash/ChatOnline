package cliente.aplicacao;

import compartilhado.modelo.UsuarioAutenticacao;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ConexaoCliente extends Thread {
    
    private String endereco;
    private int porta;
    private Socket conexao;
    private int idCliente;
    private PrintStream saida;

    public ConexaoCliente(String endereco, int porta, int idCliente){
        this.endereco = endereco;
        this.porta = porta;
        this.idCliente = idCliente;
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
    
    public boolean autenticarUsuario(UsuarioAutenticacao usuario){
        return false; // falta implementação com a DB
    }
    
    public boolean cadastrarUsuario(UsuarioAutenticacao usuario){
        return false; // falta implementação com a DB
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
        
    }
}
