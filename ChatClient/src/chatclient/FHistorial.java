
package chatclient;

import java.awt.Toolkit;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import model.Constantes;
import model.DataListItem;
import model.DataMssge;
import model.Message;

/**
 *
 * @author Carlos Marten
 */
public class FHistorial extends javax.swing.JFrame {

    private FChat parent = null;
    private DefaultListModel lstModel;

    public FHistorial() {
        initComponents();
    }

    public FHistorial(FChat fchat) {
        this.parent = fchat;
        initComponents();
        lstModel = new DefaultListModel();
        lstHistoriales.setModel(lstModel);
        peticionHistoriales();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lstHistoriales = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAHistory = new javax.swing.JTextArea();
        btnDone = new javax.swing.JButton();
        lblParticipantes = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Chat Application - Histoy");
        setMinimumSize(new java.awt.Dimension(458, 223));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        lstHistoriales.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstHistoriales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstHistorialesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(lstHistoriales);

        txtAHistory.setColumns(20);
        txtAHistory.setEditable(false);
        txtAHistory.setRows(5);
        txtAHistory.setMinimumSize(new java.awt.Dimension(164, 94));
        jScrollPane2.setViewportView(txtAHistory);

        btnDone.setText("Done");
        btnDone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoneActionPerformed(evt);
            }
        });

        lblParticipantes.setForeground(new java.awt.Color(0, 102, 204));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(37, 508, Short.MAX_VALUE)
                        .addComponent(btnDone))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblParticipantes, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblParticipantes, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDone)
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoneActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnDoneActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        this.parent.setEnabled(true);
        this.parent.toFront();
    }//GEN-LAST:event_formWindowClosed

    private void lstHistorialesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstHistorialesMouseClicked
        if (evt.getClickCount() == 2) {
            DataMssge datos = new DataMssge();

            datos.setOption(Constantes.Eoption.peticionHistorial);
            datos.setUser(this.parent.getParent().getCurrentUser());

            datos.setChatID(Integer.parseInt(((DataListItem) lstHistoriales.getSelectedValue()).getKey()));

            try {
                this.parent.getParent().getComm().sendRequest(datos);
            } catch (IOException ex) {
                Common.ErrorMessage("Error al mandar : " + ex.getMessage(), ex);
                this.parent.getParent().ConnectionClosed();
            }
        }
    }//GEN-LAST:event_lstHistorialesMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FHistorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FHistorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FHistorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FHistorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new FHistorial().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDone;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblParticipantes;
    private javax.swing.JList lstHistoriales;
    private javax.swing.JTextArea txtAHistory;
    // End of variables declaration//GEN-END:variables

    private void peticionHistoriales() {
        DataMssge datos = new DataMssge();
        datos.setOption(Constantes.Eoption.peticionHistoriales);
        datos.setUser(this.parent.getParent().getCurrentUser());
        try {
            this.parent.getParent().getComm().sendRequest(datos);
        } catch (IOException ex) {
            Logger.getLogger(FHistorial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayRecords(DataMssge datos) {
        DataListItem lstItem;
        Enumeration e = datos.getRecords().keys();
        String key;
        lstModel = (DefaultListModel) lstHistoriales.getModel();
        lstModel.clear();

        //iterate through Hashtable keys Enumeration and send request to all users
        while (e.hasMoreElements()) {
            lstItem = new DataListItem();
            key = e.nextElement().toString();
            lstItem.setValue(datos.getRecords().get(key).toString());
            lstItem.setKey(key);
            lstModel.addElement(lstItem);
        }
    }

    void displayRecord(DataMssge datos) {
        String strContent = "";
        boolean isFirst = true;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        for (Message msg : datos.getMensajes()) {
            if (isFirst) {
                strContent = "[" + msg.getStrUsername() + "-" + dateFormat.format(msg.getDatTime()) + "]: " + msg.getStrText();
                isFirst = false;
            } else {
                strContent += System.getProperty("line.separator") + "[" + msg.getStrUsername() + "-" + dateFormat.format(msg.getDatTime()) + "]: " + msg.getStrText();
            }
        }
        txtAHistory.setText(strContent);

        strContent = "participants: ";
        isFirst = true;
        //@Abraham Mejor emplear StringBuilder
        for (String name : datos.getUsers().values()) {
            if (isFirst) {
                strContent += name;
                isFirst = false;
            } else {
                strContent += " , " + name;
            }
        }
        lblParticipantes.setText(strContent);
    }
}
