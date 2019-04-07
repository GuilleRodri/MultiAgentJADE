/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author edacal
 */
public class MainPanel extends JPanel{
    private Dimension FrameSize;
    private BufferedImage factory;
    private ArrayList<Rectangle> sections;
    private int heightDiv;
    
    public MainPanel() throws IOException {
        factory = ImageIO.read(getClass().getResource("../Resources/factory1.png"));
        
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                //highlight = drawRectangle.contains(e.getPoint());
                repaint();
            }
        });
        //backgroundHeight = background.getHeight();
        //backgroundWidth = background.getWidth();
    }
    
    @Override
    public void paint(Graphics g) {
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int x = (int) b.getX();
        int y = (int) b.getY();
        FrameSize = this.getSize();
        if(x >= FrameSize.width/2 && x <= FrameSize.width/2 + 50) 
        {
            System.out.println("dentro");
        
        }
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, FrameSize.width, FrameSize.height);
        heightDiv = FrameSize.height / 20;
        for(int i = 0; i <= FrameSize.height; ++i) {
            g.setColor(Color.BLUE);
            g.fillRect(FrameSize.width/2, i, 50, heightDiv);
            g.drawImage(factory,FrameSize.width/2 - 30, 0,this);
        }
    }
}
