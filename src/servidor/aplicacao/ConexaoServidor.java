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
    
    private void setIdCliente(int id) throws IOException{
        this.idCliente = id;
    }
    
    public boolean getStatus(){
        return cliente.isConnected();
    }
    
    public void fecharConexao() throws IOException{
        cliente.close();
        Principal.frmPrincipal.enviarLog("Usuário " + Principal.usuarios.get(idCliente).getUsuario() + " (" + idCliente + ") se desconectou");
        Principal.frmPrincipal.alterarUsuarios(false);
        Principal.usuarios.remove(idCliente);
        atualizarListaUsuarios();
    }
    
    private void atualizarListaUsuarios() throws IOException{
        for (ConexaoServidor conexao : Principal.conexoes) {
            conexao.enviarListaUsuarios();
        }
    }
    
    private void enviarListaUsuarios() throws IOException{
        saidaObjeto = new ObjectOutputStream(cliente.getOutputStream());
        saidaObjeto.writeObject(Principal.usuarios);
    }
    
    private void alterarStatus(boolean online){
        Principal.usuarios.get(idCliente).setOnline(online);
    }
    
    private boolean autenticarUsuario() throws IOException, SQLException, ClassNotFoundException{
        boolean existe = false, autenticou = false;
        entradaObjeto = new ObjectInputStream(cliente.getInputStream());
        saidaDados = new DataOutputStream(cliente.getOutputStream());
        UsuarioAutenticacao usuario = (UsuarioAutenticacao)entradaObjeto.readObject(); // recebe o objeto do cliente
        System.out.println("Recebeu objeto do cliente");
        for (Usuario u : Principal.usuarios) { // verifica se o usuário já existe na lista de usuários
            if(u.getUsuario().equals(usuario.getUsuario()))
                existe = true;
                setIdCliente(u.getId());
        }
        if(existe){ // se existe, faz o login
            autenticou = Principal.gerenciador.autenticarUsuario(usuario);
        }else{ // se não existe, faz o cadastro
            Principal.gerenciador.cadastrarUsuario(usuario);
            Principal.frmPrincipal.enviarLog("Usuário " + usuario.getUsuario() + " cadastrado");
            Principal.usuarios.add(new Usuario(Principal.usuarios.size(), usuario.getUsuario(), null));
            autenticou = Principal.gerenciador.autenticarUsuario(usuario);
            setIdCliente(Principal.usuarios.size());
        }
        if(autenticou){
             Principal.frmPrincipal.enviarLog("Usuário " + usuario.getUsuario() + " (" + idCliente + ") se conectou");
             Principal.frmPrincipal.alterarUsuarios(true);
        }
        saidaDados.writeBoolean(autenticou); // envia para o cliente se a autenticação funcionou
        return autenticou;
    }
    
    @Override
    public void run(){
        int comando;
        try {
            if(autenticarUsuario()){
                alterarStatus(true);
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
