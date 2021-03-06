//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.panels;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author adrianromero
 * @author Andrey Svininykh <svininykh@gmail.com>
 * @author Algaja 
 */
public class  PaymentsModel {

    private String m_sHost;
    private int m_iSeq;
    private Date m_dDateStart;
    private Date m_dDateEnd;       
            
    private Integer m_iPayments;
    private Double m_dPaymentsTotal;
    private java.util.List<PaymentsLine> m_lpayments;
    
    private final static String[] PAYMENTHEADERS = {"Label.Payment", "label.totalcash"};
    
    private Integer m_iSales;
    private Double m_dSalesBase;
    private Double m_dSalesTaxes;
    private java.util.List<SalesLine> m_lsales;
    
    private final static String[] SALEHEADERS = {"label.taxcash", "label.totalcash"};

    private Integer m_iProductSalesRows;
    private Double m_dProductSalesTotalUnits;
    private Double m_dProductSalesTotal;
    private java.util.List<ProductSalesLine> m_lproductsales;
    
    private java.util.List<CategorySalesLine> m_lcategorysales;    
    
    private List<PaymentsModel.CashiersLine> m_lcashiers;    

    private PaymentsModel() {
    }    
    
    public static PaymentsModel emptyInstance() {
        
        PaymentsModel p = new PaymentsModel();
        
        p.m_iPayments = new Integer(0);
        p.m_dPaymentsTotal = new Double(0.0);
        p.m_lpayments = new ArrayList<PaymentsLine>();
        
        p.m_iSales = null;
        p.m_dSalesBase = null;
        p.m_dSalesTaxes = null;
        p.m_lsales = new ArrayList<SalesLine>();
        
        p.m_iProductSalesRows = new Integer(0);
        p.m_dProductSalesTotalUnits = new Double(0.0);
        p.m_dProductSalesTotal = new Double(0.0);
        p.m_lproductsales = new ArrayList<ProductSalesLine>();
        
        p.m_lcategorysales = new ArrayList<CategorySalesLine>();  
        p.m_lcashiers = new ArrayList<CashiersLine>();   

        return p;
    }
    
