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

package com.openbravo.pos.inventory;

import com.openbravo.basic.BasicException;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.pos.catalog.CatalogSelector;
import com.openbravo.pos.catalog.JCatalog;
import com.openbravo.pos.catalog.JCatalogTab;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.util.ThumbNailBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author adrianromero
 */
public class BarCodeEditor extends javax.swing.JPanel implements EditorRecord {

    private CatalogSelector m_cat;


    private AppView m_App;
    private DataLogicSales m_dlSales;
    private Map<String, BarCodeItem> componentMap ;

    public Map<String, BarCodeItem> getComponentMap() {
        return componentMap;
    }

    public void setComponentMap(Map<String, BarCodeItem> componentMap) {
        this.componentMap = componentMap;
    }

    /** Creates new form StockDiaryEditor */

    private JCatalogTab listBarcode;
    private ThumbNailBuilder tnbbutton;
    public BarCodeEditor(AppView app, DirtyManager dirty) {

        m_App = app;
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");

        initComponents();

        m_cat = new JCatalog(m_dlSales);
        m_cat.getComponent().setPreferredSize(new Dimension(0, 245));
        m_cat.addActionListener(new CatalogListener());
        add(m_cat.getComponent(), BorderLayout.NORTH);

        listBarcode = new JCatalogTab();
        listBarcode.applyComponentOrientation(getComponentOrientation());
        add(listBarcode, BorderLayout.CENTER);

        tnbbutton = new ThumbNailBuilder(120, 80, "com/openbravo/images/package.png");
        componentMap = new HashMap<String, BarCodeItem>();
    }

    public void activate() throws BasicException {
        m_cat.loadCatalog();
    }

    public void removeAllBarCode(){
        listBarcode.removeAllProduct();
        listBarcode.revalidate();
        listBarcode.setVisible(false);
        listBarcode.setVisible(true);
        componentMap = new HashMap<String, BarCodeItem>();
    }

    @Override
    public void writeValueEOF() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeValueInsert() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeValueEdit(Object value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeValueDelete(Object value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void refresh() {
    }

    public CatalogSelector getM_cat() {
        return m_cat;
    }

    public void setM_cat(CatalogSelector m_cat) {
        this.m_cat = m_cat;
    }

    public Component getComponent() {
        return this;
    }

    @Override
    public Object createValue() throws BasicException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private class CatalogListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ProductInfoExt productInfoExt = (ProductInfoExt) e.getSource();
            BarCodeItem barCodeItem = componentMap.get(productInfoExt.getID());
            if (barCodeItem == null) {
                ImageIcon imageIcon = new ImageIcon(tnbbutton.getThumbNail(productInfoExt.getImage()));
                barCodeItem = BarCodeItem.newItem(imageIcon, 1, productInfoExt.getName(), productInfoExt);
                listBarcode.addPanel(barCodeItem);
                listBarcode.validate();
                componentMap.put(productInfoExt.getID(), barCodeItem);
            } else {
                barCodeItem.incQuantity();
            }

        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());

        //add(jPanel1, BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    
}
