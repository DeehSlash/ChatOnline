package servidor.aplicacao;

import compartilhado.modelo.Usuario;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Jogo {

    private final int idGrupo;
    private final ArrayList<Usuario> timeAzul;
    private final ArrayList<Usuario> timeVermelho;
    
    private int vidaAzul;
    private int vidaVermelho;
    
    public Jogo(int idGrupo, ArrayList<Usuario> timeAzul, ArrayList<Usuario> timeVermelho){
        this.idGrupo = idGrupo;
        this.timeAzul = timeAzul;
        this.timeVermelho = timeVermelho;
        inicializar();
    }
    
    public void inicializar(){
        vidaAzul = 50;
        vidaVermelho = 50;
        try {
            enviarInformacao("Iniciando jogo...");
            String azul = "Time azul: ";
            for (Usuario usuario : timeAzul) {
                azul += usuario.getUsuario() + ", ";
            }
            azul = azul.substring(0, azul.lastIndexOf(","));
            String vermelho = "Time vermelho: ";
            for (Usuario usuario : timeVermelho) {
                vermelho += usuario.getUsuario() + ", ";
            }
            vermelho = vermelho.substring(0, vermelho.lastIndexOf(","));
            enviarInformacao(azul);
            enviarInformacao(vermelho);
            enviarInformacao("Comandos:\n.cima\n.baixo\n.esquerda\n.direita\n.atirar");
            enviarInformacao("O jogo foi iniciado");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Principal.frmPrincipal.enviarLog("Falha ao enviar informação para grupo " + idGrupo + ":" + ex.getMessage());
        }
    }
    
    public void enviarInformacao(String informacao) throws RemoteException{
        for (Usuario usuario : timeAzul) {
            if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null)
                Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.receberInformacao(idGrupo, informacao);
        }
        for (Usuario usuario : timeVermelho) {
            if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null)
                Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.receberInformacao(idGrupo, informacao);
        }
    }
    
}
