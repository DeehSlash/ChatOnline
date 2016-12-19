package servidor.jogo;

import compartilhado.modelo.Usuario;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.ArrayList;
import servidor.aplicacao.Principal;


public class Jogo {

    private final int idGrupo;
    
    private final ArrayList<Usuario> timeAzul;
    private final ArrayList<Usuario> timeVermelho;
    
    private final int x;
    private final int y;
    private final int tamVeiculo;
    
    private Point posicaoAzul;
    private int rotacaoAzul;
    
    private Point posicaoVermelho;
    private int rotacaoVermelho;
    
    private int vidaAzul;
    private int vidaVermelho;
    
    public Jogo(int idGrupo, ArrayList<Usuario> timeAzul, ArrayList<Usuario> timeVermelho){
        this.idGrupo = idGrupo;
        this.timeAzul = timeAzul;
        this.timeVermelho = timeVermelho;
        x = 500;
        y = 500;
        tamVeiculo = 50;
        inicializar();
    }
    
    public int getIdGrupo() { return idGrupo; }
    
    public void inicializar(){
        
        posicaoAzul.x = (x / 2) - (tamVeiculo / 2); // coordenada inicial para x e y
        posicaoAzul.y = y - tamVeiculo;
        rotacaoAzul = 0;
        posicaoVermelho.x = (x / 2) - (tamVeiculo / 2);
        posicaoVermelho.y = 0;
        rotacaoVermelho = 180;
        vidaAzul = 5;
        vidaVermelho = 5;
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
            abrirFrames();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Principal.frmPrincipal.enviarLog("Falha ao enviar informação para grupo " + idGrupo + ":" + ex.getMessage());
        }
    }
    
    public void abrirFrames() throws RemoteException{
        for (Usuario usuario : timeAzul) {
            if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null)
                Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.iniciarJogo(idGrupo);
        }
        for (Usuario usuario : timeVermelho) {
            if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null)
                Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.iniciarJogo(idGrupo);
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
