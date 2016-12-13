package cliente.frames;

import cliente.aplicacao.Conexao;
import cliente.aplicacao.Principal;
import cliente.aplicacao.Transmissao;
import compartilhado.aplicacao.MensagemBuilder;
import compartilhado.modelo.Grupo;
import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

public class FramePrincipal extends javax.swing.JFrame {

    public final Conexao conexao;
    public ArrayList<FrameConversa> conversas;
    
    public FramePrincipal(Conexao conexao) {
        initComponents();
        addListeners();
        this.conexao = conexao;
        conversas = new ArrayList<>();
        carregarLista(false);
        carregarInfoUsuario();
        inicializarConversas();
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
        
        listUsuarios.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt){
                if (evt.getClickCount() == 2){
                    boolean aberta = false;
                    Usuario destino = null;
                    if(!listUsuarios.getSelectedValue().equals("----------Offline----------") && // ignora se foi clicado na mensagem de online/offline
                            !listUsuarios.getSelectedValue().equals("----------Online----------"))
                        destino = Principal.usuarios.get(idPorNome(listUsuarios.getSelectedValue()) - 1); // encontra o destino a partir do nome
                    if(destino != null){
                        int i = 0;
                        for (FrameConversa conversa : conversas) { // verifica se a conversa já foi aberta alguma vez e pega a id da lista
                            if(conversa.getIdDestino() == destino.getId()){
                                aberta = true;
                                break;
                            }
                            i++;
                        }
                        if(aberta){ // se já foi aberta
                            if(!conversas.get(i).isVisible()) // e não estiver visivel
                                 conversas.get(i).setVisible(true); // torna visivel
                        }else{ // se nunca foi aberta
                            conversas.add(new FrameConversa(Principal.usuarios.get(conexao.getCliente().getId() - 1), destino.getId(), 'U')); // adiciona na lista
                            conversas.get(conversas.size() - 1).setVisible(true); // torna vísivel
                        }
                    }
                }
            }
        });
        
        itemAlterarFoto.addActionListener((ActionEvent e) -> {
            ImageIcon foto = null;
            JFileChooser fs = new JFileChooser();
            fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int val = fs.showOpenDialog(this);
            if(val == JFileChooser.APPROVE_OPTION){
                File caminhoFoto = fs.getSelectedFile();
                foto = new ImageIcon(caminhoFoto.getPath());
                Image imagemRedimensionada = compartilhado.aplicacao.Foto.redimensionarFoto(foto.getImage(), 50, false);
                foto = new ImageIcon(imagemRedimensionada);
            }
            for (Usuario usuario : Principal.usuarios) {
                if(usuario.getId() == conexao.getCliente().getId()){
                    try {
                        lblFoto.setIcon(foto);
                        usuario.setFoto(foto);
                        conexao.alterarUsuario(usuario);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        itemTransmissao.addActionListener((ActionEvent e) -> {
            String msg = JOptionPane.showInputDialog(this, "Digite a mensagem que será transmitida:", "Enviar transmissão", JOptionPane.INFORMATION_MESSAGE);
            MensagemBuilder mensagemBuilder = new MensagemBuilder(conexao.getCliente().getId(), 0);
            Mensagem mensagem = mensagemBuilder.criarMensagem(0, 'U', 'T', msg);
            try {
                Transmissao.transmitir(Principal.usuarios, mensagem);
                JOptionPane.showMessageDialog(this, "A transmissão foi enviada!", "Transmissão", JOptionPane.INFORMATION_MESSAGE);
            } catch (BadLocationException | IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Houve um erro com a transmissão, tente novamente", "Transmissão", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        itemCriarGrupo.addActionListener((ActionEvent e) -> {
            FrameCriarGrupo frmCriarGrupo = new FrameCriarGrupo();
            frmCriarGrupo.setVisible(true);
        });
        
        itemSair.addActionListener((ActionEvent e) -> {
            try {
                fecharConversas();
                conexao.desconectar();
                dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    private void inicializarConversas(){
        for (Usuario usuario : Principal.usuarios) {
            if(usuario.getId() != conexao.getCliente().getId()){
                try {
                    FrameConversa conversa = new FrameConversa(Principal.usuarios.get(conexao.getCliente().getId() - 1), usuario.getId(), 'U');
                    conversa.mensagens = conexao.comunicador.recuperarListaMensagens(conexao.getCliente().getId(), usuario.getId());
                    conversa.carregarMensagens();
                    conversas.add(conversa);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    private void fecharConversas(){
        for (FrameConversa conversa : conversas) {
            conversa.dispose();
        }
    }
    
    public void receberMensagem(Mensagem mensagem){
        boolean conversaAberta = false;
        int i = 0;
        for (FrameConversa conversa : conversas) {
            if(conversa.getIdDestino() == mensagem.getIdOrigem()){
                conversaAberta = true;
                break;
            }
            i++;
        }
        try {
            if(conversaAberta){
                if(!conversas.get(i).isVisible())
                    conversas.get(i).setVisible(true);
                conversas.get(i).escreverMensagem(mensagem, false);
            }else{
                conversas.add(new FrameConversa(Principal.usuarios.get(conexao.getCliente().getId() - 1),
                        mensagem.getIdOrigem(), 'U'));
                conversas.get(conversas.size() - 1).setVisible(true);
                conversas.get(conversas.size() - 1).escreverMensagem(mensagem, false);
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
        Usuario usuario = Principal.usuarios.get(conexao.getCliente().getId() - 1);
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
    
    public void atualizarConversas() {
        int id;
        for (FrameConversa conversa : conversas) {
            id = conversa.getIdDestino();
            for (Usuario usuario : Principal.usuarios) {
                if(usuario.getId() == id){
                    conversa.setDestino(usuario);
                }
            }
            conversa.carregarInfoUsuario();
        }
    }
    
    public void carregarLista(boolean atualizacao){ // carrega a lista de usuários
        if(!atualizacao){
            try {
                Principal.usuarios = conexao.comunicador.recuperarListaUsuarios();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        DefaultListModel listModel = new DefaultListModel();
        listModel.addElement("----------Online----------");
        for (Usuario usuario : Principal.usuarios) { // primeiro adiciona à lista os usuários online
            if(usuario.getId() != conexao.getCliente().getId() && usuario.isOnline()){
                listModel.addElement(usuario.getUsuario());
            }
        }
        listModel.addElement("----------Offline----------");
        for (Usuario usuario : Principal.usuarios) { // só então adiciona os usuários offline
            if(usuario.getId() != conexao.getCliente().getId() && !usuario.isOnline()){
                listModel.addElement(usuario.getUsuario());
            }
        }
        listModel.addElement("----------Grupos-----------");
        for(Grupo grupo : Principal.grupos){
            listModel.addElement(grupo.getNome());
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
        itemAlterarFoto = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        itemSair = new javax.swing.JMenuItem();
        mnuConversa = new javax.swing.JMenu();
        itemCriarGrupo = new javax.swing.JMenuItem();
        itemDeletarGrupo = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        itemTransmissao = new javax.swing.JMenuItem();
        mnuSobre = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mensageiro");
        setMinimumSize(new java.awt.Dimension(500, 600));
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

        jScrollPane1.setToolTipText("");
        jScrollPane1.setAutoscrolls(true);

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

        itemAlterarFoto.setText("Alterar foto...");
        mnuArquivo.add(itemAlterarFoto);
        mnuArquivo.add(jSeparator1);

        itemSair.setText("Sair");
        mnuArquivo.add(itemSair);

        menuBar.add(mnuArquivo);

        mnuConversa.setText("Conversa");

        itemCriarGrupo.setText("Criar grupo...");
        mnuConversa.add(itemCriarGrupo);

        itemDeletarGrupo.setText("Deletar grupo...");
        mnuConversa.add(itemDeletarGrupo);
        mnuConversa.add(jSeparator2);

        itemTransmissao.setText("Enviar transmissão...");
        mnuConversa.add(itemTransmissao);

        menuBar.add(mnuConversa);

        mnuSobre.setText("Sobre");
        menuBar.add(mnuSobre);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem itemAlterarFoto;
    private javax.swing.JMenuItem itemCriarGrupo;
    private javax.swing.JMenuItem itemDeletarGrupo;
    private javax.swing.JMenuItem itemSair;
    private javax.swing.JMenuItem itemTransmissao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblIP;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusConexao;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JList<String> listUsuarios;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu mnuArquivo;
    private javax.swing.JMenu mnuConversa;
    private javax.swing.JMenu mnuSobre;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlInfo;
    private javax.swing.JPanel pnlListaUsuarios;
    // End of variables declaration//GEN-END:variables
}
