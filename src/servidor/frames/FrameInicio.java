package servidor.frames;

import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import servidor.aplicacao.Principal;

public class FrameInicio extends javax.swing.JFrame {

    public FrameInicio() {
        initComponents();
        ButtonGroup grupo = new ButtonGroup(); // cria um grupo para os Radio Buttons
        grupo.add(rbtnConsole);
        grupo.add(rbtnGrafico);
        rbtnConsole.setSelected(true); // deixa o modo console selecionado por padrão
        addListeners(); // chama a função que adiciona os listeners
    }
    
    private void addListeners(){
        btnIniciar.addActionListener((ActionEvent e) -> {   // Evento de clique no botão Iniciar
            if(rbtnGrafico.isSelected()){
                Principal.frmPrincipal.setVisible(true);
                dispose();
            }else if(rbtnConsole.isSelected()){
                System.out.println("Modo console indisponível");
            }       
        });
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rbtnConsole = new javax.swing.JRadioButton();
        rbtnGrafico = new javax.swing.JRadioButton();
        btnIniciar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        lblTipo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Servidor - Mensageiro");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(350, 300));
        setName("frmInicio"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        rbtnConsole.setText("Console");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 5, 5);
        getContentPane().add(rbtnConsole, gridBagConstraints);

        rbtnGrafico.setText("Interface gráfica");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(rbtnGrafico, gridBagConstraints);

        btnIniciar.setText("Iniciar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 20, 5);
        getContentPane().add(btnIniciar, gridBagConstraints);

        lblTitulo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitulo.setText("Servidor - Mensageiro");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 5, 5);
        getContentPane().add(lblTitulo, gridBagConstraints);

        lblTipo.setText("Escolha de que modo o servidor irá ser executado:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 5, 5);
        getContentPane().add(lblTipo, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciar;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JRadioButton rbtnConsole;
    private javax.swing.JRadioButton rbtnGrafico;
    // End of variables declaration//GEN-END:variables
}
