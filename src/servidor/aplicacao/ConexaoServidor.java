package servidor.aplicacao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConexaoServidor extends Thread {
    
    Socket cliente;
    int idCliente;
    BufferedReader entrada;
    
    public ConexaoServidor(int porta, Socket cliente) throws IOException{
        this.cliente = cliente;
        this.idCliente = setId();
        Principal.frmPrincipal.enviarLog("Cliente com ID " + idCliente + " se conectou");
    }
    
    private int setId() throws IOException{
        entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        return Integer.parseInt(entrada.readLine());
    }
    
    public boolean getStatus(){
        return cliente.isConnected();
    }
    
    public void fecharConexao() throws IOException{
        cliente.close();
        Principal.frmPrincipal.enviarLog("Cliente com ID " + idCliente + " se desconectou");
        Principal.frmPrincipal.alterarUsuarios(false);
    }
    
    @Override
    public void run(){
        String msg;
        while(!cliente.isClosed()){
            try {
                entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                msg = entrada.readLine();
                if(msg != null)
                    System.out.println(idCliente + ": " + msg);
                else
                    fecharConexao();
            } catch (IOException ex) {
                ex.printStackTrace();
                Principal.frmPrincipal.enviarLog("Exceção: " + ex.getMessage());
            }
        }
    }
}
