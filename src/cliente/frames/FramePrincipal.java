package cliente.frames;

//IMPORTAÇÕES DO PROJETO
import cliente.aplicacao.Conexao;
import cliente.aplicacao.Principal;
import cliente.aplicacao.Transmissao;
import compartilhado.aplicacao.MensagemBuilder;
import compartilhado.modelo.Grupo;
import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
// IMPORTAÇÕES JAVA
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
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
        new Thread(() -> {
            inicializarConversas();
            carregarMensagens();
        }).start();
        Thread t = conexao;
        t.start();
    }

    private void addListeners(){
        
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){ // caso a janela tenha sido fechada, encerra a conexão com o servidor
                try {
                    conexao.comunicador.desconectar();
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
                    int destino = 0;
                    if(listUsuarios.getSelectedValue().equals("----------Offline----------") || // ignora se foi clicado na mensagem de online/offline
                            listUsuarios.getSelectedValue().equals("----------Online----------") || 
                            listUsuarios.getSelectedValue().equals("---------Grupos----------"))
                        return;
                    char tipoDestino = identificarSelecao();
                    switch(tipoDestino){
                        case 'U':
                            destino = Principal.usuarios.get(idPorNome(listUsuarios.getSelectedValue()) - 1).getId(); // encontra o destino a partir do nome
                            break;
                        case 'G':
                            destino = getGrupoPorNome(listUsuarios.getSelectedValue()).getId();
                            break;
                    }
                    if(destino == 0)
                        return;
                    int i = 0;
                    for (FrameConversa conversa : conversas) { // verifica se a conversa já foi aberta alguma vez e pega a id da lista
                        if(conversa.getDestino() == destino && conversa.getTipoDestino() == tipoDestino){
                            aberta = true;
                            break;
                        }
                        i++;
                    }
                    if(aberta){ // se já foi aberta
                        if(conversas.get(i).getCarregado() == false){
                            conversas.get(i).setNotificar(true);
                            JOptionPane.showMessageDialog(null, "A conversa ainda não foi carregada, você será notificado quando estiver pronta.",
                                    "Conversa não carregada", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        if(!conversas.get(i).isVisible()) // e não estiver visivel
                             conversas.get(i).setVisible(true); // torna visivel
                    }else{ // se nunca foi aberta
                        conversas.add(new FrameConversa(conexao.getCliente().getId(), destino, tipoDestino)); // adiciona na lista
                        conversas.get(conversas.size() - 1).setVisible(true); // torna vísivel
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
                Image imagemRedimensionada = compartilhado.aplicacao.Imagem.redimensionarImagem(foto.getImage(), 50, false);
                foto = new ImageIcon(imagemRedimensionada);
                for (Usuario usuario : Principal.usuarios) {
                    if(usuario.getId() == conexao.getCliente().getId()){
                        try {
                            lblFoto.setIcon(foto);
                            usuario.setFoto(foto);
                            conexao.comunicador.alterarUsuario(usuario);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        
        itemTransmissao.addActionListener((ActionEvent e) -> {
            String msg = JOptionPane.showInputDialog(this, "Digite a mensagem que será transmitida:", "Enviar transmissão", JOptionPane.INFORMATION_MESSAGE);
            MensagemBuilder mensagemBuilder = new MensagemBuilder(conexao.getCliente().getId(), 0, 'U');
            Mensagem mensagem = mensagemBuilder.criarMensagem(0, 'T', msg);
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
        
        itemDeletarGrupo.addActionListener((ActionEvent e) -> {
            String nome = JOptionPane.showInputDialog(this, "Digite o nome do grupo que será deletado:", "Deletar grupo", JOptionPane.QUESTION_MESSAGE);
            Grupo grupo = null;
            int i = 0;
            for (Grupo g : Principal.grupos) {
                if(g.getNome().equals(nome)){
                    grupo = g;
                    break;
                }
                i++;
            }
            if(grupo == null){
                JOptionPane.showMessageDialog(this, "Não existe um grupo com o nome informado, verifique e tente novamente", "Grupo não existe", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                conexao.comunicador.deletarGrupo(grupo);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Houve uma falha na comunicação com o servidor, tente novamente!", "Falha na comunicação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Principal.grupos.remove(i);
            carregarLista(true);
            i = 0;
            for (FrameConversa conversa : conversas) {
                if(conversa.getDestino() == grupo.getId() && conversa.getTipoDestino() == 'G')
                    break;
                i++;
            }
            conversas.get(i).dispose();
            conversas.remove(i);
        });
        
        itemSair.addActionListener((ActionEvent e) -> {
            try {
                fecharConversas();
                conexao.comunicador.desconectar();
                dispose();
                System.exit(0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    private void inicializarConversas(){
        for (Usuario usuario : Principal.usuarios) {
            if(usuario.getId() != conexao.getCliente().getId()){
                FrameConversa conversa = new FrameConversa(conexao.getCliente().getId(), usuario.getId(), 'U');
                conversas.add(conversa);
            }
        }
        for (Grupo grupo : Principal.grupos) {
            FrameConversa conversa = new FrameConversa(conexao.getCliente().getId(), grupo.getId(), 'G');
            conversas.add(conversa);
        }
    }
    
    private void carregarMensagens(){
        lblStatusConexao.setForeground(Color.RED);
        for (FrameConversa conversa : conversas) {
            String nome;
            if(conversa.getTipoDestino() == 'U') { nome = Principal.usuarios.get(conversa.getDestino() - 1).getUsuario(); }
            else { nome = getGrupoPorId(conversa.getDestino()).getNome(); }
            lblStatusConexao.setText("Carregando conversa com " + nome);
            try {
                conversa.mensagens = conexao.comunicador.recuperarListaMensagens(conexao.getCliente().getId(), conversa.getDestino(), conversa.getTipoDestino());
                new Thread(() -> {
                    conversa.carregarMensagens();
                    conversa.setCarregado(true);
                    if(conversa.getNotificar() == true)
                        JOptionPane.showMessageDialog(this, "A conversa com " + nome + " foi carregada, você pode abrir a conversa agora.",
                                "Conversa carregada", JOptionPane.INFORMATION_MESSAGE);
                }).start();
            } catch (RemoteException ex) {
                ex.printStackTrace();
                
            }
        }
        lblStatusConexao.setText("Conectado");
        lblStatusConexao.setForeground(new Color(31, 167, 9));
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
            if(mensagem.getDestinoTipo() == 'U'){
                if(conversa.getDestino() == mensagem.getIdOrigem() && conversa.getTipoDestino() == mensagem.getDestinoTipo()){
                    conversaAberta = true;
                    conversa.setVisible(true);
                    try {
                        conversa.escreverMensagem(mensagem, false);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }
            if(mensagem.getDestinoTipo() == 'G'){
                if(conversa.getDestino() == mensagem.getIdDestino() && conversa.getTipoDestino() == mensagem.getDestinoTipo()){
                    conversaAberta = true;
                    conversa.setVisible(true);
                    try {
                        conversa.escreverMensagem(mensagem, false);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }
            i++;
        }
        if(!conversaAberta){ // caso a conversa não esteja aberta (novo usuário ou grupo) então cria a conversa e adiciona na lista
            int id;
            if(mensagem.getDestinoTipo() == 'U')
                id = mensagem.getIdOrigem();
            else
                id = mensagem.getIdDestino();
            FrameConversa conversa = new FrameConversa(conexao.getCliente().getId(), id, mensagem.getDestinoTipo());
            conversas.add(conversa);
        }
    }
    
    private char identificarSelecao(){
        DefaultListModel modelo = (DefaultListModel) listUsuarios.getModel();
        int selecionado = listUsuarios.getSelectedIndex();
        int i = selecionado - 1;
        boolean encontrou = false;
        while(i >= 0){
            switch(modelo.get(i).toString()){
                case "----------Online----------":
                    return 'U';
                case "----------Offline----------":
                    return 'U';
                case "---------Grupos----------":
                    return 'G';
            }
            i--;
        }
        return 'N';
    }
    
    public int idPorNome(String nome){
        for (Usuario usuario : Principal.usuarios) {
            if(usuario.getUsuario().equals(nome)){
                return usuario.getId();
            }
        }
        return -1;
    }
    
    public Grupo getGrupoPorNome(String nome){
        for (Grupo grupo : Principal.grupos) {
            if(grupo.getNome().equals(nome))
                return grupo;
        }
        return null;   
    }
    
    public Grupo getGrupoPorId(int id){
        for (Grupo grupo : Principal.grupos) {
            if(grupo.getId() == id)
                return grupo;
        }
        return null;
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
            conversa.carregarInfo();
        }
    }
    
    public void carregarLista(boolean atualizacao){ // carrega a lista de usuários
        if(!atualizacao){
            try {
                Principal.usuarios = conexao.comunicador.recuperarListaUsuarios();
                Principal.grupos = conexao.comunicador.recuperarListaGrupos();
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
        listModel.addElement("---------Grupos----------");
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
