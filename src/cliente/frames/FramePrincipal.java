package cliente.frames;

import cliente.aplicacao.ConexaoCliente;
import cliente.aplicacao.Principal;
import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;

public class FramePrincipal extends javax.swing.JFrame {

    private final ConexaoCliente conexao;
    public ArrayList<FrameConversa> conversas;
    
    public FramePrincipal(ConexaoCliente conexao) {
        initComponents();
        addListeners();
        this.conexao = conexao;
        conversas = new ArrayList<>();
        carregarLista();
        carregarInfoUsuario();
        Thread t = conexao;
        t.start();
    }

    private void addListeners(){
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){ // caso a janela tenha sido fechada, encerra a conexão com o servidor
                try {
                    conexao.desconectar();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        listUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Usuario destino = null;
                    if(!listUsuarios.getSelectedValue().equals("----------Offline----------") &&
                            !listUsuarios.getSelectedValue().equals("----------Online----------"))
                        destino = Principal.usuarios.get(idPorNome(listUsuarios.getSelectedValue()) - 1);
                    if(destino != null)
                        conversas.add(new FrameConversa(Principal.usuarios.get(conexao.getIdCliente() - 1), destino));
                }
            }
        });
    }
    
    protected void enviarMensagem(Mensagem mensagem){
        try {
            conexao.enviarMensagem(mensagem);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void receberMensagem(Mensagem mensagem){
        boolean conversaAberta = false;
        int id = -1;
        for (FrameConversa conversa : conversas) {
            if(conversa.getIdDestino() == mensagem.getIdOrigem()){
                conversaAberta = true;
                id = conversas.indexOf(conversa);
            }
        }
        try {
            if(conversaAberta)
                conversas.get(id).receberMensagem(mensagem);
            else{
                conversas.add(new FrameConversa(Principal.usuarios.get(conexao.getIdCliente() - 1),
                        Principal.usuarios.get(mensagem.getIdOrigem()- 1)));
                conversas.get(conversas.size() - 1).receberMensagem(mensagem);
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }
    
    public int idPorNome(String nome){
        for (Usuario usuario : Principal.usuarios) {
            if(usuario.getUsuario().equals(nome)){
                return usuario.getId();
            }
        }
        return -1;
    }
    
    private void carregarInfoUsuario(){ // carrega as informações do usuário (cliente)
        Usuario usuario = Principal.usuarios.get(conexao.getIdCliente() - 1);
        lblFoto.setIcon(usuario.getFoto());
        lblUsuario.setText(usuario.getUsuario());
        atualizarStatusConexao();
    }
    
    private void atualizarStatusConexao(){ // atualiza os labels com a informação da conexão
        if(conexao.getStatus()){
            lblStatusConexao.setText("Conectado");
            lblStatusConexao.setForeground(new Color(31, 167, 9));
            lblStatus.setText("Online");
            lblStatus.setForeground(new Color(31, 167, 9));
            lblIP.setText(conexao.getEndereco() + ":" + conexao.getPorta());
        }
    }
    
    public void carregarLista(){ // carrega a lista de usuários
        try {
            conexao.atualizarListaUsuarios();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        DefaultListModel listModel = new DefaultListModel();
        listModel.addElement("----------Online----------");
        for (Usuario usuario : Principal.usuarios) { // primeiro adiciona à lista os usuários online
            if(usuario.getId() != conexao.getIdCliente() && usuario.isOnline()){
                listModel.addElement(usuario.getUsuario());
            }
        }
        listModel.addElement("----------Offline----------");
        for (Usuario usuario : Principal.usuarios) { // só então adiciona os usuários offline
            if(usuario.getId() != conexao.getIdCliente() && !usuario.isOnline()){
                listModel.addElement(usuario.getUsuario());
            }
        }
        listUsuarios.setModel(listModel);
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

        pnlHeader = new javax.swing.JPanel();
        lblFoto = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        pnlListaUsuarios = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listUsuarios = new javax.swing.JList<>();
        pnlInfo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblStatusConexao = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblIP = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        mnuArquivo = new javax.swing.JMenu();
        itemSair = new javax.swing.JMenuItem();
        mnuContatos = new javax.swing.JMenu();
        itemAddContato = new javax.swing.JMenuItem();
        itemDelContato = new javax.swing.JMenuItem();
        mnuSobre = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mensageiro");
        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(500, 600));
        setPreferredSize(new java.awt.Dimension(500, 600));
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

        pnlListaUsuarios.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setViewportView(listUsuarios);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlListaUsuarios.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnlListaUsuarios, gridBagConstraints);

        pnlInfo.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlInfo.add(jLabel1, gridBagConstraints);

        lblStatusConexao.setForeground(new java.awt.Color(255, 0, 0));
        lblStatusConexao.setText("Desconectado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 10);
        pnlInfo.add(lblStatusConexao, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Servidor:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        pnlInfo.add(jLabel3, gridBagConstraints);

        lblIP.setText("0.0.0.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlInfo.add(lblIP, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(pnlInfo, gridBagConstraints);

        mnuArquivo.setText("Arquivo");

        itemSair.setText("Sair");
        mnuArquivo.add(itemSair);

        menuBar.add(mnuArquivo);

        mnuContatos.setText("Contatos");

        itemAddContato.setText("Adicionar contato");
        mnuContatos.add(itemAddContato);

        itemDelContato.setText("Excluir contato");
        mnuContatos.add(itemDelContato);

        menuBar.add(mnuContatos);

        mnuSobre.setText("Sobre");
        menuBar.add(mnuSobre);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem itemAddContato;
    private javax.swing.JMenuItem itemDelContato;
    private javax.swing.JMenuItem itemSair;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblIP;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusConexao;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JList<String> listUsuarios;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu mnuArquivo;
    private javax.swing.JMenu mnuContatos;
    private javax.swing.JMenu mnuSobre;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlInfo;
    private javax.swing.JPanel pnlListaUsuarios;
    // End of variables declaration//GEN-END:variables

}
