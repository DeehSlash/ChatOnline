package cliente.frames;

import compartilhado.aplicacao.Imagem;
import compartilhado.aplicacao.MensagemBuilder;
import compartilhado.modelo.Grupo;
import compartilhado.modelo.Mensagem;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import cliente.aplicacao.Principal;
import cliente.jogo.FrameJogo;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.text.Style;

public class FrameConversa extends javax.swing.JFrame {

    private final int origem;
    private final int destino;
    private final char tipoDestino;
    
    private boolean carregado;
    private boolean notificar;
    
    private StyledDocument doc;
    
    private final MensagemBuilder mensagemBuilder;
    public ArrayList<Mensagem> mensagens;
    private char tipoMensagem;
    
    public FrameConversa(int origem, int destino, char tipoDestino) {
        initComponents();
        addListeners();
        this.origem = origem;
        this.destino = destino;
        this.tipoDestino = tipoDestino;
        carregado = false;
        notificar = false;
        mensagemBuilder = new MensagemBuilder(origem, destino, tipoDestino);
        mensagens = new ArrayList<>();
        tipoMensagem = 'T';
        if(tipoDestino == 'G')
            implementarMenu();
        carregarInfo();
    }
    
    public int getDestino(){ return destino; }
    public char getTipoDestino() { return tipoDestino; }
    public boolean getCarregado() { return carregado; }
    public boolean getNotificar() { return notificar; }
    
    public void setCarregado(boolean carregado) { this.carregado = carregado; }
    public void setNotificar(boolean notificar) { this.notificar = notificar; }
    
