import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlLogin extends JFrame {

  public void showFrame(){
          this.setTitle("Զ�̿��ƿͻ���");
          this.setLayout(new FlowLayout());
          JLabel la_ip=new JLabel("���ض�IP");
          final JTextField jta_ip=new JTextField("172.25.240.30 ");
          JLabel la_port=new JLabel("���ض˶˿�");
          final JTextField jta_port=new JTextField("9090");
          JButton jb_start=new JButton("����");
          this.add(la_ip);
          this.add(jta_ip);
          this.add(la_port);
          this.add(jta_port); this.add(jb_start);
          
          jb_start.addActionListener(new ActionListener(){

                
                public void actionPerformed(ActionEvent e) {
                        String remoteIP=jta_ip.getText();
                        String sPort=jta_port.getText();
                        int port=Integer.parseInt(sPort);
                        ControlThread ct = new ControlThread(remoteIP,port);
                        new Thread(ct).start();
                }
                
          });
          
          this.setSize(230, 120);
          this.setVisible(true);
          this.setUndecorated(true);
          this.setDefaultCloseOperation(3);
          
  }
  
  
  
  public static void main(String[] args) {
          
          ControlLogin self=new ControlLogin();
          self.showFrame();
  }

}
