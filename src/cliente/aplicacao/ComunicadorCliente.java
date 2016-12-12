package cliente.aplicacao;

import compartilhado.aplicacao.IComunicadorCliente;
import compartilhado.modelo.Mensagem;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ComunicadorCliente extends UnicastRemoteObject implements IComunicadorCliente {

    public ComunicadorCliente() throws RemoteException {}
    
    @Override
    public boolean receberMensagem(Mensagem mensagem) throws RemoteException {
        Principal.frmPrincipal.receberMensagem(mensagem);
        return true;
    }

}
