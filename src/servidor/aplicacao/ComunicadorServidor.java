package servidor.aplicacao;

import compartilhado.aplicacao.IComunicadorServidor;
import compartilhado.modelo.Grupo;
import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import compartilhado.modelo.UsuarioAutenticacao;
import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

public class ComunicadorServidor implements IComunicadorServidor {

    @Override
    public int autenticarUsuario(UsuarioAutenticacao autenticacao) throws RemoteException {
        /*  CÓDIGO DE RETORNO (STATUS)
            -1 - Erro genérico
            0 - Usuário não existe
            1 - Usuário já está online
            2 - Senha incorreta
            3 - Autenticou
        */
        boolean existe = false; // controle se usuário já existe
        int status = -1; // controle de status da autenticação
        for (Usuario u : Principal.usuarios) { // verifica se o usuário já existe na lista de usuários
            if(u.getUsuario().equals(autenticacao.getUsuario())){
                existe = true;
                //setIdCliente(u.getId());
                if(u.isOnline())
                    return 1; // se já está online, então retorna 1
            }
        }
        if(!existe)
            return 0; // se usuário não existe, então retorna 0
        try {
            status = Principal.gerenciador.autenticarUsuario(autenticacao); // tenta autenticar na db
        } catch (SQLException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao autenticar usuário " + autenticacao.getUsuario() + ": " + ex.getMessage());
            return status;
        }
        if(status == 2){
            return status; // se a senha está incorreta, retorna
        }
        Principal.frmPrincipal.enviarLog("Usuário " + autenticacao.getUsuario() + " se conectou"); // envia log de autenticação
        Principal.frmPrincipal.alterarUsuarios(true); // incrementa número de usuários online
        return status; // retorna status de autenticação
    }

    @Override
    public int cadastrarUsuario(UsuarioAutenticacao autenticacao) throws RemoteException {
        /*  CÓDIGO DE RETORNO (STATUS)
            -1 - Erro genérico
            0 - Usuário já existe
            1 - Cadastrou
        */
        int status = -1; // controle de status do cadastro
        boolean cadastro; // controle para retorno da db
        for (Usuario u : Principal.usuarios) { // verifica se o usuário já existe na lista de usuários
            if(u.getUsuario().equals(autenticacao.getUsuario())){
                return 0; // se usuário já existe, então retorna 0
            }
        }
        try {
            cadastro = Principal.gerenciador.cadastrarUsuario(autenticacao); // tenta cadastrar na db
        } catch (SQLException | IOException | URISyntaxException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao cadastrar usuário " + autenticacao.getUsuario() + ": " + ex.getMessage());
            return status;
        }
        if(!cadastro){
            return status; // se a db retornou falso, retorna o status
        }else{
            status = 1; // se não, status é 1 (cadastrou)
        }
        ImageIcon imagem = new ImageIcon(getClass().getResource("/compartilhado/imagens/usuario.png")); // cria uma ImageIcon com a foto padrão de usuário
        Image foto = compartilhado.aplicacao.Foto.redimensionarFoto(imagem.getImage(), 50, false); // redimensiona a imagem
        Principal.usuarios.add(new Usuario(Principal.usuarios.size() + 1, autenticacao.getUsuario(), new ImageIcon(foto))); // adiciona na lista de usuários
        Principal.frmPrincipal.enviarLog("Usuário " + autenticacao.getUsuario() + " se cadastrou"); // envia log de cadastro
        return status; // retorna o status
    }

    @Override
    public int alterarUsuario(Usuario usuario) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int criarGrupo(Grupo grupo) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int alterarGrupo(Grupo grupo) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList recuperarListaUsuarios() throws RemoteException {
        return Principal.usuarios; // retorna a lista de usuários
    }

    @Override
    public ArrayList recuperarListaGrupos() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean enviarMensagem(Mensagem mensagem) throws RemoteException {
        try {
            Principal.gerenciador.enviarMensagem(mensagem);
        } catch (SQLException | IOException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao enviar mensagem com origem " + mensagem.getIdOrigem() + " e destino " + mensagem.getIdDestino() + ": " + ex.getMessage());
            return false;
        }
        // implementar recebimento de mensagem pelo destino
        return true;
    }

    @Override
    public ArrayList recuperarListaMensagens(int idOrigem, int idDestino) throws RemoteException {
        ArrayList<Mensagem> mensagens = null;
        try {
            mensagens = Principal.gerenciador.getListaMensagens(idOrigem, idDestino);
        } catch (SQLException | IOException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao recuperar lista de mensagens com origem " + idOrigem + " e destino " + idDestino + ": " + ex.getMessage());
        }
        return mensagens;
    }

}
