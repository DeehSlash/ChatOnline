package compartilhado.aplicacao;

import compartilhado.modelo.Grupo;
import java.rmi.Remote;
import java.rmi.RemoteException;
import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import java.util.ArrayList;

public interface IComunicadorCliente extends Remote {

    public boolean receberMensagem(Mensagem mensagem) throws RemoteException;
    
    public boolean iniciarJogo(int idGrupo) throws RemoteException;
    public boolean atualizarPosicaoJogo(int idGrupo, int x, int y, String time) throws RemoteException;
    public boolean atualizarVidaJogo(int idGrupo, int vida, String time) throws RemoteException;
    public boolean criarTiroJogo(int idGrupo, String time) throws RemoteException;
    public boolean receberInformacao(int idGrupo, String informacao) throws RemoteException;
    
    public boolean atualizarLista(ArrayList<Usuario> usuarios, ArrayList<Grupo> grupos) throws RemoteException;
    
}
