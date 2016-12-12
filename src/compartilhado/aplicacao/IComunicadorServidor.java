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
    public Usuario getUsuarioPorNome(String nome) throws RemoteException;
    
    public int criarGrupo(Grupo grupo) throws RemoteException;
    public int alterarGrupo(Grupo grupo) throws RemoteException;
    
    public ArrayList<Usuario> recuperarListaUsuarios() throws RemoteException;
    public ArrayList<Grupo> recuperarListaGrupos() throws RemoteException;
    public ArrayList<Mensagem> recuperarListaMensagens(int idOrigem, int idDestino) throws RemoteException;
    
    public boolean enviarMensagem(Mensagem mensagem) throws RemoteException;
    
}