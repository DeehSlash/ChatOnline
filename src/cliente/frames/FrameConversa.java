package cliente.frames;

import compartilhado.aplicacao.MensagemBuilder;
import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import cliente.aplicacao.Principal;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.DateFormat;

public class FrameConversa extends javax.swing.JFrame {

    private Usuario origem;
    private Usuario destino;
    private StyledDocument doc;
    private final MensagemBuilder mensagemBuilder;
    private ArrayList<Mensagem> mensagens;
    
    public FrameConversa(Usuario origem, Usuario destino) {
        initComponents();
        addListeners();
        this.origem = origem;
        this.destino = destino;
        mensagens = new ArrayList<>();
        mensagemBuilder = new MensagemBuilder(origem.getId(), destino.getId());
        carregarInfoUsuario();
        setVisible(true);
    }
    
    public int getIdDestino(){ return destino.getId(); }
    public void setDestino(Usuario usuario) { destino = usuario; }
    
    private void addListeners(){
        
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                setVisible(false);
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
    }
    
    private void enviarMensagem(){
        if(!txtMensagem.getText().isEmpty()){ // se o texto não estiver vazio
            try {
                Mensagem mensagem = mensagemBuilder.criarMensagem(mensagens.size(), 'U', 'T', txtMensagem.getText()); // cria a mensagem
                escreverMensagem(mensagem); // método para escrever mensagem na própria tela de quem mandou
                txtMensagem.setText(""); // limpa o campo de mensagem
                Principal.frmPrincipal.enviarMensagem(mensagem); // envia a mensagem para o FramePrincipal
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void escreverMensagem(Mensagem mensagem) throws BadLocationException{ // escreve a própria mensagem na tela com formatação
        doc = txtConversa.getStyledDocument();
        doc.insertString(doc.getLength(), origem.getUsuario(), formatacao("origemNome"));
        doc.insertString(doc.getLength(), " (" + DateFormat.getInstance().format(mensagem.getDataMensagem()) + ")\n", formatacao("normal"));
        doc.insertString(doc.getLength(), "» " + mensagem.getMensagem().toString() + "\n\n", formatacao("normal"));
        doc.setParagraphAttributes(0, doc.getLength(), formatacao("paragrafo"), true);
        txtConversa.setStyledDocument(doc);
    }
    
    public void receberMensagem(Mensagem mensagem) throws BadLocationException{ // escreve a mensagem que recebeu na tela com formatação
        doc = txtConversa.getStyledDocument();
        doc.insertString(doc.getLength(), destino.getUsuario(), formatacao("destinoNome"));
        doc.insertString(doc.getLength(), " (" + DateFormat.getInstance().format(mensagem.getDataMensagem()) + ")\n", formatacao("nome"));
        doc.insertString(doc.getLength(), "» " + mensagem.getMensagem().toString() + "\n\n", formatacao("nome"));
        doc.setParagraphAttributes(0, doc.getLength(), formatacao("paragrafo"), true);
        txtConversa.setStyledDocument(doc);
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
        else if(tipo.equals("normal")){
            // não faz nada
        }
        return formatacao;
    }
    
    public void carregarInfoUsuario(){ // carrega as informações do usuário (cliente)
        lblFoto.setIcon(destino.getFoto());
        lblUsuario.setText(destino.getUsuario());
        setTitle(destino.getUsuario() + " - Mensageiro");
        lblStatus.setText((destino.isOnline()? "Online" : "Offline"));
        lblStatus.setForeground((destino.isOnline()? new Color(31, 167, 9) : Color.red));
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
        lblUsuario = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        pnlConversa = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtConversa = new javax.swing.JTextPane();
        pnlEnviarMsg = new javax.swing.JPanel();
        txtMensagem = new javax.swing.JTextField();
        btnEnviar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Usuário");
        setMaximumSize(null);
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

        lblUsuario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblUsuario.setText("Usuário");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlHeader.add(lblUsuario, gridBagConstraints);

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
        jScrollPane1.setViewportView(txtConversa);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlConversa.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(pnlConversa, gridBagConstraints);

        pnlEnviarMsg.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 1);
        pnlEnviarMsg.add(txtMensagem, gridBagConstraints);

        btnEnviar.setText("Enviar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 5);
        pnlEnviarMsg.add(btnEnviar, gridBagConstraints);

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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel pnlConversa;
    private javax.swing.JPanel pnlEnviarMsg;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JTextPane txtConversa;
    private javax.swing.JTextField txtMensagem;
    // End of variables declaration//GEN-END:variables
}
