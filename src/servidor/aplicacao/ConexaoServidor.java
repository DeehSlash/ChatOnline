package servidor.aplicacao;

import compartilhado.modelo.Usuario;
import compartilhado.modelo.UsuarioAutenticacao;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class ConexaoServidor extends Thread {
    
    private Socket cliente;
    private int idCliente;
    private BufferedReader entrada;
    private ObjectInputStream entradaObjeto;
    private ObjectOutputStream saidaObjeto;
    private DataInputStream entradaDados;
    private DataOutputStream saidaDados;
    
    public ConexaoServidor(int porta, Socket cliente) throws IOException{
        this.cliente = cliente;
    }
    
    public int getIdCliente(){ return this.idCliente; }
    private void setIdCliente(int id) throws IOException{ this.idCliente = id; } 
    public boolean getStatus(){ return cliente.isConnected(); }
    
    public void fecharConexao() throws IOException{
        cliente.close();
        Principal.frmPrincipal.enviarLog("Usuário " + Principal.usuarios.get(idCliente).getUsuario() + " (" + idCliente + ") se desconectou");
        Principal.frmPrincipal.alterarUsuarios(false);
        Principal.usuarios.remove(idCliente);
        atualizarListaUsuarios();
    }
    
    private void atualizarListaUsuarios() throws IOException{
        for (ConexaoServidor conexao : Principal.conexoes) {
            if(conexao.getIdCliente() != getIdCliente())
                conexao.enviarListaUsuarios(true); // envia a lista atualizada para o usuário
        }
    }
    
    private void enviarListaUsuarios(boolean loop) throws IOException{
        if(loop){ // se estiver no loop esperando um comando
            saidaDados = new DataOutputStream(cliente.getOutputStream());
            saidaDados.writeInt(1); // envia comando 1 para o cliente (preparação para atualizar a lista de usuários)
        } // se ele não estiver no loop, automaticamente vai estar esperando só a lista
        saidaObjeto = new ObjectOutputStream(cliente.getOutputStream());
        saidaObjeto.writeObject(Principal.usuarios); // envia a lista de usuários para o cliente
    }
    
    private void setOnline(boolean online){
        Principal.usuarios.get(idCliente - 1).setOnline(online);
    }
    
    private boolean autenticarUsuario() throws IOException, SQLException, ClassNotFoundException{
        boolean existe = false, autenticou = false;
        entradaObjeto = new ObjectInputStream(cliente.getInputStream());
        saidaDados = new DataOutputStream(cliente.getOutputStream());
        UsuarioAutenticacao usuario = (UsuarioAutenticacao)entradaObjeto.readObject(); // recebe o objeto do cliente
        for (Usuario u : Principal.usuarios) { // verifica se o usuário já existe na lista de usuários
            if(u.getUsuario().equals(usuario.getUsuario())){
                existe = true;
                setIdCliente(u.getId());
            }
        }
        if(existe) // se existe, faz o login
            autenticou = Principal.gerenciador.autenticarUsuario(usuario);
        else{ // se não existe, faz o cadastro e então o login
            Principal.gerenciador.cadastrarUsuario(usuario);
            Principal.frmPrincipal.enviarLog("Usuário " + usuario.getUsuario() + " cadastrado");
            Principal.usuarios.add(new Usuario(Principal.usuarios.size(), usuario.getUsuario(), null));
            autenticou = Principal.gerenciador.autenticarUsuario(usuario);
            setIdCliente(Principal.usuarios.size());
        }
        saidaDados.writeBoolean(autenticou); // envia para o cliente se a autenticação funcionou
        if(autenticou){
            saidaDados.writeInt(idCliente); // envia a id do cliente
            Principal.frmPrincipal.enviarLog("Usuário " + usuario.getUsuario() + " (" + idCliente + ") se conectou");
            Principal.frmPrincipal.alterarUsuarios(true);
        }
        return autenticou;
    }
    
    @Override
    public void run(){
        int comando;
        try {
            if(autenticarUsuario()){
                setOnline(true);
                enviarListaUsuarios(false);
                atualizarListaUsuarios();
                while(!cliente.isClosed()){
                    entradaDados = new DataInputStream(cliente.getInputStream());
                    comando = entradaDados.readInt();
                    switch(comando){
                        case 0: // caso mensagem enviada
                            break;
                        case 1: 
                            break;
                        default:
                            fecharConexao();
                            break;
                    }
                }
            }else
                fecharConexao();
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            Principal.frmPrincipal.enviarLog("Exceção: " + ex.getMessage());
        }
    }
}
