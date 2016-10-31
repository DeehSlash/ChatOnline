package cliente.aplicacao;

import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import java.io.IOException;
import java.util.ArrayList;

public class Transmissao {
    
    public static void transmitir(ArrayList<Usuario> usuarios, Mensagem mensagem) throws IOException{
        ArrayList<Mensagem> mensagens = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            System.out.println("Usuario " + usuario.getId() + ": " + usuario.getUsuario());
            if(usuario.getId() != mensagem.getIdOrigem()){
                System.out.println("Mudou id da mensagem para " + usuario.getId());
                mensagem.setIdDestino(usuario.getId());
                System.out.println("Chamou recuperarUltimaId");
                //mensagem.setIdMensagem(Principal.frmPrincipal.conexao.recuperarUltimaId());
                System.out.println("Recebeu ID " + mensagem.getIdMensagem());
  
            }
        }
        System.out.println("Enviando para conexão");
        for (Mensagem msg : mensagens) {
            Principal.frmPrincipal.conexao.enviarMensagem(msg); 
        }
    }
    
}
