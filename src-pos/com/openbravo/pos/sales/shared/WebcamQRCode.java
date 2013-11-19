package com.openbravo.pos.sales.shared;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.*;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.JPanelTicketSales;
import com.openbravo.pos.sales.TicketsEditor;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.ProductInfoExtCat;
import com.openbravo.pos.util.SpringUtilities;
import com.openbravo.pos.util.StringUtils;

/**
 * User: son.nguyen
 * Date: 11/17/13
 * Time: 6:56 PM
 */
public class WebcamQRCode extends JFrame implements ActionListener {

    private static final long serialVersionUID = 6441489157408381878L;

    private DataLogicSales m_dlSales = null;
    private JPanelTicketSales panelticket = null;

    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private JTextArea textarea = null;
    private JTextField barCodeText = null;
    private JTextField categoryText = null;
    private JTextField quantity = null;
    private JButton done = null;
    private boolean runJob = false;

    private JButton m_webcam;

    private ProductInfoExtCat productInfoExt;


    public boolean isRunJob() {
        return runJob;
    }

    public void setRunJob(boolean runJob) {
        this.runJob = runJob;
    }

    public WebcamQRCode(DataLogicSales m_dlSales, TicketsEditor panelticket, JButton m_webcam) {
        super();

        this.m_dlSales = m_dlSales;
        if (panelticket instanceof JPanelTicketSales) {
            this.panelticket = (JPanelTicketSales)panelticket;
        }

        this.m_webcam = m_webcam;
        setLayout(new FlowLayout());
        setTitle("Read QR / Bar Code With Webcam");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        Dimension size = WebcamResolution.QVGA.getSize();

        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);

        panel = new WebcamPanel(webcam, false);
        panel.setPreferredSize(size);

        JPanel righP = new JPanel();
        righP.setLayout(new BoxLayout(righP, BoxLayout.Y_AXIS));
        righP.setPreferredSize(size);

        barCodeText = new JTextField();
        barCodeText.setEditable(false);
        barCodeText.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));

        categoryText = new JTextField();
        categoryText.setEditable(false);
        categoryText.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));

        JPanel tP = new JPanel();
        tP.setLayout(new BoxLayout(tP, BoxLayout.LINE_AXIS));
        tP.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        quantity = new JTextField();
        done = new JButton("Done");

        done.addActionListener(this);
        quantity.addActionListener(this);

        tP.add(quantity);
        tP.add(done);




        textarea = new JTextArea();
        textarea.setEditable(false);

        righP.add(barCodeText);
        righP.add(Box.createRigidArea(new Dimension(0,5)));
        righP.add(categoryText);
        righP.add(Box.createRigidArea(new Dimension(0,5)));
        righP.add(textarea);
        righP.add(Box.createRigidArea(new Dimension(0,5)));
        righP.add(tP);
        this.setLocationByPlatform(true);

        add(panel);
        add(righP);

        pack();
        //setVisible(true);
        final JButton m_webcamTmp = this.m_webcam;
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                panel.stop();
                runJob = false;
                m_webcamTmp.setEnabled(true);
            }
        });

    }

    private void resetValue(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
                runJob = true;
                textarea.setText("");
                categoryText.setText("");
                barCodeText.setText("");
                quantity.setText("");
                done.setEnabled(false);
                productInfoExt = null;
            }
        });
    }

    public void runJob() {
        resetValue();
        m_webcam.setEnabled(false);
        panel.start();
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {

                if ((image = webcam.getImage()) == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                }
            }
            //if (result != null) {
                //String rsString = result.getText();
                String rsString = "10000002";
                //got result
                barCodeText.setText(rsString);
                //query product
                try {
                    productInfoExt = m_dlSales.getProductInfoandCatByCode(rsString);
                    final ProductInfoExtCat product = this.productInfoExt;
                    if (product != null) {
                        //stop get image from camera
                        runJob = false;
                        //StringUtils.prinOut(product);
                        //update info to panel
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                textarea.setText(product.getName());
                                categoryText.setText(product.getCategoryName());
                                quantity.setText("1");
                                quantity.grabFocus();
                                quantity.selectAll();
                                done.setEnabled(true);
                            }
                        });
                        Toolkit.getDefaultToolkit().beep();
                    }
                } catch (BasicException e) {
                    e.printStackTrace();
                }


            //}

        } while (runJob);
    }


    public static void main(String[] args) {
        //new WebcamQRCode();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //add to sale panel
        double md = Double.parseDouble(quantity.getText());
        if (productInfoExt != null && md > 0) {
            this.panelticket.incProduct(md, productInfoExt);
        }
        resetValue();
    }
}