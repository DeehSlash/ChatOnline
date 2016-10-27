package servidor.aplicacao;

import compartilhado.modelo.*;
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
    
    private int autenticarUsuario() throws IOException, SQLException, ClassNotFoundException{
        entradaObjeto = new ObjectInputStream(cliente.getInputStream());
        entradaDados = new DataInputStream(cliente.getInputStream());
        saidaDados = new DataOutputStream(cliente.getOutputStream());
        boolean existe = false, cadastro;
        int status = -1;
        cadastro = entradaDados.readBoolean(); // recebe se é um cadastro
        UsuarioAutenticacao usuario = (UsuarioAutenticacao)entradaObjeto.readObject(); // recebe o objeto de autenticação do cliente
        for (Usuario u : Principal.usuarios) { // verifica se o usuário já existe na lista de usuários
            if(u.getUsuario().equals(usuario.getUsuario())){
                existe = true;
                setIdCliente(u.getId());
            }
        }
        if(!cadastro) // se não for cadastro, faz o login
            status = Principal.gerenciador.autenticarUsuario(usuario);
        else{ // se for cadastro, primeiro cadastra e então faz o login
            if(!existe){ // verifica se o usuário não existe já
                if(Principal.gerenciador.cadastrarUsuario(usuario)){ // se o cadastro funcionou
                    Principal.frmPrincipal.enviarLog("Usuário " + usuario.getUsuario() + " cadastrado"); // envia o log
                    Principal.usuarios.add(new Usuario(Principal.usuarios.size(), usuario.getUsuario(), null)); // adiciona na lista de usuários
                    status = Principal.gerenciador.autenticarUsuario(usuario); // tenta autenticar
                    setIdCliente(Principal.usuarios.size()); // define a id desse usuário
                }
            }else // caso usuário exista, retorna 2: usuário já existe e não pode ser cadastrado
                status = 2;
        }
        saidaDados.writeInt(status); // envia para o cliente o status da autenticação
        if(status == 3){ // se status for 3 (autenticou)
            saidaDados.writeInt(idCliente); // envia a id do cliente
            Principal.frmPrincipal.enviarLog("Usuário " + usuario.getUsuario() + " (" + idCliente + ") se conectou");
            Principal.frmPrincipal.alterarUsuarios(true);
        }
        return status;
    }
    
    private void receberMensagem(Mensagem mensagem) throws IOException{
        saidaDados = new DataOutputStream(cliente.getOutputStream());
        saidaDados.writeInt(0);
        saidaObjeto = new ObjectOutputStream(cliente.getOutputStream());
        saidaObjeto.writeObject(mensagem);
    }
    
    private void enviarMensagem() throws IOException, ClassNotFoundException, SQLException{
        entradaObjeto = new ObjectInputStream(cliente.getInputStream());
        Mensagem mensagem = (Mensagem)entradaObjeto.readObject();
        Principal.gerenciador.enviarMensagem(mensagem);
        for (ConexaoServidor conexao : Principal.conexoes) {
            if(conexao.getIdCliente() == mensagem.getIdDestino()){
                conexao.receberMensagem(mensagem);
            }     
        }
    }
    
    @Override
    public void run(){
        int comando;
        try {
            if(autenticarUsuario() == 3){
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
            }
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            Principal.frmPrincipal.enviarLog("Exceção: " + ex.getMessage());
        }
    }
}
