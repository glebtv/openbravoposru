
package com.openbravo.pos.inventory;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.ticket.ProductInfoEdit;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JFrame;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class JProductEditDialog extends javax.swing.JDialog implements EditorCreator {

    private ProductInfoEdit m_oEditProduct;
    
    private SentenceList m_sentcat;
    private ComboBoxValModel m_CategoryModel;

    private SentenceList m_taxcatsent;
    private ComboBoxValModel m_TaxCategoryModel;  

    private SentenceList m_attsent;
    private ComboBoxValModel m_AttributeModel;
    
    private SentenceList taxsent;
    private TaxesLogic taxeslogic;
    
//    private AppView m_App;    
//    private DataLogicSales m_dlSales;
        
    private JProductEditDialog(Frame parent, boolean modal) {
        super(parent, modal);
    }

    private JProductEditDialog(Dialog parent, boolean modal) {
        super(parent, modal);
    }

    public static JProductEditDialog getProductEditor(Component parent, DataLogicSales dlSales) throws BasicException {
        Window window = getWindow(parent);

        JProductEditDialog myMsg;
        if (window instanceof Frame) {
            myMsg = new JProductEditDialog((Frame) window, true);
        } else {
            myMsg = new JProductEditDialog((Dialog) window, true);
        }
        myMsg.init(dlSales);
        myMsg.applyComponentOrientation(parent.getComponentOrientation());
        return myMsg;
    }

    public ProductInfoEdit getEditProduct() {
        return m_oEditProduct;
    }
    
    private void init(DataLogicSales dlSales) throws BasicException {

        initComponents();
                
        m_jReference.setEnabled(true);
        m_jName.setEnabled(true);        
        m_jBarcode.setEnabled(true);
        m_jBuyPrice.setEnabled(true);
        m_jSellPrice.setEnabled(true);
        
        m_jSellPriceTax.setEnabled(false);
        
        m_jName.addEditorKeys(m_jKeyboardKeys);
        m_jReference.addEditorKeys(m_jKeyboardKeys);
        m_jBarcode.addEditorKeys(m_jKeyboardKeys);
        m_jBuyPrice.addEditorKeys(m_jKeyboardKeys);
        m_jSellPrice.addEditorKeys(m_jKeyboardKeys);
        m_jSellPriceTax.addEditorKeys(m_jKeyboardKeys);
       
        m_jName.reset();
        m_jReference.reset();
        m_jBarcode.reset();
        m_jBuyPrice.reset();
        m_jSellPrice.reset();
        m_jSellPriceTax.reset();
        
        m_jName.activate();
        
        taxsent = dlSales.getTaxList();
        taxeslogic = new TaxesLogic(taxsent.list());   
        
        m_sentcat = dlSales.getCategoriesList();
        m_CategoryModel = new ComboBoxValModel(m_sentcat.list());
        m_jCategory.setModel(m_CategoryModel);


        m_taxcatsent = dlSales.getTaxCategoriesList();        
        m_TaxCategoryModel = new ComboBoxValModel(m_taxcatsent.list());
        m_jTax.setModel(m_TaxCategoryModel);

        m_attsent = dlSales.getAttributeSetList();
        m_AttributeModel = new ComboBoxValModel(m_attsent.list());
        m_AttributeModel.add(0, null);
        m_jAtt.setModel(m_AttributeModel);
        
        getRootPane().setDefaultButton(m_jButtonOK);
        
        m_oEditProduct = null;
    }
        
    public void editProductFields(ProductInfoEdit oProductCurrentEdit) throws BasicException {
        m_jName.setText(oProductCurrentEdit.getName());

        m_jReference.setText(oProductCurrentEdit.getReference());
        m_jBarcode.setText(oProductCurrentEdit.getCode());
        m_jBuyPrice.setDoubleValue(oProductCurrentEdit.getPriceBuy());
        m_jSellPrice.setDoubleValue(oProductCurrentEdit.getPriceSell());
        

        m_CategoryModel.setSelectedKey(oProductCurrentEdit.getCategoryID());
        m_TaxCategoryModel.setSelectedKey(oProductCurrentEdit.getTaxID());
        m_AttributeModel.setSelectedKey(oProductCurrentEdit.getAttributeUseID());
    }

    @Override
    public Object createValue() throws BasicException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
        private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }
    
    private class CalcSellPrice {
 
    }
    
    private class CalcSellPriceTax {
        Double dPriceSellTax = readCurrency(m_jSellPrice.getText());
        
        //m_jSellPrice.setDoubleValue(0.0);

//         double dTaxRate = taxeslogic.getTaxRate((TaxCategoryInfo) taxcatmodel.getSelectedItem(), new Date());
//         .setDoubleValue(dPriceSell.doubleValue() * (1.0 + dTaxRate));
    }
        
    private final static Double readCurrency(String sValue) {
        try {
            return (Double) Formats.CURRENCY.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }
        
    private final static Double readPercent(String sValue) {
        try {
            return (Double) Formats.PERCENT.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        m_jReference = new com.openbravo.editor.JKeyboardString();
        m_jCategory = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        m_jAtt = new javax.swing.JComboBox();
        m_jBarcode = new com.openbravo.editor.JKeyboardString();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        m_jName = new com.openbravo.editor.JKeyboardString();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        m_jBuyPrice = new com.openbravo.editor.JEditorCurrencyPositive();
        jLabel5 = new javax.swing.JLabel();
        m_jSellPriceTax = new com.openbravo.editor.JEditorCurrencyPositive();
        m_jSellPrice = new com.openbravo.editor.JEditorCurrencyPositive();
        jLabel6 = new javax.swing.JLabel();
        m_jTax = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_jKeyboardKeys = new com.openbravo.editor.JKeyboardKeys();
        jPanel3 = new javax.swing.JPanel();
        m_jButtonOK = new javax.swing.JButton();
        m_jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edit product");
        setResizable(false);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText(AppLocal.getIntString("label.prodref")); // NOI18N
        jLabel1.setBorder(null);
        jPanel6.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        m_jReference.setMinimumSize(new java.awt.Dimension(110, 25));
        m_jReference.setPreferredSize(new java.awt.Dimension(110, 25));
        jPanel6.add(m_jReference, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 160, -1));
        jPanel6.add(m_jCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 160, -1));

        jLabel13.setText(AppLocal.getIntString("label.attributes")); // NOI18N
        jLabel13.setBorder(null);
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));
        jPanel6.add(m_jAtt, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 160, -1));

        m_jBarcode.setMinimumSize(new java.awt.Dimension(160, 25));
        m_jBarcode.setPreferredSize(new java.awt.Dimension(160, 25));
        jPanel6.add(m_jBarcode, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, -1, -1));

        jLabel8.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N
        jLabel8.setBorder(null);
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jLabel2.setText(AppLocal.getIntString("label.prodbarcode")); // NOI18N
        jLabel2.setBorder(null);
        jPanel6.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jPanel5.add(jPanel6, java.awt.BorderLayout.WEST);

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jName.setMinimumSize(new java.awt.Dimension(330, 25));
        m_jName.setPreferredSize(new java.awt.Dimension(330, 25));
        m_jName.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                m_jNamePropertyChange(evt);
            }
        });
        jPanel7.add(m_jName, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 12, 359, -1));

        jLabel4.setText(AppLocal.getIntString("label.prodname")); // NOI18N
        jPanel7.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 12, 77, -1));

        jLabel3.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N
        jPanel7.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 46, 86, -1));
        jPanel7.add(m_jBuyPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 43, 118, -1));

        jLabel5.setText(AppLocal.getIntString("label.prodpriceselltax")); // NOI18N
        jPanel7.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 79, -1, -1));
        jPanel7.add(m_jSellPriceTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(355, 75, 118, -1));
        jPanel7.add(m_jSellPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(355, 43, 118, -1));

        jLabel6.setText(AppLocal.getIntString("label.prodpricesell")); // NOI18N
        jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 46, -1, -1));
        jPanel7.add(m_jTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 74, 118, -1));

        jLabel7.setText(AppLocal.getIntString("label.taxcategory")); // NOI18N
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 79, -1, -1));

        jPanel5.add(jPanel7, java.awt.BorderLayout.EAST);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        m_jKeyboardKeys.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 30, 0, 30));
        jPanel4.add(m_jKeyboardKeys, java.awt.BorderLayout.PAGE_START);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 30, 0, 30));
        jPanel3.setRequestFocusEnabled(false);

        m_jButtonOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        m_jButtonOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
        m_jButtonOK.setFocusPainted(false);
        m_jButtonOK.setFocusable(false);
        m_jButtonOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jButtonOK.setRequestFocusEnabled(false);
        m_jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jButtonOKActionPerformed(evt);
            }
        });
        jPanel3.add(m_jButtonOK);

        m_jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        m_jButtonCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
        m_jButtonCancel.setFocusPainted(false);
        m_jButtonCancel.setFocusable(false);
        m_jButtonCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jButtonCancel.setRequestFocusEnabled(false);
        m_jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jButtonCancelActionPerformed(evt);
            }
        });
        jPanel3.add(m_jButtonCancel);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_END);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-755)/2, (screenSize.height-522)/2, 755, 522);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jButtonOKActionPerformed

        m_oEditProduct = new ProductInfoEdit();       
        
        if (m_jName.getText() != null) m_oEditProduct.setName(m_jName.getText());
        if (m_jReference.getText() != null) m_oEditProduct.setReference(m_jReference.getText());
        if (m_jBarcode.getText() != null) m_oEditProduct.setCode(m_jBarcode.getText());
        
        m_oEditProduct.setCategoryID(m_CategoryModel.getSelectedKey().toString());
        m_oEditProduct.setTaxID(m_TaxCategoryModel.getSelectedKey().toString());
        if (m_AttributeModel.getSelectedKey() != null) m_oEditProduct.setAttributeUseID(m_AttributeModel.getSelectedKey().toString());
        
        m_oEditProduct.setPriceBuy(readCurrency(m_jBuyPrice.getText()));
        m_oEditProduct.setPriceSell(readCurrency(m_jSellPrice.getText()));
        
        dispose();
    }//GEN-LAST:event_m_jButtonOKActionPerformed

    private void m_jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jButtonCancelActionPerformed
        dispose();
    }//GEN-LAST:event_m_jButtonCancelActionPerformed

    private void m_jNamePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_m_jNamePropertyChange

    }//GEN-LAST:event_m_jNamePropertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JComboBox m_jAtt;
    private com.openbravo.editor.JKeyboardString m_jBarcode;
    private javax.swing.JButton m_jButtonCancel;
    private javax.swing.JButton m_jButtonOK;
    private com.openbravo.editor.JEditorCurrencyPositive m_jBuyPrice;
    private javax.swing.JComboBox m_jCategory;
    private com.openbravo.editor.JKeyboardKeys m_jKeyboardKeys;
    private com.openbravo.editor.JKeyboardString m_jName;
    private com.openbravo.editor.JKeyboardString m_jReference;
    private com.openbravo.editor.JEditorCurrencyPositive m_jSellPrice;
    private com.openbravo.editor.JEditorCurrencyPositive m_jSellPriceTax;
    private javax.swing.JComboBox m_jTax;
    // End of variables declaration//GEN-END:variables
}
