package cliente.aplicacao;

import java.io.IOException;
import java.net.Socket;

public class ConexaoCliente {
    
    private String endereco;
    private int porta;
    private Socket conexao;
    
    public ConexaoCliente(String endereco, int porta){
        this.endereco = endereco;
        this.porta = porta;
    }
    
    public void criarConexao() throws IOException{
        conexao = new Socket(endereco, porta);
    }
    
    public boolean getStatus(){
        return conexao.isConnected();
    }
    
    public void fecharConexao() throws IOException{
        conexao.close();
        
    }
}
