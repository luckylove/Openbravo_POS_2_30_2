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
import com.openbravo.data.gui.*;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.*;
import com.openbravo.pos.forms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author adrianromero
 */
public abstract class JPanelToolBar extends JPanel implements JPanelView, BeanFactoryApp {

    protected BrowsableEditableData bd;
    protected DirtyManager dirty;
    protected AppView app;
    private JTextField search;

    /** Creates new form JPanelTableEditor */
    public JPanelToolBar() {

        initComponents();
    }

    public void init(AppView app) throws BeanFactoryException {

        this.app = app;
        dirty = new DirtyManager();
        bd = null;

        init();
    }

    public Object getBean() {
        return this;
    }

    protected void startNavigation() {

        if (bd == null) {

            // init browsable editable data
            bd = new BrowsableEditableData(getListProvider(), getSaveProvider(), getEditor(), dirty);

            // Add the filter panel
            Component c = getFilter();
            if (c != null) {
                c.applyComponentOrientation(getComponentOrientation());
                add(c, BorderLayout.NORTH);
            }

            // Add the editor
            c = getEditor().getComponent();
            if (c != null) {
                c.applyComponentOrientation(getComponentOrientation());
                container.add(c, BorderLayout.CENTER);
            }

            // el panel este
            ListCellRenderer cr = getListCellRenderer();
            if (cr != null) {
                JListNavigator nl = new JListNavigator(bd);
                nl.applyComponentOrientation(getComponentOrientation());
                if (cr != null) nl.setCellRenderer(cr);
                container.add(nl, BorderLayout.LINE_START);
            }

            // add toolbar extras
            JLabel sLb = new JLabel("Tim kiếm");
            toolbar.add(sLb);
            search = new JTextField();
            search.setPreferredSize(new Dimension(150, 25));
            search.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    searchProducts();
                }
            });
            toolbar.add(search);
            JButton ok = new JButton("Ok");
            ok.setFocusPainted(false);
            ok.setFocusable(false);
            ok.setRequestFocusEnabled(false);
            ok.setMargin(new java.awt.Insets(0, 8, 0, 8));

            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    searchProducts();
                }
            });
            toolbar.add(ok);

            JButton preview = new JButton("In ra File");
            preview.setMargin(new java.awt.Insets(0, 8, 0, 8));
            preview.setFocusable(false);
            preview.setRequestFocusEnabled(false);
            preview.setMargin(new java.awt.Insets(0, 8, 0, 8));
            preview.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    exportImage();
                }
            });
            toolbar.add(preview);

            JButton remove = new JButton("Xóa toàn bộ");
            remove.setMargin(new java.awt.Insets(0, 8, 0, 8));
            remove.setFocusable(false);
            remove.setRequestFocusEnabled(false);
            remove.setMargin(new java.awt.Insets(0, 8, 0, 8));
            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeAll();
                }
            });
            toolbar.add(remove);

        }
    }

    public abstract void exportImage();
    public abstract void removeAll();

    public void searchProducts(){
        searchProducts(search.getText());
    }

    public abstract void searchProducts(String query);

    public Component getToolbarExtras() {
        return null;
    }

    public Component getFilter() {
        return null;
    }

    protected abstract void init();

    public abstract EditorRecord getEditor();

    public abstract ListProvider getListProvider();

    public abstract SaveProvider getSaveProvider();

    public Vectorer getVectorer() {
        return null;
    }

    public ComparatorCreator getComparatorCreator() {
        return null;
    }

    public ListCellRenderer getListCellRenderer() {
        return null;
    }

    public JComponent getComponent() {
        return this;
    }

    public void activate() throws BasicException {
        startNavigation();
        bd.actionLoad();
    }

    public boolean deactivate() {

        try {
            return bd.actionClosingForm(this);
        } catch (BasicException eD) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.CannotMove"), eD);
            msg.show(this);
            return false;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        container = new JPanel();
        toolbar = new JPanel();

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout());

        container.setLayout(new BorderLayout());
        container.add(toolbar, BorderLayout.NORTH);

        add(container, BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel container;
    private JPanel toolbar;
    // End of variables declaration//GEN-END:variables
    
}
