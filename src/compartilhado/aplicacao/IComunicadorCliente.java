package compartilhado.aplicacao;

import java.rmi.Remote;
import java.rmi.RemoteException;
import compartilhado.modelo.Mensagem;

public interface IComunicadorCliente extends Remote {

    public boolean receberMensagem(Mensagem mensagem) throws RemoteException;
    
}
