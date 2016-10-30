package servidor.aplicacao;

import compartilhado.modelo.*;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.swing.ImageIcon;

public class ConexaoServidor extends Thread {
    
    private final Socket conexao;
    private int idCliente;
    private BufferedReader entradaString;
    private ObjectInputStream entradaObjeto;
    private ObjectOutputStream saidaObjeto;
    private DataInputStream entradaDado;
    private DataOutputStream saidaDado;
    
    public ConexaoServidor(int porta, Socket cliente) throws IOException{
        this.conexao = cliente;
    }
    
    public int getIdCliente(){ return this.idCliente; }
    private void setIdCliente(int id) throws IOException{ this.idCliente = id; } 
    public boolean getStatus(){ return !conexao.isClosed() && conexao.isConnected(); }
    
    private ObjectInputStream getEntradaObjeto() throws IOException{
        return entradaObjeto = new ObjectInputStream(conexao.getInputStream());
    }
    private ObjectOutputStream getSaidaObjeto() throws IOException{
        return saidaObjeto = new ObjectOutputStream(conexao.getOutputStream());
    }
    
    private DataInputStream getEntradaDado() throws IOException{
        return entradaDado = new DataInputStream(conexao.getInputStream());
    }
    
    private DataOutputStream getSaidaDado() throws IOException{
        return saidaDado = new DataOutputStream(conexao.getOutputStream());
    }
    
    public void fecharConexao() throws IOException{
        Principal.frmPrincipal.enviarLog("Usuário " + Principal.usuarios.get(idCliente - 1).getUsuario() + " (" + idCliente + ") se desconectou");
        Principal.frmPrincipal.alterarUsuarios(false);
        int i = 0;
        for (ConexaoServidor conexao : Principal.conexoes) {
            if(conexao.getIdCliente() == getIdCliente())
                break;
            i++;
        }
        Principal.conexoes.remove(i);
        setOnline(false);
        atualizarListaUsuarios();
        getSaidaDado().writeInt(2);
        conexao.close();
    }
    
    private void atualizarListaUsuarios() throws IOException{
        for (ConexaoServidor conexao : Principal.conexoes) {
            if(conexao.getIdCliente() != getIdCliente())
                conexao.enviarListaUsuarios(true); // envia a lista atualizada para o usuário
        }
    }
    
    private void enviarListaUsuarios(boolean loop) throws IOException{
        if(loop){ // se estiver no loop esperando um comando
            getSaidaDado().writeInt(1); // envia comando 1 para o cliente (preparação para atualizar a lista de usuários)
        } // se ele não estiver no loop, automaticamente vai estar esperando só a lista
        getSaidaObjeto().writeObject(Principal.usuarios); // envia a lista de usuários para o cliente
    }
    
    private void setOnline(boolean online){
        Principal.usuarios.get(idCliente - 1).setOnline(online);
    }
    
    private int autenticarUsuario() throws IOException, SQLException, ClassNotFoundException, URISyntaxException{
        boolean existe = false, online = false , cadastro;
        int status = -1;
        cadastro = getEntradaDado().readBoolean(); // recebe se é um cadastro
        UsuarioAutenticacao usuario = (UsuarioAutenticacao)getEntradaObjeto().readObject(); // recebe o objeto de autenticação do cliente
        for (Usuario u : Principal.usuarios) { // verifica se o usuário já existe na lista de usuários
            if(u.getUsuario().equals(usuario.getUsuario())){
                existe = true;
                setIdCliente(u.getId());
                if(u.isOnline())
                    online = true;
            }
        }
        if(!cadastro) // se não for cadastro, faz o login
            if(online)
                status = 4;
            else
                status = Principal.gerenciador.autenticarUsuario(usuario);
        else{ // se for cadastro, primeiro cadastra e então faz o login
            if(!existe){ // verifica se o usuário não existe já
                if(Principal.gerenciador.cadastrarUsuario(usuario)){ // se o cadastro funcionou
                    Principal.frmPrincipal.enviarLog("Usuário " + usuario.getUsuario() + " cadastrado"); // envia o log
                    ClassLoader classLoader = getClass().getClassLoader();
                    ImageIcon imagem = new ImageIcon(getClass().getResource("/compartilhado/imagens/usuario.png"));
                    Image foto = compartilhado.aplicacao.Foto.redimensionarFoto(imagem.getImage(), 50);
                    Principal.usuarios.add(new Usuario(Principal.usuarios.size() + 1, usuario.getUsuario(), new ImageIcon(foto))); // adiciona na lista de usuários
                    status = Principal.gerenciador.autenticarUsuario(usuario); // tenta autenticar
                    setIdCliente(Principal.usuarios.size()); // define a id desse usuário
                }
            }else // caso usuário exista, retorna 2: usuário já existe e não pode ser cadastrado
                status = 2;
        }
        getSaidaDado().writeInt(status); // envia para o cliente o status da autenticação
        if(status == 3){ // se status for 3 (autenticou)
            getSaidaDado().writeInt(idCliente); // envia a id do cliente
            Principal.frmPrincipal.enviarLog("Usuário " + usuario.getUsuario() + " (" + idCliente + ") se conectou");
            Principal.frmPrincipal.alterarUsuarios(true);
        }
        return status;
    }
    
    private void alterarUsuario() throws IOException, ClassNotFoundException, SQLException{
        Usuario usuario = (Usuario)getEntradaObjeto().readObject();
        Principal.gerenciador.alterarUsuario(usuario);
        Principal.usuarios.set(usuario.getId() - 1, usuario);
        atualizarListaUsuarios();
    }
    
    private void receberMensagem(Mensagem mensagem) throws IOException{
        getSaidaDado().writeInt(0); // envia comando 0 (receber mensagem)
        getSaidaObjeto().writeObject(mensagem); // envia a mensagem
    }
    
    private void enviarMensagem() throws IOException, ClassNotFoundException, SQLException{;
        Mensagem mensagem = (Mensagem)getEntradaObjeto().readObject();
        //Principal.gerenciador.enviarMensagem(mensagem);
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
                while(!conexao.isClosed()){
                    comando = getEntradaDado().readInt();
                    switch(comando){
                        case 0: // caso mensagem enviada
                            enviarMensagem();
                            break;
                        case 1: // caso usuário alterado
                            alterarUsuario();
                            break;
                        case 2: // caso encerrar conexão;
                            fecharConexao();
                            break;
                        default:
                            fecharConexao();
                            break;
                    }
                }
            }
        } catch (URISyntaxException | IOException | ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            Principal.frmPrincipal.enviarLog("Exceção: " + ex.getMessage());
        }
    }
}
