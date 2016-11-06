import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.*;
public class ControlThread extends JFrame implements
                    Runnable,MouseListener,
                    MouseMotionListener,
                    MouseWheelListener,KeyListener{
    
      private String ip;
      private int port;
      private ObjectOutputStream out;
      private DataInputStream dins;
      private JLabel imageLab;
      
      
      public ControlThread(String ip,int port){
              this.ip=ip;
              this.port=port;
              this.setUndecorated(true);
              init();
      }
      
      private void init(){
              imageLab=new JLabel();
              this.add(imageLab);
              this.addMouseListener(this);
              this.addMouseMotionListener(this);
              this.addMouseWheelListener(this);
              this.addKeyListener(this);
              this.setVisible(true);
              this.requestFocus(true);
              this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
              this.setDefaultCloseOperation(3);
      }
      
      
      public void run() {
              try{  
                    Socket socket = new Socket(this.ip,this.port);
                    out=new ObjectOutputStream(socket.getOutputStream());
                    dins=new DataInputStream(socket.getInputStream());
                    while(true){
                            int imageLen=dins.readInt();
                            byte[] iData=new byte[100000000];
                            dins.readFully(iData);
                            
                            ByteArrayInputStream bins = new ByteArrayInputStream(iData);
                            BufferedImage image=ImageIO.read(bins);
                            ImageIcon icon=new ImageIcon(image);
                            imageLab.setIcon(icon);
                            imageLab.repaint();
                            
                    }
              }catch(Exception e){
                    e.printStackTrace();
              }
              
      }

      @Override
      public void mouseClicked(MouseEvent e) {
              sendClientAction(e);
              
      }

      @Override
      public void mouseEntered(MouseEvent e) {
              // TODO Auto-generated method stub
              sendClientAction(e);
      }

      @Override
      public void mouseExited(MouseEvent e) {
              // TODO Auto-generated method stub
              sendClientAction(e);
      }

      @Override
      public void mousePressed(MouseEvent e) {
              // TODO Auto-generated method stub
              sendClientAction(e);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
              // TODO Auto-generated method stub
              sendClientAction(e);
      }

      @Override
      public void mouseDragged(MouseEvent e) {
              // TODO Auto-generated method stub
              sendClientAction(e);
      }

      @Override
      public void mouseMoved(MouseEvent e) {
              // TODO Auto-generated method stub
              sendClientAction(e);
      }

      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
              // TODO Auto-generated method stub
              sendClientAction(e);
      }

      @Override
      public void keyPressed(KeyEvent e) {
              // TODO Auto-generated method stub
              sendClientAction(e);
      }

      @Override
      public void keyReleased(KeyEvent e) {
              // TODO Auto-generated method stub
              sendClientAction(e);
      }

      @Override
      public void keyTyped(KeyEvent e) {
              // TODO Auto-generated method stub
              sendClientAction(e);
      }
      
      public void sendClientAction(InputEvent event){
              try{
              out.writeObject(event);
              }catch(Exception e){
                    e.printStackTrace();
              }
      }

}



