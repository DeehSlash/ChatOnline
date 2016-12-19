package cliente.aplicacao;

import cliente.frames.FrameConversa;
import compartilhado.aplicacao.IComunicadorCliente;
import compartilhado.modelo.Grupo;
import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ComunicadorCliente implements IComunicadorCliente {

    public ComunicadorCliente() throws RemoteException {}
    
    @Override
    public boolean receberMensagem(Mensagem mensagem) throws RemoteException {
        Principal.frmPrincipal.receberMensagem(mensagem);
        return true;
    }

    @Override
    public boolean receberInformacao(int idGrupo, String informacao) throws RemoteException {
        for (FrameConversa conversa : Principal.frmPrincipal.conversas) {
            if(conversa.getDestino() == idGrupo){
                conversa.escreverInformacao(informacao);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean atualizarLista(ArrayList<Usuario> usuarios, ArrayList<Grupo> grupos) throws RemoteException {
        Principal.usuarios = usuarios;
        Principal.grupos = grupos;
        Principal.frmPrincipal.carregarLista(true);
        Principal.frmPrincipal.atualizarConversas();
        return true;
    }

}
