/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphamods.controlcenter;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.concurrent.ScheduledExecutorService;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Manu
 */
public class ConfigurationWizard extends javax.swing.JDialog {
public int step = 1;
public int totalsteps = 7;
int pumpfanmin = 3;
int pumpfanmax = 12;
public String path = System.getProperty("user.dir");

    /**
     * Creates new form ConfigurationWizard
     */
    public ConfigurationWizard(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setModels(true, false);
    }
    
    public void setModels(boolean fanchanged, boolean ledchanged){
        int vled = Integer.parseInt(leds.getValue().toString());
        int vfan = Integer.parseInt(fans.getValue().toString());
        int vpump = Integer.parseInt(pumps.getValue().toString());
        if (vfan + vpump >= 3){
            }else{
            if (fanchanged){
                pumps.setValue(3-vfan);
            }else{
                fans.setValue(3-vpump);
            }
        }
        if(vfan + vpump >pumpfanmax){
            if (fanchanged){
                if (vpump != 1){
                  pumps.setValue(vpump-1);

                }else{
                    fans.setValue(vfan -1);
                }
                
                
            }else{
                
              if (ledchanged){
                if((vled*3)+vfan+vpump <= 15){
                    
                }else {
                    
                      if(vpump >= 4 ){
                          pumps.setValue(vpump-3);
                      }else if (vpump==3){
                          pumps.setValue(vpump-2);
                          fans.setValue(vfan-1);
                      }else if (vpump == 2){
                          pumps.setValue(vpump-1);
                          fans.setValue(vfan-2);
                      }else if (vpump == 1){
                          fans.setValue(vfan-3);
                      }
                        
                        
                  
                }  
                  
                  
                  
              }else{
                 if (vfan != 1){
                fans.setValue(vfan-1);
                }else{
                    pumps.setValue(vpump-1);
                } 
              }
                
            }
        }
        
        SpinnerNumberModel fan = new javax.swing.SpinnerNumberModel(1, 1, 12, 1);
        SpinnerNumberModel pump = new javax.swing.SpinnerNumberModel(1, 1, 12, 1);
       // fans.setModel(fan);
        //pumps.setModel(pump);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        leds = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        fans = new javax.swing.JSpinner();
        pumps = new javax.swing.JSpinner();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        triples2 = new javax.swing.JLabel();
        triples1 = new javax.swing.JLabel();
        triples3 = new javax.swing.JLabel();
        triples4 = new javax.swing.JLabel();
        single1 = new javax.swing.JLabel();
        single2 = new javax.swing.JLabel();
        single3 = new javax.swing.JLabel();
        single4 = new javax.swing.JLabel();
        single5 = new javax.swing.JLabel();
        single6 = new javax.swing.JLabel();
        single7 = new javax.swing.JLabel();
        single8 = new javax.swing.JLabel();
        single9 = new javax.swing.JLabel();
        single10 = new javax.swing.JLabel();
        single11 = new javax.swing.JLabel();
        single12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jSlider1 = new javax.swing.JSlider();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jButton1.setText("Next >");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        mainPanel.setLayout(new java.awt.CardLayout());

        jLabel2.setText("welcome");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(229, 229, 229)
                .addComponent(jLabel2)
                .addContainerGap(435, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jLabel2)
                .addContainerGap(350, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel1, "card1");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Select the number of fans");

        leds.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));
        leds.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ledsStateChanged(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Select the number of pumps");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Select the number of  LED strips");

        fans.setModel(new javax.swing.SpinnerNumberModel(1, 1, 12, 1));
        fans.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fansStateChanged(evt);
            }
        });

        pumps.setModel(new javax.swing.SpinnerNumberModel(2, 1, 12, 1));
        pumps.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                pumpsStateChanged(evt);
            }
        });

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/channels.png"))); // NOI18N
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 62, -1, -1));

        triples2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector3.png"))); // NOI18N
        jPanel5.add(triples2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, -1, -1));

        triples1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector3.png"))); // NOI18N
        jPanel5.add(triples1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, -1));

        triples3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector3.png"))); // NOI18N
        jPanel5.add(triples3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, -1, 20));

        triples4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector3.png"))); // NOI18N
        jPanel5.add(triples4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, -1, 20));

        single1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector1.png"))); // NOI18N
        jPanel5.add(single1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 30, 20));

        single2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector1.png"))); // NOI18N
        jPanel5.add(single2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 30, 20));

        single3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector1.png"))); // NOI18N
        jPanel5.add(single3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 30, 20));

        single4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector1.png"))); // NOI18N
        jPanel5.add(single4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 30, 20));

        single5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector1.png"))); // NOI18N
        jPanel5.add(single5, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 30, 20));

        single6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector1.png"))); // NOI18N
        jPanel5.add(single6, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 30, 20));

        single7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector1.png"))); // NOI18N
        jPanel5.add(single7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, 30, 20));

        single8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector1.png"))); // NOI18N
        jPanel5.add(single8, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 30, 20));

        single9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector1.png"))); // NOI18N
        jPanel5.add(single9, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, 30, 20));

        single10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector1.png"))); // NOI18N
        jPanel5.add(single10, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, 30, 20));

        single11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector1.png"))); // NOI18N
        jPanel5.add(single11, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 30, 20));

        single12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alphamods/controlcenter/res/selector1.png"))); // NOI18N
        jPanel5.add(single12, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 40, 30, 20));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pumps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(leds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(233, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(leds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pumps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        mainPanel.add(jPanel2, "card2");

        jCheckBox1.setText("jCheckBox1");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(450, Short.MAX_VALUE)
                .addComponent(jCheckBox1)
                .addGap(174, 174, 174))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addComponent(jCheckBox1)
                .addContainerGap(313, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel3, "card3");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(369, Short.MAX_VALUE)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(136, 136, 136))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(257, Short.MAX_VALUE)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(195, 195, 195))
        );

        mainPanel.add(jPanel4, "card4");

        jButton2.setText("< Back");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked

        if(step == totalsteps){

        }else{
            CardLayout card = (CardLayout)mainPanel.getLayout();
            step = step + 1;
            jButton2.setEnabled(true);
            card.show(mainPanel, "card" + (step));
            jLabel3.setText("Step " + step);
            if(step == totalsteps){
                jButton1.setEnabled(false);
            }
        }

    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        if(step == 1){

        }else{
            CardLayout card = (CardLayout)mainPanel.getLayout();
            step = step - 1;
            card.show(mainPanel, "card" + (step));
            jLabel3.setText("Step " + step);
            jButton1.setEnabled(true);
            if (step == 1){
                jButton2.setEnabled(false);
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        jLabel3.setText("Step " + step);
        if (step == 1){
            jButton2.setEnabled(false);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowOpened

    private void ledsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ledsStateChanged
int value = Integer.parseInt(leds.getValue().toString());
        if (value == 1){
            pumpfanmax = 12;
        }else if (value == 2){
            pumpfanmax = 9;
        }else if (value == 3){
             pumpfanmax = 6;
        }else if (value == 4){
            pumpfanmax = 3;
        }
setModels(false, true);


// TODO add your handling code here:
    }//GEN-LAST:event_ledsStateChanged

    private void fansStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fansStateChanged
setModels(true, false);        // TODO add your handling code here:
    }//GEN-LAST:event_fansStateChanged

    private void pumpsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_pumpsStateChanged
setModels(false, false);        // TODO add your handling code here:
    }//GEN-LAST:event_pumpsStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ConfigurationWizard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConfigurationWizard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConfigurationWizard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConfigurationWizard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ConfigurationWizard dialog = new ConfigurationWizard(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner fans;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSpinner leds;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JSpinner pumps;
    private javax.swing.JLabel single1;
    private javax.swing.JLabel single10;
    private javax.swing.JLabel single11;
    private javax.swing.JLabel single12;
    private javax.swing.JLabel single2;
    private javax.swing.JLabel single3;
    private javax.swing.JLabel single4;
    private javax.swing.JLabel single5;
    private javax.swing.JLabel single6;
    private javax.swing.JLabel single7;
    private javax.swing.JLabel single8;
    private javax.swing.JLabel single9;
    private javax.swing.JLabel triples1;
    private javax.swing.JLabel triples2;
    private javax.swing.JLabel triples3;
    private javax.swing.JLabel triples4;
    // End of variables declaration//GEN-END:variables
}