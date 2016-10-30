package cliente.aplicacao;

import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import java.io.IOException;
import java.util.ArrayList;

public class Transmissao {

    public void transmitir(ArrayList<Usuario> usuarios, Mensagem mensagem) throws IOException{
        for (Usuario usuario : usuarios) {
            if(usuario.getId() != mensagem.getIdOrigem()){
                mensagem.setIdDestino(usuario.getId());
                mensagem.setIdMensagem(Principal.frmPrincipal.conexao.recuperarUltimaId(mensagem.getIdOrigem(), mensagem.getIdDestino(), mensagem.getDestinoTipo()));
                Principal.frmPrincipal.conexao.enviarMensagem(mensagem);   
            }
        }
    }
    
}
