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

package com.openbravo.pos.util;

import com.openbravo.pos.inventory.BarCodeItem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class BarcodeBuilder {

    private Image m_imgdefault;
    private int m_width = 125;
    private int m_height = 125;
    private BufferedImage mainImage;
    private java.util.List<BarCodeItem> products;
    private Graphics graphics;
    private File barcodeFolder;
    public BarcodeBuilder() {
    }

    private  int startX = 10, startY = 10, rowItem = 7, currentIndex = 1, fileCnt = 1;
    boolean isSave = false;
    public BarcodeBuilder renderImage(Collection<BarCodeItem> products) {
        if (products.size() <= 0) {
            return this;
        }
        barcodeFolder = new File("C:\\barcode");
        barcodeFolder.mkdir();
        mainImage = createMainImage();
        graphics = mainImage.createGraphics();
        startX = 10;
        startY = 10;
        rowItem = 7;
        fileCnt = 1;
        currentIndex = 1;
        isSave = false;
        for (BarCodeItem p : products) {
            renderImage(p);
        }
        if (!isSave) {
            graphics.dispose();
            writeImage(fileCnt++ + ".jpg");
        }
        return this;
    }

    private void renderImage(BarCodeItem p){
        int loop = p.getSizeQ();
        System.out.println("xuat " + p.getSource().getName() + " size: " + p.getSizeQ());
        for (int i =1 ; i <=loop; i++) {
            Image thumbNail = getThumbNailText(getThumbNail(p.getSource().getImage()), p.getSource().getName(), p.getSource().getCode());
            graphics.drawImage(thumbNail, startX, startY, null);
            System.out.println(startX + ":" +startY + " : " + currentIndex);
            if (currentIndex % rowItem == 0) {
                startY += 175;
                startX = 10;
            } else {
                startX += 135;
            }
            currentIndex++;
            isSave = false;
            if (currentIndex % 64 == 0) {
                currentIndex = 1;
                System.out.println(">>>>>>>>>>>>>>> reset: " + currentIndex + " fileCnt: " + fileCnt);
                //write first image
                isSave = true;
                writeImage(fileCnt++ + ".jpg");
                //reset
                graphics.dispose();
                mainImage = createMainImage();
                graphics = mainImage.createGraphics();
                startX = 10;
                startY = 10;
                rowItem = 7;
            }
        }
    }

    public void writeImage(String fileName) {
        try {
            ImageIO.write(mainImage, "jpg", new File(barcodeFolder +"\\"+ fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(int width, int height, Image imgdef) {
        m_width = width;
        m_height = height;
        if (imgdef == null) {
            m_imgdefault = null;
        } else {
            m_imgdefault = createThumbNail(imgdef);
        } 
    }
    
    public Image getThumbNail(Image img) {
   
        if (img == null) {
            return m_imgdefault;
        } else {
            return createThumbNail(img);
        }     
    }      
    
    public Image getThumbNailText(Image img, String nameText, String codeText) {
                
        img = getThumbNail(img);
        
        BufferedImage imgtext = new BufferedImage(127, 165,  BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imgtext.createGraphics();
        g2d.setBackground(Color.white);
        // The text        
        JLabel code = new JLabel();
        code.setOpaque(false);
        code.setText(codeText);
        code.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        code.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        code.setBounds(0, 0, 130, 18);
        
        g2d.drawImage(img, 1, 1, null);
        g2d.translate(0, 127);
        g2d.fillRect(1 , 0, imgtext.getWidth() - 2, code.getHeight());
        code.paint(g2d);

        JLabel name = new JLabel();
        name.setOpaque(false);
        name.setText(nameText);
        name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        name.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        name.setBounds(0, 0, 130, 18);

        g2d.translate(0, 19);
        g2d.setColor(Color.white);
        g2d.fillRect(1 , 0, imgtext.getWidth() - 2, name.getHeight());
        name.paint(g2d);

        g2d.dispose();
        
        return imgtext;    
    }
    
    private Image createThumbNail(Image img) {
//            MaskFilter filter = new MaskFilter(Color.WHITE);
//            ImageProducer prod = new FilteredImageSource(img.getSource(), filter);
//            img = Toolkit.getDefaultToolkit().createImage(prod);
            
        int targetw;
        int targeth;

        double scalex = (double) m_width / (double) img.getWidth(null);
        double scaley = (double) m_height / (double) img.getHeight(null);
        if (scalex < scaley) {
            targetw = m_width;
            targeth = (int) (img.getHeight(null) * scalex);
        } else {
            targetw = (int) (img.getWidth(null) * scaley);
            targeth = (int) m_height;
        }

        int midw = img.getWidth(null);
        int midh = img.getHeight(null);
        BufferedImage midimg = null;
        Graphics2D g2d = null;

        Image previmg = img;
        int prevw = img.getWidth(null);
        int prevh = img.getHeight(null);

        do {
            if (midw > targetw) {
                midw /= 2;
                if (midw < targetw) {
                    midw = targetw;
                }
            } else {
                midw = targetw;
            }
            if (midh > targeth) {
                midh /= 2;
                if (midh < targeth) {
                    midh = targeth;
                }
            } else {
                midh = targeth;
            }
            if (midimg == null) {
                midimg = new BufferedImage(midw, midh, BufferedImage.TYPE_INT_ARGB);
                g2d = midimg.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            }
            g2d.drawImage(previmg, 0, 0, midw, midh, 0, 0, prevw, prevh, null);
            prevw = midw;
            prevh = midh;
            previmg = midimg;
        } while (midw != targetw || midh != targeth);

        g2d.dispose();

        if (m_width != midimg.getWidth() || m_height != midimg.getHeight()) {
            midimg = new BufferedImage(m_width, m_height, BufferedImage.TYPE_INT_ARGB);
            int x = (m_width > targetw) ? (m_width - targetw) / 2 : 0;
            int y = (m_height > targeth) ? (m_height - targeth) / 2 : 0;
            g2d = midimg.createGraphics();
            g2d.drawImage(previmg, x, y, x + targetw, y + targeth,
                                   0, 0, targetw, targeth, null);
            g2d.dispose();
            previmg = midimg;
        } 
        return previmg;           
    }

    public BufferedImage createMainImage() {

        BufferedImage image = new BufferedImage(970, 1600, BufferedImage.TYPE_INT_RGB);
        image.setRGB(255, 255, 255);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.fillRect(0, 0, 970, 1600);
        graphics.dispose();
        return image;
    }
}
