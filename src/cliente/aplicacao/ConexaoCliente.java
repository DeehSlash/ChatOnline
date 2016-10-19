package cliente.aplicacao;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ConexaoCliente extends Thread {
    
    private String endereco;
    private int porta;
    private Socket conexao;
    private int idCliente;
    PrintStream saida;

    public ConexaoCliente(String endereco, int porta, int idCliente){
        this.endereco = endereco;
        this.porta = porta;
        this.idCliente = idCliente;
    }
    
    public int getIdCliente(){ return idCliente; }
    
    public void criarConexao() throws IOException{
        conexao = new Socket(endereco, porta);
    }
    
    public boolean getStatus(){
        return conexao.isConnected();
    }
    
    public void fecharConexao() throws IOException{
        saida.close();
        conexao.close();
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
