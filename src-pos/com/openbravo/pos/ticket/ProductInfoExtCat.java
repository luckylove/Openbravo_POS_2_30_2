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

package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.ImageUtils;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.format.Formats;

import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 *
 * @author adrianromero
 *
 */
public class ProductInfoExtCat extends ProductInfoExt{

    private static final long serialVersionUID = 7587696873036L;
    protected String categoryName;

    /** Creates new ProductInfo */
    public ProductInfoExtCat() {
        super();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }



    public static SerializerRead getSerializerRead() {
        return new SerializerRead() { public Object readValues(DataRead dr) throws BasicException {
            ProductInfoExtCat product = new ProductInfoExtCat();
            product.m_ID = dr.getString(1);
            product.m_sRef = dr.getString(2);
            product.m_sCode = dr.getString(3);
            product.m_sName = dr.getString(4);
            product.m_bCom = dr.getBoolean(5).booleanValue();
            product.m_bScale = dr.getBoolean(6).booleanValue();
            product.m_dPriceBuy = dr.getDouble(7).doubleValue();
            product.m_dPriceSell = dr.getDouble(8).doubleValue();
            product.taxcategoryid = dr.getString(9);
            product.categoryid = dr.getString(10);
            product.attributesetid = dr.getString(11);
            product.m_Image = ImageUtils.readImage(dr.getBytes(12));
            product.attributes = ImageUtils.readProperties(dr.getBytes(13));
            product.categoryName = dr.getString(14);
            return product;
        }};
    }


}
