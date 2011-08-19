//    Исходный код для Openbravo POS, автоматизированной системы продаж для работы
//    с сенсорным экраном, предоставлен ТОО "Норд-Трейдинг ЛТД", Республика Казахстан,
//    в период 2008-2011 годов на условиях лицензионного соглашения GNU LGPL.
//
//    Исходный код данного файл разработан в рамках проекта Openbravo POS ru
//    для использования системы Openbravo POS на территории бывшего СССР
//    <http://code.google.com/p/openbravoposru/>.
//
//    Openbravo POS является свободным программным обеспечением. Вы имеете право
//    любым доступным образом его распространять и/или модифицировать соблюдая
//    условия изложенные в GNU Lesser General Public License версии 3 и выше.
//
//    Данный исходный распространяется как есть, без каких либо гарантий на его
//    использование в каких либо целях, включая коммерческое применение. Данный
//    исход код может быть использован для связи с сторонними библиотеками
//    распространяемыми под другими лицензионными соглашениями. Подробности
//    смотрите в описании лицензионного соглашение GNU Lesser General Public License.
//
//    Ознакомится с условиями изложенными в GNU Lesser General Public License
//    вы можете в файле lgpl-3.0.txt каталога licensing проекта Openbravo POS ru.
//    А также на сайте <http://www.gnu.org/licenses/>.

package com.openbravo.pos.inventory;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.ListModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.pludevice.massakvpm.DeviceScaleVPM;
import com.openbravo.pos.pludevice.massakvpm.DeviceScaleVPMException;

public class JDlgUploadProductsScaleVPM extends javax.swing.JDialog {
    private DeviceScaleVPM m_scale;
    private BrowsableEditableData m_bd;
    
    private static String m_sUserBarcode;
    
    private JDlgUploadProductsScaleVPM(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    private JDlgUploadProductsScaleVPM(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    }       

    private void init(DeviceScaleVPM scale, BrowsableEditableData bd) {
        initComponents();
        getRootPane().setDefaultButton(jcmdOK);   
        m_scale = scale;
        m_bd = bd;
        setVisible(true);
    }
    
    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window)parent;
        } else {
            return getWindow(parent.getParent());
        }
    }    
    
    public static void showMessage(Component parent, DeviceScaleVPM scale, BrowsableEditableData bd, String userbarcode) {
        m_sUserBarcode = userbarcode;
        Window window = getWindow(parent);      
        JDlgUploadProductsScaleVPM myMsg;
        if (window instanceof Frame) { 
            myMsg = new JDlgUploadProductsScaleVPM((Frame) window, true);
        } else {
            myMsg = new JDlgUploadProductsScaleVPM((Dialog) window, true);
        }
        myMsg.init(scale, bd);
    }         
  
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jcmdOK = new javax.swing.JButton();
        jcmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabelScaleVPM = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(AppLocal.getIntString("caption.upload")); // NOI18N
        setResizable(false);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jcmdOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        jcmdOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
        jcmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdOKActionPerformed(evt);
            }
        });
        jPanel2.add(jcmdOK);

        jcmdCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        jcmdCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
        jcmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdCancelActionPerformed(evt);
            }
        });
        jPanel2.add(jcmdCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        jPanel1.setLayout(null);

        jLabelScaleVPM.setText(AppLocal.getIntString("message.preparescanner")); // NOI18N
        jLabelScaleVPM.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabelScaleVPM.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel1.add(jLabelScaleVPM);
        jLabelScaleVPM.setBounds(10, 10, 440, 70);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("pos_messages"); // NOI18N
  //      jLabelScaleVPM.getAccessibleContext().setAccessibleName(bundle.getString("message.preparemercury130")); // NOI18N

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-474)/2, (screenSize.height-161)/2, 474, 161);
    }// </editor-fold>//GEN-END:initComponents

    private void jcmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdCancelActionPerformed
        dispose();
    }//GEN-LAST:event_jcmdCancelActionPerformed

    private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdOKActionPerformed

        String stext = jLabelScaleVPM.getText();
        jLabelScaleVPM.setText(AppLocal.getIntString("label.uploadingproducts"));
        jLabelScaleVPM.doLayout();
        jLabelScaleVPM.setEnabled(true);
        jLabelScaleVPM.setVisible(true);
        jcmdOK.setEnabled(false);
        jcmdCancel.setEnabled(false);

        try {
            m_scale.connectDevice();
            m_scale.startUploadProduct();
            ListModel l = m_bd.getListModel();
            int size = l.getSize();
            for (int i = 0; i < size; i++) {
//                System.out.println("iProduct = " + i);
                Object[] myprod = (Object[]) l.getElementAt(i);
                m_scale.sendProduct(
                        (String) myprod[3], // name
                        (String) myprod[2], // barcode
                        (Double) myprod[7], // sell price
                        i+1,
                        size, m_sUserBarcode);
            }
            
            m_scale.stopUploadProduct();
            MessageInf msg = new MessageInf(MessageInf.SGN_SUCCESS, AppLocal.getIntString("message.scannerok"));
            m_scale.disconnectDevice();
            msg.show(this);
        } catch (DeviceScaleVPMException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.scannerfail"), e);
            msg.show(this);
        }

        jLabelScaleVPM.setText(stext);
        jcmdOK.setEnabled(true);
        jcmdCancel.setEnabled(true);
        dispose();
    }//GEN-LAST:event_jcmdOKActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelScaleVPM;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jcmdCancel;
    private javax.swing.JButton jcmdOK;
    // End of variables declaration//GEN-END:variables
    
}
