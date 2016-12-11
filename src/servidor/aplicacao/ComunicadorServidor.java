package servidor.aplicacao;

import compartilhado.aplicacao.IComunicadorServidor;
import compartilhado.modelo.Grupo;
import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import compartilhado.modelo.UsuarioAutenticacao;
import java.awt.Image;
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
            -1 - Erro padrão
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
                    return 1; // já está online, então retorna 1
            }
        }
        if(!existe)
            return 0;
        try {
            status = Principal.gerenciador.autenticarUsuario(autenticacao);
        } catch (SQLException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao autenticar usuário " + autenticacao.getUsuario() + ": " + ex.getMessage());
            return status;
        }
        if(status == 2){
            return status;
        }
        Principal.frmPrincipal.enviarLog("Usuário " + autenticacao.getUsuario() + " se conectou");
        Principal.frmPrincipal.alterarUsuarios(true);
        return status;
    }

    @Override
    public int cadastrarUsuario(UsuarioAutenticacao autenticacao) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList recuperarListaGrupos() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int enviarMensagem(Mensagem mensagem) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList recuperarListaMensagens(int idOrigem, int idDestino) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