    public static PaymentsModel loadInstance(AppView app) throws BasicException {
        
        PaymentsModel p = new PaymentsModel();
        
        // Propiedades globales
        p.m_sHost = app.getProperties().getHost();
        p.m_iSeq = app.getActiveCashSequence();
        p.m_dDateStart = app.getActiveCashDateStart();
        p.m_dDateEnd = null;
        
        
        // Pagos
        Object[] valtickets = (Object []) new StaticSentence(app.getSession()
            , "SELECT COUNT(*), SUM(PAYMENTS.TOTAL) " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE}))
            .find(app.getActiveCashIndex());
            
        if (valtickets == null) {
            p.m_iPayments = new Integer(0);
            p.m_dPaymentsTotal = new Double(0.0);
        } else {
            p.m_iPayments = (Integer) valtickets[0];
            p.m_dPaymentsTotal = (Double) valtickets[1];
        }  
        
        List l = new StaticSentence(app.getSession()            
            , "SELECT PAYMENTS.PAYMENT, SUM(PAYMENTS.TOTAL) " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ? " +
              "GROUP BY PAYMENTS.PAYMENT"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(PaymentsModel.PaymentsLine.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
            .list(app.getActiveCashIndex()); 
        
        if (l == null) {
            p.m_lpayments = new ArrayList();
        } else {
            p.m_lpayments = l;
        }        
        
        // Sales
        Object[] recsales = (Object []) new StaticSentence(app.getSession(),
            "SELECT COUNT(DISTINCT RECEIPTS.ID), SUM(TICKETLINES.UNITS * TICKETLINES.PRICE) " +
            "FROM RECEIPTS, TICKETLINES WHERE RECEIPTS.ID = TICKETLINES.TICKET AND RECEIPTS.MONEY = ?",
            SerializerWriteString.INSTANCE,
            new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE}))
            .find(app.getActiveCashIndex());
        if (recsales == null) {
            p.m_iSales = null;
            p.m_dSalesBase = null;
        } else {
            p.m_iSales = (Integer) recsales[0];
            p.m_dSalesBase = (Double) recsales[1];
        }             
        
        // Taxes
        Object[] rectaxes = (Object []) new StaticSentence(app.getSession(),
            "SELECT SUM(TAXLINES.AMOUNT) " +
            "FROM RECEIPTS, TAXLINES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND RECEIPTS.MONEY = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
            .find(app.getActiveCashIndex());            
        if (rectaxes == null) {
            p.m_dSalesTaxes = null;
        } else {
            p.m_dSalesTaxes = (Double) rectaxes[0];
        } 
                
        // Product Sales
        Object[] valproductsales = (Object []) new StaticSentence(app.getSession()
            , "SELECT COUNT(*), SUM(TICKETLINES.UNITS), SUM((TICKETLINES.PRICE + TICKETLINES.PRICE * TAXES.RATE ) * TICKETLINES.UNITS) " +
              "FROM TICKETLINES, TICKETS, RECEIPTS, TAXES " +
              "WHERE TICKETLINES.TICKET = TICKETS.ID AND TICKETS.ID = RECEIPTS.ID AND TICKETLINES.TAXID = TAXES.ID AND TICKETLINES.PRODUCT IS NOT NULL AND RECEIPTS.MONEY = ? " +
              "GROUP BY RECEIPTS.MONEY"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE, Datas.DOUBLE}))
            .find(app.getActiveCashIndex());

        if (valproductsales == null) {
            p.m_iProductSalesRows = new Integer(0);
            p.m_dProductSalesTotalUnits = new Double(0.0);
            p.m_dProductSalesTotal = new Double(0.0);
        } else {
            p.m_iProductSalesRows = (Integer) valproductsales[0];
            p.m_dProductSalesTotalUnits = (Double) valproductsales[1];
            p.m_dProductSalesTotal= (Double) valproductsales[2];
        }

        List products = new StaticSentence(app.getSession()
            , "SELECT PRODUCTS.NAME, SUM(TICKETLINES.UNITS), TICKETLINES.PRICE, TAXES.RATE, CATEGORIES.NAME " +
              "FROM TICKETLINES, TICKETS, RECEIPTS, PRODUCTS, TAXES, CATEGORIES " +
              "WHERE TICKETLINES.PRODUCT = PRODUCTS.ID AND TICKETLINES.TICKET = TICKETS.ID AND TICKETS.ID = RECEIPTS.ID AND TICKETLINES.TAXID = TAXES.ID AND CATEGORIES.ID = PRODUCTS.CATEGORY AND RECEIPTS.MONEY = ? " +
              "GROUP BY CATEGORIES.NAME,  PRODUCTS.NAME, TICKETLINES.PRICE, TAXES.RATE"                
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(PaymentsModel.ProductSalesLine.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
            .list(app.getActiveCashIndex());

        if (products == null) {
            p.m_lproductsales = new ArrayList();
        } else {
            p.m_lproductsales = products;
        }
        
       // Product category Sales
        
        List categorys = new StaticSentence(app.getSession()
            , "SELECT CATEGORIES.NAME, SUM(TICKETLINES.UNITS), SUM(TICKETLINES.UNITS * (TICKETLINES.PRICE + (TICKETLINES.PRICE * TAXES.RATE))) " +
              "FROM CATEGORIES " +
              "LEFT JOIN PRODUCTS ON CATEGORIES.ID = PRODUCTS.CATEGORY " +
              "LEFT JOIN TICKETLINES ON PRODUCTS.ID = TICKETLINES.PRODUCT " +
              "LEFT JOIN TAXES ON TICKETLINES.TAXID = TAXES.ID " +
              "LEFT JOIN RECEIPTS ON TICKETLINES.TICKET = RECEIPTS.ID " +
              "WHERE RECEIPTS.MONEY = ? " +
              "GROUP BY CATEGORIES.NAME"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(PaymentsModel.CategorySalesLine.class))
            .list(app.getActiveCashIndex());

        if (categorys == null) {
            p.m_lcategorysales = new ArrayList();
        } else {
            p.m_lcategorysales = categorys;
        }        
        
        List cashiers = new StaticSentence(app.getSession()
             , "SELECT PEOPLE.NAME, SUM(TICKETLINES.UNITS), SUM(TICKETLINES.UNITS * (TICKETLINES.PRICE + (TICKETLINES.PRICE * TAXES.RATE))) " +
               "FROM PEOPLE " +
               "LEFT JOIN TICKETS ON PEOPLE.ID = TICKETS.PERSON " +
               "LEFT JOIN TICKETLINES ON TICKETS.ID = TICKETLINES.TICKET " +
               "LEFT JOIN TAXES ON TICKETLINES.TAXID = TAXES.ID " +
               "LEFT JOIN RECEIPTS ON TICKETLINES.TICKET = RECEIPTS.ID " +
               "WHERE RECEIPTS.MONEY = ? " +
               "GROUP BY PEOPLE.NAME"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(CashiersLine.class))
            .list(app.getActiveCashIndex());
        
        if (cashiers == null) {
            p.m_lcashiers = new ArrayList();
        } else {
            p.m_lcashiers = cashiers;
        }

        List<SalesLine> asales = new StaticSentence(app.getSession(),
                "SELECT TAXCATEGORIES.NAME, SUM(TAXLINES.AMOUNT) " +
                "FROM RECEIPTS, TAXLINES, TAXES, TAXCATEGORIES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND TAXLINES.TAXID = TAXES.ID AND TAXES.CATEGORY = TAXCATEGORIES.ID " +
                "AND RECEIPTS.MONEY = ?" +
                "GROUP BY TAXCATEGORIES.NAME"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(SalesLine.class))
                .list(app.getActiveCashIndex());
        if (asales == null) {
            p.m_lsales = new ArrayList<SalesLine>();
        } else {
            p.m_lsales = asales;
        }
         
        return p;
    }

    public int getPayments() {
        return m_iPayments.intValue();
    }
    public double getTotal() {
        return m_dPaymentsTotal.doubleValue();
    }
    public String getHost() {
        return m_sHost;
    }
    public int getSequence() {
        return m_iSeq;
    }
    public Date getDateStart() {
        return m_dDateStart;
    }
    public void setDateEnd(Date dValue) {
        m_dDateEnd = dValue;
    }
    public Date getDateEnd() {
        return m_dDateEnd;
    }
    
    public String printHost() {
        return StringUtils.encodeXML(m_sHost);
    }
    public String printSequence() {
        return Formats.INT.formatValue(m_iSeq);
    }
    public String printDateStart() {
        return Formats.TIMESTAMP.formatValue(m_dDateStart);
    }
    public String printDateEnd() {
        return Formats.TIMESTAMP.formatValue(m_dDateEnd);
    }  
    
    public String printPayments() {
        return Formats.INT.formatValue(m_iPayments);
    }

    public String printPaymentsTotal() {
        return Formats.CURRENCY.formatValue(m_dPaymentsTotal);
    }     
    
    public List<PaymentsLine> getPaymentLines() {
        return m_lpayments;
    }
    
    public int getSales() {
        return m_iSales == null ? 0 : m_iSales.intValue();
    }    
    public String printSales() {
        return Formats.INT.formatValue(m_iSales);
    }
    public String printSalesBase() {
        return Formats.CURRENCY.formatValue(m_dSalesBase);
    }     
    public String printSalesTaxes() {
        return Formats.CURRENCY.formatValue(m_dSalesTaxes);
    }     
    public String printSalesTotal() {            
        return Formats.CURRENCY.formatValue((m_dSalesBase == null || m_dSalesTaxes == null)
                ? null
                : m_dSalesBase + m_dSalesTaxes);
    }     
    public List<SalesLine> getSaleLines() {
        return m_lsales;
    }
    
    public AbstractTableModel getPaymentsModel() {
        return new AbstractTableModel() {
            public String getColumnName(int column) {
                return AppLocal.getIntString(PAYMENTHEADERS[column]);
            }
            public int getRowCount() {
                return m_lpayments.size();
            }
            public int getColumnCount() {
                return PAYMENTHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                PaymentsLine l = m_lpayments.get(row);
                switch (column) {
                case 0: return l.getType();
                case 1: return l.getValue();
                default: return null;
                }
            }  
        };
    }
    
    public static class SalesLine implements SerializableRead {
        
        private String m_SalesTaxName;
        private Double m_SalesTaxes;
        
        public void readValues(DataRead dr) throws BasicException {
            m_SalesTaxName = dr.getString(1);
            m_SalesTaxes = dr.getDouble(2);
        }
        public String printTaxName() {
            return StringUtils.encodeXML(m_SalesTaxName);
        }      
        public String printTaxes() {
            return Formats.CURRENCY.formatValue(m_SalesTaxes);
        }
        public String getTaxName() {
            return m_SalesTaxName;
        }
        public Double getTaxes() {
            return m_SalesTaxes;
        }        
    }

    public AbstractTableModel getSalesModel() {
        return new AbstractTableModel() {
            public String getColumnName(int column) {
                return AppLocal.getIntString(SALEHEADERS[column]);
            }
            public int getRowCount() {
                return m_lsales.size();
            }
            public int getColumnCount() {
                return SALEHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                SalesLine l = m_lsales.get(row);
                switch (column) {
                case 0: return l.getTaxName();
                case 1: return l.getTaxes();
                default: return null;
                }
            }  
        };
    }
    
    public static class PaymentsLine implements SerializableRead {
        
        private String m_PaymentType;
        private Double m_PaymentValue;
        
        public void readValues(DataRead dr) throws BasicException {
            m_PaymentType = dr.getString(1);
            m_PaymentValue = dr.getDouble(2);
        }
        
        public String printType() {
            return AppLocal.getIntString("transpayment." + m_PaymentType);
        }
        public String getType() {
            return m_PaymentType;
        }
        public String printValue() {
            return Formats.CURRENCY.formatValue(m_PaymentValue);
        }
        public Double getValue() {
            return m_PaymentValue;
        }        
    }

    public double getProductSalesRows() {
        return m_iProductSalesRows.intValue();
    }

    public String printProductSalesRows() {
        return Formats.INT.formatValue(m_iProductSalesRows);
    }

    public double getProductSalesTotalUnits() {
        return m_dProductSalesTotalUnits.doubleValue();
    }

    public String printProductSalesTotalUnits() {
        return Formats.DOUBLE.formatValue(m_dProductSalesTotalUnits);
    }

    public double getProductSalesTotal() {
        return m_dProductSalesTotal.doubleValue();
    }

    public String printProductSalesTotal() {
        return Formats.CURRENCY.formatValue(m_dProductSalesTotal);
    }

    public List<ProductSalesLine> getProductSalesLines() {
        return m_lproductsales;
    }

    public static class ProductSalesLine implements SerializableRead {
        
        private String m_ProductName;
        private String m_ProductCategory;        
        private Double m_ProductUnits;
        private Double m_ProductPrice;
        private Double m_TaxRate;
        private Double m_ProductPriceTax;
        
        public void readValues(DataRead dr) throws BasicException {
            m_ProductName = dr.getString(1);
            m_ProductUnits = dr.getDouble(2);
            m_ProductPrice = dr.getDouble(3);
            m_TaxRate = dr.getDouble(4);
            m_ProductCategory = dr.getString(5);            

            m_ProductPriceTax = m_ProductPrice + m_ProductPrice * m_TaxRate;
        }
        
        public String printProductName() {
            return StringUtils.encodeXML(m_ProductName);
        }

        public String printProductUnits() {
            return Formats.DOUBLE.formatValue(m_ProductUnits);
        }

        public Double getProductUnits() {
            return m_ProductUnits;
        }

        public String printProductPrice() {
            return Formats.CURRENCY.formatValue(m_ProductPrice);
        }        

        public Double getProductPrice() {
            return m_ProductPrice;
        }

        public String printTaxRate() {
            return Formats.PERCENT.formatValue(m_TaxRate);
        }        

        public Double getTaxRate() {
            return m_TaxRate;
        } 
    
        public String printProductPriceTax() {
            return Formats.CURRENCY.formatValue(m_ProductPriceTax);
        }

        public String printProductCategory() {
            return StringUtils.encodeXML(m_ProductCategory);
        }        

        public String printProductSubValue() {
            return Formats.CURRENCY.formatValue(m_ProductPriceTax * m_ProductUnits);
        }
    }
    
    public List<CategorySalesLine> getCategorySalesLines() {
        return m_lcategorysales;
    }

    // Products category sales class
    
    public static class CategorySalesLine implements SerializableRead {

        private String m_CategoryName;
        private Double m_CategoryUnits;
        private Double m_CategorySum;

        public void readValues(DataRead dr) throws BasicException {
            m_CategoryName = dr.getString(1);
            m_CategoryUnits = dr.getDouble(2);
            m_CategorySum = dr.getDouble(3);
        }

        public String printCategoryName() {
            return StringUtils.encodeXML(m_CategoryName);
        }

        public String printCategoryUnits() {
            return Formats.DOUBLE.formatValue(m_CategoryUnits);
        }

        public Double getCategoryUnits() {
            return m_CategoryUnits;
        }

        public String printCategorySum() {
            return Formats.CURRENCY.formatValue(m_CategorySum);
        }

        public Double getCategorySum() {
            return m_CategorySum;
        }
    }    
    
    public List<CashiersLine> getCashiersLines() {
        return m_lcashiers;
    }

    public static class CashiersLine implements SerializableRead {
        
        private String m_sCashierName;
        private Double m_dCashierUnits;
        private Double m_dCashierValue;
        
        public void readValues(DataRead dr) throws BasicException {
            m_sCashierName = dr.getString(1);
            m_dCashierUnits = dr.getDouble(2);            
            m_dCashierValue = dr.getDouble(3);
        }
        
        public String printCashierName() {
            return StringUtils.encodeXML(m_sCashierName);
        }
        public String getCashierName() {
            return m_sCashierName;
        }
        public String printCashierUnits() {
            return Formats.DOUBLE.formatValue(m_dCashierUnits);
        }
        public Double getCashierUnits() {
            return m_dCashierUnits;
        }        
        public String printCashierValue() {
            return Formats.CURRENCY.formatValue(m_dCashierValue);
        }
        public Double getCashierValue() {
            return m_dCashierValue;
        }        
    }     
}