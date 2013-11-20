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

import com.openbravo.pos.ticket.ProductInfoExt;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author adrianromero
 */
public class BarCodeItem extends JPanel {

    private int sizeQ;
    private JTextField quantity;
    private ProductInfoExt source;

    public ProductInfoExt getSource() {
        return source;
    }

    public void setSource(ProductInfoExt source) {
        this.source = source;
    }

    public int getSizeQ() {
        return sizeQ;
    }

    public void setSizeQ(int sizeQ) {
        this.sizeQ = sizeQ;
    }

    BarCodeItem(JLabel bt, int size, String name, ProductInfoExt source) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(bt);
        JLabel pName = new JLabel(name);
        pName.setHorizontalAlignment(SwingConstants.CENTER);
        pName.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
        this.add(pName);
        quantity = new JTextField("" + size);
        quantity.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
        quantity.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                String text = quantity.getText();
                if ("".equals(text)) {
                    sizeQ = 0;
                } else {
                    sizeQ = Integer.parseInt(text);
                }
            }
        });
        //quantity.setMargin(new Insets(0 , -2 , 0, 0));
        this.add(quantity);
        this.setPreferredSize(new Dimension(120, 120));
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.sizeQ = size;
        this.source = source;
    }



    public static BarCodeItem newItem(Icon ico, int size, String name, ProductInfoExt source) {
        JLabel  btn = new JLabel ();
        btn.setIcon(ico);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        //btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        //btn.setMargin(new Insets(2, 0, 2, 0));
        btn.setPreferredSize(new Dimension(Short.MAX_VALUE,70));
        BarCodeItem item = new BarCodeItem(btn, size, name, source);

        return item;
    }

    public void incQuantity() {
        this.sizeQ += 1;
        this.quantity.setText(""  + this.sizeQ);
        this.quantity.grabFocus();
        this.quantity.selectAll();
    }
}
