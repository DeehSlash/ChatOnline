package servidor.aplicacao;

import java.io.IOException;
import java.net.ServerSocket;

public class Conexao {
    
    String endereco;
    int porta;
    ServerSocket conexao;
    
    public Conexao(String endereco, int porta){
        this.endereco = endereco;
        this.porta = porta;
    }
    
    public void criarConexao() throws IOException{
        conexao = new ServerSocket(porta);
    }
    
    public boolean getStatus(){
        return conexao.isBound();
    }
    
    public void fecharConexao() throws IOException{
        conexao.close();
    }
}
