package compartilhado.aplicacao;

import compartilhado.modelo.Grupo;
import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import compartilhado.modelo.UsuarioAutenticacao;
import java.rmi.*;
import java.util.ArrayList;

public interface IComunicadorServidor extends Remote{
    
    public int autenticarUsuario(UsuarioAutenticacao autenticacao) throws RemoteException;
    public int cadastrarUsuario(UsuarioAutenticacao autenticacao) throws RemoteException;
    public int alterarUsuario(Usuario usuario) throws RemoteException;
    
    public int criarGrupo(Grupo grupo) throws RemoteException;
    public int alterarGrupo(Grupo grupo) throws RemoteException;
    
    public ArrayList recuperarListaUsuarios() throws RemoteException;
    public ArrayList recuperarListaGrupos() throws RemoteException;
    
    public int enviarMensagem(Mensagem mensagem) throws RemoteException;
    public ArrayList recuperarListaMensagens(int idOrigem, int idDestino) throws RemoteException;
    
}
