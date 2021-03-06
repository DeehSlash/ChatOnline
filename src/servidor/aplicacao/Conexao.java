package servidor.aplicacao;

// IMPORTAÇÕES DO PROJETO
import compartilhado.aplicacao.IComunicadorCliente;
import compartilhado.modelo.Grupo;
import java.io.DataOutputStream;
// IMPORTAÇÕES JAVA
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Conexao extends Thread {
    
    private int id; // id da conexão
    
    private final Socket cliente;
    public IComunicadorCliente comunicador;
    private ComunicadorServidor comunicadorServidor;
    
    private int idCliente;
    
    public Conexao(int id, Socket cliente){
        this.id = id;
        this.cliente = cliente;
    }
    
    public int getIdConexao(){ return id; }
    public void setIdConexao(int id){ this.id = id; }
    public int getIdCliente(){ return this.idCliente; }
    public void setIdCliente(int id){ this.idCliente = id; } 
    public boolean getStatus(){ return !cliente.isClosed() && cliente.isConnected(); }
    public void setComunicador(IComunicadorCliente comunicador){ this.comunicador = comunicador; };
    
    public void conectar() throws RemoteException, MalformedURLException, NotBoundException{
        // COMUNICADOR SERVIDOR (USADO PELO CLIENTE)
        comunicadorServidor = new ComunicadorServidor(id); // cria um novo comunicador
        Naming.rebind("rmi://localhost:" + (Principal.portaRMI) + "/ComunicadorServidor", comunicadorServidor); // vincula o objeto comunicador ao endereço RMI
        try {
            DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());
            saida.writeBoolean(true);
        } catch (IOException ex) {
            Principal.frmPrincipal.enviarLog("Exceção no OutputStream para cliente " + idCliente + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void desconectar() throws IOException{
        Principal.frmPrincipal.enviarLog("Usuário " + Principal.usuarios.get(idCliente - 1).getUsuario() + " (" + idCliente + ") se desconectou");
        Principal.frmPrincipal.alterarUsuarios(false); // decrementa a qtd de usuários online
        removerConexao();
        setOnline(false); // define o usuário como offline
        atualizarLista(); // atualiza a lista de usuários
        cliente.close(); // fecha a conexão
    }
    
    public void removerConexao(){
        int i = 0;
        for (Conexao conexao : Principal.conexoes) {
            if(conexao.getIdConexao() == getIdConexao())
                break;
            i++;
        }
        Principal.conexoes.remove(i);
    }
    
    public void atualizarLista() throws RemoteException{
        for (Conexao conexao : Principal.conexoes) { // para cada conexão
            if(conexao.getIdCliente() != getIdCliente()) // exceto a própria
                conexao.comunicador.atualizarLista(Principal.usuarios, conexao.getGrupos()); // envia a lista de usuários atualizada
        }
    }
    
    public boolean pertenceAoGrupo(int id){
        Grupo grupo = Principal.grupos.get(id - 1);
        for (int i = 0; i < 10; i++) {
            if(grupo.getMembros()[i] == getIdCliente())
                return true;
        }
        return false;
    }
    
    public ArrayList<Grupo> getGrupos(){
        ArrayList<Grupo> grupos = new ArrayList<>();
        for (Grupo grupo : Principal.grupos) {
            for (int i = 0; i < 10; i++) {
                if(grupo.getMembros()[i] == getIdCliente()){
                    grupos.add(grupo);
                    break;
                }
            }
        }
        return grupos;
    }
    
    private void setOnline(boolean online){
        Principal.usuarios.get(idCliente - 1).setOnline(online);
    }
    
    @Override
    public void run(){
    }
}
