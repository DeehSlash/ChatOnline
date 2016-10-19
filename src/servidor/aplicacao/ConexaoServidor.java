package servidor.aplicacao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConexaoServidor extends Thread {
    
    Socket cliente;
    int idCliente;
    
    public ConexaoServidor(int porta, Socket cliente){
        this.cliente = cliente;
    }
    
    public boolean getStatus(){
        return cliente.isConnected();
    }
    
    public void fecharConexao() throws IOException{
        cliente.close();
    }
    
    @Override
    public void run(){
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
}
