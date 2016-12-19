package cliente.aplicacao;

import cliente.frames.FrameConversa;
import compartilhado.aplicacao.IComunicadorCliente;
import compartilhado.modelo.Grupo;
import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import java.awt.Point;
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
    public boolean iniciarJogo(int idGrupo) throws RemoteException{
        for (FrameConversa conversa : Principal.frmPrincipal.conversas) {
            if(conversa.getDestino() == idGrupo && conversa.getTipoDestino() == 'G'){
                conversa.setVisible(true);
                conversa.iniciarJogo();
                return true;
            }
        }
        return false;
    }
    
    
    @Override
    public boolean atualizarPosicaoJogo(int idGrupo, int x, int y, String time) throws RemoteException {
        for (FrameConversa conversa : Principal.frmPrincipal.conversas) {
            if(conversa.getDestino() == idGrupo && conversa.getTipoDestino() == 'G'){
                conversa.frameJogo.atualizarPosicao(new Point(x, y), time);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean atualizarVidaJogo(int idGrupo, int vida, String time) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean criarTiroJogo(int idGrupo, String time) throws RemoteException {
        for (FrameConversa conversa : Principal.frmPrincipal.conversas) {
            if(conversa.getDestino() == idGrupo && conversa.getTipoDestino() == 'G'){
                conversa.frameJogo.criarTiro(time);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean receberInformacao(int idGrupo, String informacao) throws RemoteException {
        for (FrameConversa conversa : Principal.frmPrincipal.conversas) {
            if(conversa.getDestino() == idGrupo && conversa.getTipoDestino() == 'G'){
                conversa.escreverInformacao(informacao);
                conversa.descerScroll();
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
