package compartilhado.aplicacao;

import compartilhado.modelo.*;

public class MensagemFactory {

    public static Mensagem criarMensagem(char tipo){
        switch(tipo){
            case 'T':
                return new MensagemTexto();
            case 'I':
                return new MensagemImagem();
            case 'V':
                return new MensagemVideo();
            case 'A':
                return new MensagemArquivo();
        }
        return null;
    }
}
