package cliente.aplicacao;

import cliente.frames.FrameConversa;
import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.text.BadLocationException;

public class Transmissao {
    
    public static void transmitir(ArrayList<Usuario> usuarios, Mensagem mensagem) throws IOException, BadLocationException{
        ArrayList<Mensagem> mensagens = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if(usuario.getId() != mensagem.getIdOrigem()){
                mensagem.setIdDestino(usuario.getId());
                for (FrameConversa conversa : Principal.frmPrincipal.conversas) {
                    if(conversa.getIdDestino() == usuario.getId()){
                        mensagem.setIdMensagem(conversa.mensagens.size() + 1);
                        conversa.mensagens.add(mensagem);
                        conversa.escreverMensagem(mensagem);
                    }
                }
                Principal.frmPrincipal.conexao.enviarMensagem(mensagem); 
            }
        }
    }
    
}