    private void addListeners(){
        
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                setVisible(false);
                Principal.frmPrincipal.requestFocus();
            }
        });
        
        btnEnviar.addActionListener((ActionEvent e) -> {
            enviarMensagem();
            txtMensagem.grabFocus();
        });
        
        txtMensagem.addActionListener((ActionEvent e) -> {
            enviarMensagem();
            txtMensagem.grabFocus();
        });
        
        lblTexto.addMouseListener(new MouseAdapter()  
        {  
            @Override
            public void mouseClicked(MouseEvent e)  
            {  
                lblImagem.setForeground(Color.black);
                lblTexto.setForeground(new Color(31, 167, 9));
                txtMensagem.setEnabled(true);
                txtMensagem.setText("");
                tipoMensagem = 'T';
            }  
        });
        
        lblImagem.addMouseListener(new MouseAdapter()  
        {  
            @Override
            public void mouseClicked(MouseEvent e)  
            {  
                lblTexto.setForeground(Color.black);
                lblImagem.setForeground(new Color(31, 167, 9));
                txtMensagem.setEnabled(false);
                tipoMensagem = 'I';
                ImageIcon foto = null;
                JFileChooser fs = new JFileChooser();
                fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int val = fs.showOpenDialog(e.getComponent());
                if(val == JFileChooser.APPROVE_OPTION){
                    File caminhoFoto = fs.getSelectedFile();
                    txtMensagem.setText(caminhoFoto.getPath());
                }
            }  
        }); 
    }
    
    private void implementarMenu(){
        // declaração de variáveis
        JMenuBar menu = new JMenuBar();
        JMenu mnuArquivo = new JMenu();
        JMenuItem itemAlterarFoto = new JMenuItem();
        JMenuItem itemIniciarJogo = new JMenuItem();
        JSeparator separador = new JSeparator();
        JMenuItem itemSair = new JMenuItem();
        // propriedades
        mnuArquivo.setText("Arquivo");
        itemAlterarFoto.setText("Alterar foto...");
        itemIniciarJogo.setText("Iniciar jogo");
        itemSair.setText("Sair");
        // adiciona os componentes
        mnuArquivo.add(itemAlterarFoto);
        mnuArquivo.add(itemIniciarJogo);
        mnuArquivo.add(separador);
        mnuArquivo.add(itemSair);
        menu.add(mnuArquivo);
        setJMenuBar(menu);
        // implementa eventos de clique
        itemAlterarFoto.addActionListener((ActionEvent e) -> {
            ImageIcon foto = null;
            JFileChooser fs = new JFileChooser();
            fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int val = fs.showOpenDialog(this);
            if(val == JFileChooser.APPROVE_OPTION){
                File caminhoFoto = fs.getSelectedFile();
                foto = new ImageIcon(caminhoFoto.getPath());
                Image imagemRedimensionada = compartilhado.aplicacao.Imagem.redimensionarImagem(foto.getImage(), 50, false);
                foto = new ImageIcon(imagemRedimensionada);
                for (Grupo grupo : Principal.grupos) {
                    if(grupo.getId() == destino){
                        try {
                            grupo.setFoto(foto);
                            Principal.frmPrincipal.conexao.comunicador.alterarGrupo(grupo);
                        } catch (RemoteException ex) {
                            JOptionPane.showMessageDialog(null, "Houve uma falha ao enviar a mensagem para o servidor, tente novamente", 
                                    "Falha no envio", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                            return;
                        }
                        lblFoto.setIcon(foto);
                        break;
                    }
                }
            }
        });
        
        itemIniciarJogo.addActionListener((ActionEvent e) -> {
            FrameJogo frameJogo = new FrameJogo();
            frameJogo.setVisible(true);
            try {
                escreverInformacao("Jogo foi iniciado com sucesso!");
            } catch (BadLocationException ex) {
                Logger.getLogger(FrameConversa.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        itemSair.addActionListener((ActionEvent e) -> {
            setVisible(false);
        });
    }
    
    private void enviarMensagem(){
        if(!txtMensagem.getText().isEmpty()){ // se o texto não estiver vazio
            try {
                Mensagem mensagem = null;
                switch(tipoMensagem){
                    case 'T':
                        mensagem = mensagemBuilder.criarMensagem(mensagens.size() + 1, 'T', txtMensagem.getText()); // cria a mensagem
                        break;
                    case 'I':
                        ImageIcon imagem = new ImageIcon(txtMensagem.getText());
                        mensagem = mensagemBuilder.criarMensagem(mensagens.size() + 1, 'I', imagem); // cria a mensagem
                        break;
                }
                btnEnviar.setText("Enviando...");
                btnEnviar.setEnabled(false);
                try {
                    Principal.frmPrincipal.conexao.comunicador.enviarMensagem(mensagem); // envia a mensagem para o FramePrincipal
                    escreverMensagem(mensagem, false); // método para escrever mensagem na própria tela de quem mandou
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(null, "Houve uma falha ao enviar a mensagem para o servidor, tente novamente", 
                            "Falha no envio", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                btnEnviar.setText("Enviar");
                btnEnviar.setEnabled(true);
                txtMensagem.setText(""); // limpa o campo de mensagem
                txtMensagem.setEnabled(true);
                tipoMensagem = 'T';
                lblTexto.setForeground(new Color(31, 167, 9));
                lblImagem.setForeground(Color.black);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private boolean testeScroll(){
        JScrollBar sb = scroll.getVerticalScrollBar();
        int min = sb.getValue() + sb.getVisibleAmount();
        int max = sb.getMaximum();
        return min == max;
    }
    
    private void descerScroll(){
        SwingUtilities.invokeLater(() -> {
            scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
        });
    }
    
    public void carregarMensagens(){
        for (Mensagem mensagem : mensagens) {
            try {
                escreverMensagem(mensagem, true);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }    
        }
        descerScroll();
    }
    
    public void escreverMensagem(Mensagem mensagem, boolean carregamento) throws BadLocationException{
        if(!carregamento) // se não for um carregamento das mensagens antigas, então adiciona na lista de mensagens
            mensagens.add(mensagem);
        doc = txtConversa.getStyledDocument(); // pega o documento do JTextPane
        boolean deveDarScroll = testeScroll(); // faz o teste de scroll
        doc.insertString(doc.getLength(), "\n", formatacao("normal")); // pula uma linha
        if(mensagem.getIdOrigem() == origem) // verifica quem que enviou a mensagem
            doc.insertString(doc.getLength(), Principal.usuarios.get(origem - 1).getUsuario(), formatacao("origemNome")); // escreve o nome com a formatação adequada
        else
            doc.insertString(doc.getLength(), Principal.usuarios.get(mensagem.getIdOrigem() - 1).getUsuario(), formatacao("destinoNome")); // escreve o nome com a formatação adequada
        doc.insertString(doc.getLength(), " (" + DateFormat.getInstance().format(mensagem.getDataMensagem()) + ")\n", formatacao("normal")); // escreve a data da mensagem
        switch(mensagem.getTipoMensagem()){
            case 'T': // caso for mensagem de texto
                doc.insertString(doc.getLength(), "» " + mensagem.getMensagem().toString() + "\n", formatacao("normal")); // insere o texto da mensagem
                break;
            case 'I': // caso for mensagem de imagem
                Style estilo = doc.addStyle("imagem", null); // cria um novo estilo
                ImageIcon imagem = (ImageIcon)mensagem.getMensagem(); // obtem a imagem da mensagem
                StyleConstants.setIcon(estilo, new ImageIcon(Imagem.redimensionarImagem(imagem.getImage(), 200, true))); // define o icone com redimensionamento
                doc.insertString(doc.getLength(), "imagem", estilo); // insere a imagem no documento
                doc.insertString(doc.getLength(), "\n", formatacao("normal")); // pula uma linha
                break;
        }
        doc.setParagraphAttributes(0, doc.getLength(), formatacao("normal"), true); // formata a fonte e paragrafo
        txtConversa.setStyledDocument(doc); // joga o documento de volta para o JTextPane
        if(deveDarScroll) // se deve dar scroll
            descerScroll(); // desce o scroll
    }
    
    private void escreverInformacao(String informacao) throws BadLocationException{
        doc = txtConversa.getStyledDocument(); // pega o documento do JTextPane
        boolean deveDarScroll = testeScroll(); // faz o teste de scroll
        doc.insertString(doc.getLength(), "\n" + informacao + "\n", formatacao("informacao")); // escreve a informação
        doc.setParagraphAttributes(0, doc.getLength(), formatacao("normal"), true); // formata a fonte e paragrafo
        txtConversa.setStyledDocument(doc); // joga o documento de volta para o JTextPane
        if(deveDarScroll) // se deve dar scroll
            descerScroll(); // desce o scroll
    }
    
    private SimpleAttributeSet formatacao(String tipo){
        SimpleAttributeSet formatacao = new SimpleAttributeSet();
        StyleConstants.setFontFamily(formatacao, "Tahoma");
        StyleConstants.setFontSize(formatacao, 11);
        StyleConstants.setLeftIndent(formatacao, 10);
        StyleConstants.setRightIndent(formatacao, 10);
        if(tipo.equals("destinoNome")){
            StyleConstants.setBold(formatacao, true);
            StyleConstants.setForeground(formatacao, Color.blue);
        }else if(tipo.equals("origemNome"))
            StyleConstants.setBold(formatacao, true);
        else if(tipo.equals("informacao")){
            StyleConstants.setBold(formatacao, true);
            StyleConstants.setForeground(formatacao, Color.red);
        } else if(tipo.equals("normal")){
            // não faz nada além do básico
        }
        return formatacao;
    }
    
    public void carregarInfo(){ // carrega as informações do destino
        if(tipoDestino == 'U'){
            lblFoto.setIcon(Principal.usuarios.get(destino - 1).getFoto());
            lblDestino.setText(Principal.usuarios.get(destino - 1).getUsuario());
            setTitle(Principal.usuarios.get(destino - 1).getUsuario() + " - Mensageiro");
            lblStatus.setText((Principal.usuarios.get(destino - 1).isOnline()? "Online" : "Offline"));
            lblStatus.setForeground((Principal.usuarios.get(destino - 1).isOnline()? new Color(31, 167, 9) : Color.red));
        }else{
            Grupo grupo = Principal.frmPrincipal.getGrupoPorId(destino);
            Image foto = compartilhado.aplicacao.Imagem.redimensionarImagem(grupo.getFoto().getImage(), 50, false);
            lblFoto.setIcon(new ImageIcon(foto));
            lblDestino.setText(grupo.getNome());
            setTitle(grupo.getNome() + " - Mensageiro");
            String membros = "";
            for (int i = 0; i < 10; i++) {
                if(grupo.getMembros()[i] == 0)
                    break;
                membros += Principal.usuarios.get(grupo.getMembros()[i] - 1).getUsuario() + ", ";
            }
            membros = membros.substring(0, membros.lastIndexOf(","));
            lblStatus.setText(membros);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollBar1 = new javax.swing.JScrollBar();
        pnlHeader = new javax.swing.JPanel();
        lblFoto = new javax.swing.JLabel();
        lblDestino = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        pnlConversa = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        txtConversa = new javax.swing.JTextPane();
        pnlEnviarMsg = new javax.swing.JPanel();
        txtMensagem = new javax.swing.JTextField();
        btnEnviar = new javax.swing.JButton();
        lblTexto = new javax.swing.JLabel();
        lblImagem = new javax.swing.JLabel();

        setTitle("Usuário");
        setMinimumSize(new java.awt.Dimension(600, 500));
        setName("frmConversa"); // NOI18N
        setPreferredSize(new java.awt.Dimension(600, 500));
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.rowWeights = new double[] {2.0, 50.0, 1.0};
        getContentPane().setLayout(layout);

        pnlHeader.setLayout(new java.awt.GridBagLayout());

        lblFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblFoto.setMaximumSize(new java.awt.Dimension(50, 50));
        lblFoto.setMinimumSize(new java.awt.Dimension(50, 50));
        lblFoto.setPreferredSize(new java.awt.Dimension(50, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlHeader.add(lblFoto, gridBagConstraints);

        lblDestino.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDestino.setText("Usuário");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlHeader.add(lblDestino, gridBagConstraints);

        lblStatus.setText("Offline");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlHeader.add(lblStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(pnlHeader, gridBagConstraints);

        pnlConversa.setLayout(new java.awt.GridBagLayout());

        txtConversa.setEditable(false);
        txtConversa.setBorder(null);
        txtConversa.setMargin(new java.awt.Insets(5, 10, 5, 10));
        scroll.setViewportView(txtConversa);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlConversa.add(scroll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(pnlConversa, gridBagConstraints);

        pnlEnviarMsg.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 1);
        pnlEnviarMsg.add(txtMensagem, gridBagConstraints);

        btnEnviar.setText("Enviar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 5);
        pnlEnviarMsg.add(btnEnviar, gridBagConstraints);

        lblTexto.setBackground(new java.awt.Color(255, 255, 255));
        lblTexto.setForeground(new java.awt.Color(31, 167, 9));
        lblTexto.setText("Texto");
        lblTexto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 1, 5);
        pnlEnviarMsg.add(lblTexto, gridBagConstraints);

        lblImagem.setText("Imagem");
        lblImagem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 40, 1, 5);
        pnlEnviarMsg.add(lblImagem, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(pnlEnviarMsg, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnviar;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JLabel lblDestino;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblImagem;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTexto;
    private javax.swing.JPanel pnlConversa;
    private javax.swing.JPanel pnlEnviarMsg;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTextPane txtConversa;
    private javax.swing.JTextField txtMensagem;
    // End of variables declaration//GEN-END:variables
}
