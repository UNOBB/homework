package Service;     //�����˰�����ͬ

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.SystemTray;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.TrayIcon;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author wealthypanda
 */

//����ͼ�񲢻��Ƶ��߳�
class ReceiveImageThread implements Runnable {
    
    private String ipNumber;                     //���ڴ洢�����Ip��ַ
    private int intPortNumber;                   //���ڴ洢����Ķ˿ں�
    private int intSleepTime=100;
    private String connectNumber;                //���ڴ洢�������������
    private JFrame frame;                        //����ͼ���������
    private JPanel panelImage=new JPanel();      //����ͼ������
    private JPanel panelButton;                  //��Ű�ť�����
    private JButton controlB;                    //���ư�ť
    private JSlider setSleepTime;
    private JLabel tipsT;                        //����ͼƬ�������ʾ��Ϣ
    private BufferedImage image;                 //���ڴ洢���յ�ͼƬ
    private InputStream is2;                     //Socket������������ͼƬ��
    private Socket socket;                       //Socket
    private BufferedInputStream bis;             //�����������
    private JPEGImageDecoder decoder;            //
    private ObjectOutputStream oos;
    private OutputStream os;
    private int hp;          //�洢����ĸ�
    private int hi;          //�洢ͼ��ĸ�
    private int wp;          //�洢����Ŀ�
    private int wi;          //�洢ͼ��Ŀ�    ��Ҫ���ڱ�������
    private ControlInterface control;       //���ڴ洢Զ�̶���Ĵ������RMI��أ�
    
    private MouseAndKeyOfButtonChange mkbc0 = new MouseAndKeyOfButtonChange(0);         //�������ư�ť
    private MouseAndKeyOfButtonChange mkbc1 = new MouseAndKeyOfButtonChange(1);         //�������Ӱ�ť
    private MouseAndKeyOfButtonChange mkbc2 = new MouseAndKeyOfButtonChange(2);         //Զ�̿���
        
    public void run() {

        ipNumber=MainFrameThread.ipNumberT.getText();                           //���Ip��ַ
        try{
            intPortNumber=new Integer(MainFrameThread.portNumberT.getText());   //�˿ں�
            if(intPortNumber>65535){
                MainFrameThread.information.append("�˿ںŴ���65535\n");
            }
        }catch(Exception e){
             MainFrameThread.information.append("IP��ַ��˿ںŷǷ�\n");
        }
        try{
            intSleepTime=Math.abs(new Integer(MainFrameThread.sleepTimeT.getText()));
            if(intSleepTime>1000){
                MainFrameThread.information.append("�������������÷�Χ��ʹ��Ĭ��ֵ100\n");
                intSleepTime=100;
            }
        }catch(Exception e){
            MainFrameThread.information.append("����������Ϊ�ջ���ڷǷ��ַ�\n");
            MainFrameThread.information.append("ʹ��Ĭ�Ϻ�����100\n");
            intSleepTime=100;
        }
        connectNumber=MainFrameThread.connectNumberT.getText();                 //��������
        try {      
            socket = new Socket();                                              //����socket����������
            SocketAddress sAdd=new InetSocketAddress(ipNumber,intPortNumber);
            socket.connect(sAdd, 60000);
            os=socket.getOutputStream();
            oos=new ObjectOutputStream(os);
            oos.writeObject(connectNumber);

            is2 = socket.getInputStream();	                                // ��ȡ����������
            bis = new BufferedInputStream(is2);
            decoder = JPEGCodec.createJPEGDecoder(bis);
            socket.setTcpNoDelay(true);
            socket.setReceiveBufferSize(1024*1024);

            frame=new JFrame();                                                 //������ʾԶ����������
            frame.setBounds(0, 0, 860, 574);
            frame.setIconImage(MainFrameThread.icon.getImage());
            frame.setTitle(ipNumber);
            frame.setFocusable(true);
            frame.setLayout(null);
            frame.addWindowListener(new MainFrameWinLis());
                                    
            controlB=new JButton("��  ��");                                     //���ƿ��ذ�ť
            controlB.setBounds(30, 5, 70, 27);
            controlB.setFocusable(false);

            setSleepTime=new JSlider(0, 1000, intSleepTime);
            setSleepTime.setBounds(150, 10, 200, 20);
            setSleepTime.addChangeListener(new SleepTimeChange());
            
            tipsT=new JLabel(String.valueOf(intSleepTime)+" ms");                                        //��Ϣ��ʾ
            tipsT.setBounds(355, 5, 50, 27);
            tipsT.setFocusable(false);
            tipsT.setForeground(Color.red);

            tipsT.setFont(tipsT.getFont().deriveFont(20));
            tipsT.setVisible(true);

            panelImage=new JPanel();                                            //��ʾͼ���panel
            panelImage.setFocusable(false);
            controlB.addMouseListener(mkbc0);
            
            panelButton=new JPanel();                                           //������ť��panel
            panelButton.setLayout(null);
            panelButton.add(controlB);
            panelButton.add(tipsT);
            panelButton.add(setSleepTime);
            panelButton.setFocusable(false);
            
            frame.add(panelImage);
            frame.add( panelButton);
            frame.setVisible(true);

            control= (ControlInterface)Naming.lookup("//"+ipNumber+":1099/Control");   //���Զ�̶���Ĵ������RMI��أ�
            control.sleepTime(intSleepTime);                                           //����Զ�̷�������������ʱ��
            MainFrameThread.information.append(MainFrameThread.getTime());
            MainFrameThread.information.append("���ӳɹ�����ʼ��������\n");

            while (!socket.isClosed()) {
                Thread.sleep(100);
                socket.sendUrgentData(0xff);  //Ŀ�������ж�Զ�������Ƿ�ر�
                panelImage.setBounds(0, 0, frame.getWidth()-5, frame.getHeight()-70);
                panelButton.setBounds(0, frame.getHeight()-70, frame.getWidth(),70);
	        // ����JPEG������
                image = decoder.decodeAsBufferedImage();	// ������������JPEGͼƬ
                wi=image.getWidth();         //���ͼ��Ĵ�С�����ڼ����������ȷ�������λ��
                hi=image.getHeight();
                Graphics g=panelImage.getGraphics();
                wp=panelImage.getWidth();   //�������Ĵ�С�����ڼ����������ȷ��������λ��
                hp=panelImage.getHeight();
                g.drawImage(image, 0, 0,wp,hp, panelImage);  //������ϻ���ͼ��
            }
        } catch (UnknownHostException ex) {
            MainFrameThread.information.append("�޷���������\n");
        } catch (Exception ex) {
            MainFrameThread.information.append("���ӹر�\n");
            frame.dispose();
        }

    }
    class MainFrameWinLis implements WindowListener{

        public void windowOpened(WindowEvent e) {
            frame.getFocusListeners();//frame��ý��㣬���ڼ��������¼�
        }

        public void windowClosing(WindowEvent e) {
            try {
                MainFrameThread.information.append(MainFrameThread.getTime());
                MainFrameThread.information.append("�ر���"+ipNumber+"������"+"\n");
                socket.close();    //SocketException ��δ�����
            } catch (IOException ex) {
                MainFrameThread.information.append("socket�޷��ر�\n");
            }
        }

        public void windowClosed(WindowEvent e) {
            
        }

        public void windowIconified(WindowEvent e) {

        }

        public void windowDeiconified(WindowEvent e) {

        }

        public void windowActivated(WindowEvent e) {

            frame.getFocusListeners();
        }

        public void windowDeactivated(WindowEvent e) {

        }

    }

    class SleepTimeChange implements ChangeListener{

        public void stateChanged(ChangeEvent e) {
            try {
                control.sleepTime(setSleepTime.getValue());
                tipsT.setText(String.valueOf(setSleepTime.getValue())+" ms");
            } catch (RemoteException ex) {
                MainFrameThread.information.append("Զ�̷��������쳣ԭ������ST\n");
            }
        }

    }

    class MouseAndKeyOfButtonChange implements MouseListener, MouseMotionListener,KeyListener,MouseWheelListener{

        int whereB;
        Integer intScanP;
        String copyNetIp;
        String copyStartIp;
        String copyEndIp;
        Socket scanSocket;

        MouseAndKeyOfButtonChange(int wherebcopy) {
            whereB = wherebcopy;  //0���������ƣ�1���������ӣ�2�������
        }
       
        public void mouseClicked(MouseEvent e1) {
            switch(whereB){
                case 0 : {
                    
                }
                break;
                case 1 : {
                    
                }
                break;
                case 2 : {
                    
                }
                break;
            }
        }

        public void mousePressed(MouseEvent e2) {
            
                switch (whereB){
                    case 0 : {
                        if (e2.getButton() == MouseEvent.BUTTON1){
                            panelImage.addMouseMotionListener(mkbc2);         //������Զ������Ŀ���
                            panelImage.addMouseListener(mkbc2);
                            panelImage.addMouseWheelListener(mkbc2);
                            frame.addKeyListener(mkbc2);
                            controlB.setText("��  ��");
                            controlB.removeMouseListener(mkbc0);
                            controlB.addMouseListener(mkbc1);
                        }                        
                    }
                    break;
                    case 1 : {
                        if(e2.getButton()==MouseEvent.BUTTON1){                  
                            panelImage.removeMouseListener(mkbc2);
                            panelImage.removeMouseMotionListener(mkbc2);
                            panelImage.removeMouseWheelListener(mkbc2);
                            frame.removeKeyListener(mkbc2);
                            controlB.setText("��  ��");
                            controlB.removeMouseListener(mkbc1);
                            controlB.addMouseListener(mkbc0);
                        }
                    }
                    break;
                    case 2 : {
                        int buttons=e2.getButton();
                        switch (buttons){
                            case MouseEvent.BUTTON1 : buttons=InputEvent.BUTTON1_MASK;
                            break;
                            case MouseEvent.BUTTON2 : buttons=InputEvent.BUTTON2_MASK;
                            break;
                            case MouseEvent.BUTTON3 : buttons=InputEvent.BUTTON3_MASK;
                            break;
                        }
                        try{
                            control.controlMousePress(buttons);
                        }catch (Exception e){
                            MainFrameThread.information.append("Զ�̿��ƴ�����MP");
                        }
                    }
                }  
        }

        public void mouseReleased(MouseEvent e3) {
            switch(whereB){
                case 0 : {
                    
                }
                break;
                case 1 : {
                    
                }
                break;
                case 2 : {
                    int buttons=e3.getButton();
                        switch (buttons){
                            case MouseEvent.BUTTON1 : buttons=InputEvent.BUTTON1_MASK;
                            break;
                            case MouseEvent.BUTTON2 : buttons=InputEvent.BUTTON2_MASK;
                            break;
                            case MouseEvent.BUTTON3 : buttons=InputEvent.BUTTON3_MASK;
                            break;
                        }
                        try{
                            control.controlMouseRelease(buttons);
                        }catch (Exception e){
                            MainFrameThread.information.append("Զ�̿��ƴ�����MR");
                        }
                }
                break;
            }
        }

        public void mouseEntered(MouseEvent e4) {
            switch(whereB){
                case 0 : {
                }
                break;
                case 1 : {
                }
                break;
                case 2 : {
                }
                break;
            }
        }

        public void mouseExited(MouseEvent e5) {
            switch(whereB){
                case 0 : {
                    
                }
                break;
                case 1 : {
                    
                }
                break;
                case 2 : {
                    
                }
                break;
            }
        }

        public void keyTyped(KeyEvent e6) {
            switch(whereB){
                case 0 : {
                    
                }
                break;
                case 1 : {
                    
                }
                break;
                case 2 : {
                    
                }
                break;
            }
        }

        public void keyPressed(KeyEvent e7) {
            switch(whereB){
                case 0 : {
                    
                }
                break;
                case 1 : {
                    
                }
                break;
                case 2 : {
                    try {
                        
                        control.controlKeyPress(e7.getKeyCode());
                    } catch (Exception ex) {
                        MainFrameThread.information.append("Զ�̿��ƴ�����KP");
                    }
                }
                break;
            }
        }

        public void keyReleased(KeyEvent e8) {
            switch(whereB){
                case 0 : {
                    
                }
                break;
                case 1 : {
                   
                }
                break;
                case 2 : {
                     try {
                         control.controlKeyRelease(e8.getKeyCode());
                    } catch (Exception ex) {
                        MainFrameThread.information.append("Զ�̿��ƴ�����KR");
                    }
                }
                break;
            }
        }

        public void mouseDragged(MouseEvent e9) {
            switch(whereB){
                case 0 : {
                    
                }
                break;
                case 1 : {
                    
                }
                break;
                case 2 : {
                    int x=e9.getX();       //������������е�λ��
                    int y=e9.getY();
                    x=(x*(1024*10000/wp))/10000;        //����Զ������ƶ���λ�ã�����10000���ڼ�С������10000����ȷ��λ��
                    y=(y*(768*10000/hp))/10000;
                    try {
                        control.controlMouseMove(x, y);
                    } catch (Exception ex) {
                        MainFrameThread.information.append("Զ�̿��ƴ�����MD");
                    }
                }
                break;
            }
        }

        public void mouseMoved(MouseEvent e10) {
            switch(whereB){
                case 0 : {
                    
                }
                break;
                case 1 : {
                    
                }
                break;
                case 2 : {
                    int x=e10.getX();       //������������е�λ��
                    int y=e10.getY();
                    x=(x*(1024*10000/wp))/10000;        //����Զ������ƶ���λ�ã�����10000���ڼ�С������10000����ȷ��λ��
                    y=(y*(768*10000/hp))/10000;
                    try {
                        control.controlMouseMove(x, y);
                    } catch (Exception ex) {
                        MainFrameThread.information.append("Զ�̿��ƴ�����MM");
                    }
                }
                break;
            }          
        }

        public void mouseWheelMoved(MouseWheelEvent e11) {
            switch(whereB){
                case 0 : {

                }
                break;
                case 1 : {

                }
                break;
                case 2 : {
                     try {
                        control.controlMouseWheel(e11.getWheelRotation());
                    } catch (Exception ex) {
                        MainFrameThread.information.append("Զ�̿��ƴ�����MWM");
                    }
                }
                break;
            }
           
        }

    }
}

//*******************************************************************************************************************
//�������߳�
//*******************************************************************************************************************
class MainFrameThread implements Runnable{

    private JFrame mainFrame;
    static TextArea information;
    private JLabel ipNumberL;
    private JLabel portNumberL;
    private JLabel connectNumberL;
    
    static TextField ipNumberT;
    static TextField portNumberT;
    static TextField connectNumberT;
    static TextField sleepTimeT;

    private JButton moreB;
    private JButton connectB;
    
    private JLabel scanPortNL;
    private JLabel netIpNL;
    private JLabel startIpNL;
    private JLabel endIpNL;
    private JLabel sleepTimeLS;
    private JLabel sleepTimeL;
    
    private TextField scanPortNT;
    private TextField netIpNT;
    private TextField startIpNT;
    private TextField endIpNT;
    
    private JButton startB;

    private SystemTray systemTray;
    static ImageIcon icon= new ImageIcon("trayIconC.png");
    private TrayIcon trayIcon;

    private MouseAndKeyOfButtonChange mkbc0 = new MouseAndKeyOfButtonChange(0);        //��������������ť�ļ���
    private MouseAndKeyOfButtonChange mkbc1 = new MouseAndKeyOfButtonChange(1);        //������������ť�ļ���
    private MouseAndKeyOfButtonChange mkbc2 = new MouseAndKeyOfButtonChange(2);        //�����������Ӱ�ť�ļ���
    private MouseAndKeyOfButtonChange mkbc3 = new MouseAndKeyOfButtonChange(3);        //�����趨��ʼ��ť�ļ���
    private MouseAndKeyOfButtonChange mkbc4 = new MouseAndKeyOfButtonChange(4);        //�����趨�������������ı���ļ���
    private MouseAndKeyOfButtonChange mkbc5 = new MouseAndKeyOfButtonChange(5);

    public void run() { 

        mainFrame=new JFrame("Զ������ϵͳ�ͻ���");
        mainFrame.setBounds(345, 228, 333, 253);
        mainFrame.setIconImage(icon.getImage());
        mainFrame.setResizable(false);
        mainFrame.addWindowListener(new MainFrameWinLis());

        mainFrame.setBackground(Color.lightGray);
        mainFrame.setLayout(null);

        information=new TextArea();
        information.setBounds(0, 170, 333, 73);
        information.setEditable(true);
        information.setFocusable(false);
        try {
            InetAddress ia = InetAddress.getLocalHost();
            String hostName=ia.getHostName();
            String hostIp=ia.getHostAddress();
            information.append(getTime());
            information.append("�ͻ��˳�������\n");
            information.append("����������:"+hostName+"\n");
            information.append("���ص�ַ:"+hostIp+"\n");
        } catch (UnknownHostException ex) {
            information.append("�����쳣������������\n");
            System.exit(1);
        }
        
        ipNumberL=new JLabel("������IP��ַ��");
        ipNumberL.setBounds(20, 20, 100, 20);
        ipNumberT=new TextField(15);
        ipNumberT.setBounds(120, 20, 165, 20);

        portNumberL=new JLabel("������˿ںţ�");
        portNumberL.setBounds(20, 50, 100, 20);
        portNumberT=new TextField(5);
        portNumberT.setBounds(120, 50, 165, 20);

        connectNumberL=new JLabel("�������룺");
        connectNumberL.setBounds(20, 80, 70, 20);
        connectNumberT=new TextField(6);
        connectNumberT.setBounds(90, 80, 60, 20);
        connectNumberT.setEchoChar('*');
        connectNumberT.addKeyListener(mkbc4);

        sleepTimeLS=new JLabel("����");
        sleepTimeLS.setBounds(170, 80, 30, 20);

        sleepTimeT=new TextField();
        sleepTimeT.setBounds(200, 80, 40, 20);
        sleepTimeT.addKeyListener(mkbc5);

        sleepTimeL=new JLabel("����/֡");
        sleepTimeL.setBounds(245, 80, 50, 20);
        

        moreB=new JButton("��  ��");
        moreB.setBounds(50, 120, 70, 30);
        moreB.addMouseListener(mkbc0);

        connectB=new JButton("��  ��");
        connectB.setBounds(205, 120, 70, 30);
        connectB.addMouseListener(mkbc2);
        connectB.addKeyListener(mkbc2);
        
        mainFrame.add(information);
        mainFrame.add(ipNumberL);
        mainFrame.add(ipNumberT);
        mainFrame.add(portNumberL);
        mainFrame.add(portNumberT);
        mainFrame.add(connectNumberL);
        mainFrame.add(connectNumberT);
        mainFrame.add(sleepTimeLS);
        mainFrame.add(sleepTimeT);
        mainFrame.add(sleepTimeL);
        mainFrame.add(moreB);
        mainFrame.add(connectB);

        mainFrame.setVisible(true);
        
    }

    static String getTime(){
        GregorianCalendar gc=new GregorianCalendar();
        String now=gc.get(Calendar.HOUR_OF_DAY)+"ʱ"+gc.get(Calendar.MINUTE)+"��"+gc.get(Calendar.SECOND)+"��";
        return now;
    }

    class MouseAndKeyOfButtonChange implements MouseListener, KeyListener{

        int whereB;
        Integer intScanP;
        String copyNetIp;
        String copyStartIp;
        String copyEndIp;
        Socket scanSocket;

        MouseAndKeyOfButtonChange(int wherebcopy) {
            whereB = wherebcopy;
        }

        public void mouseClicked(MouseEvent e1) {

        }

        public void mousePressed(MouseEvent e2) {
            if (e2.getButton() == MouseEvent.BUTTON1){
                switch(whereB){
                    case 0 : {
                        moreB.removeMouseListener(mkbc0);
                        moreB.addMouseListener(mkbc1);
                        mainFrame.setVisible(false);
                        moreB.setText("��  ��");
                        mainFrame.setBounds(345, 128,333, 423);

                        scanPortNL=new JLabel("Ҫɨ��˿ںţ�");
                        scanPortNL.setBounds(20, 260, 100, 20);
                        scanPortNT=new TextField("8000");
                        scanPortNT.setBounds(120, 260, 150, 20);

                        netIpNL=new JLabel("���������ַ��");
                        netIpNL.setBounds(20, 290, 100, 20);
                        netIpNT=new TextField("192.168.1.");
                        netIpNT.setBounds(120, 290, 150, 20);

                        startIpNL=new JLabel("����������ַ��  ��");
                        startIpNL.setBounds(20, 320, 115, 20);
                        startIpNT=new TextField("1");
                        startIpNT.setBounds(135, 320, 30, 20);

                        endIpNL=new JLabel("��");
                        endIpNL.setBounds(170, 320, 15, 20);
                        endIpNT=new TextField("254");
                        endIpNT.setBounds(190, 320, 30, 20);

                        startB=new JButton("��  ʼ");
                        startB.setBounds(190, 350, 70, 30);
                        startB.addMouseListener(mkbc3);
                                              
                        mainFrame.add(scanPortNL);
                        mainFrame.add(scanPortNT);
                        mainFrame.add(netIpNL);
                        mainFrame.add(netIpNT);
                        mainFrame.add(startIpNL);
                        mainFrame.add(startIpNT);
                        mainFrame.add(endIpNL);
                        mainFrame.add(endIpNT);
                        mainFrame.add(startB);
                        mainFrame.setVisible(true);                       
                    }
                    break;
                    case 1 : {
                        moreB.setText("��  ��");
                        moreB.removeMouseListener(mkbc1);
                        moreB.addMouseListener(mkbc0);
                        mainFrame.setBounds(345, 228, 333, 253);
                    }
                    break;
                    case 2 : {
                        information.append(getTime());
                        information.append("������"+ipNumberT.getText()+"�������ӡ���\n");
                        ReceiveImageThread rit=new ReceiveImageThread();
                        Thread t2=new Thread(rit);
                        t2.start();
                    }
                    break;
                    case 3 : {
                        information.append(getTime());
                        information.append("��ʼɨ��\n");
                        intScanP=new Integer(scanPortNT.getText());
                        copyNetIp=netIpNT.getText();
                        int intStartIp=new Integer(startIpNT.getText());
                        int intEndIp=new Integer(endIpNT.getText());                       
                        for(;intStartIp<=intEndIp||intStartIp==254;intStartIp++){                         
                            try {
                                scanSocket = new Socket();
                                scanSocket.setSoLinger(true, 0);
                                SocketAddress sa=new InetSocketAddress(copyNetIp + String.valueOf(intStartIp),intScanP);
                                scanSocket.connect(sa, 500);
                                OutputStream os=scanSocket.getOutputStream();
                                ObjectOutputStream oos=new ObjectOutputStream(os);
                                oos.writeObject("��������");
                                information.append(copyNetIp + String.valueOf(intStartIp)+"������\n");
                            }catch (IOException ex) {                               
                                information.append(copyNetIp + String.valueOf(intStartIp)+"δ����\n");
                            }
                        }
                    }
                    information.append(getTime());
                    information.append("ɨ�����\n");
                    break;
                    case 5 : {
                        mainFrame.setVisible(true);
                        systemTray.remove(trayIcon);
                    }
                    break;
                }
            }
        }

        public void mouseReleased(MouseEvent e3) {

        }

        public void mouseEntered(MouseEvent e4) {

        }

        public void mouseExited(MouseEvent e5) {

        }

        public void keyTyped(KeyEvent e6) {

        }

        public void keyPressed(KeyEvent e7) {
            switch(whereB){
                case 2 : {
                   switch(e7.getKeyCode()){
                        case 10 : {
                            information.append(getTime());
                            information.append("��"+ipNumberT.getText()+"��������\n");
                            ReceiveImageThread rit=new ReceiveImageThread();
                            Thread t2=new Thread(rit);
                            t2.start();
                        }
                        break;
                    }
                }
                break;
                case 4 : {
                    switch(e7.getKeyCode()){
                        case 10 : {
                            information.append(getTime());
                            information.append("��"+ipNumberT.getText()+"��������\n");
                            ReceiveImageThread rit=new ReceiveImageThread();
                            Thread t2=new Thread(rit);
                            t2.start();
                        }
                        break;
                    }
                }
                break;
                case 5 : {
                    switch(e7.getKeyCode()){
                        case 10 : {
                            information.append(getTime());
                            information.append("��"+ipNumberT.getText()+"��������\n");
                            ReceiveImageThread rit=new ReceiveImageThread();
                            Thread t2=new Thread(rit);
                            t2.start();
                        }
                        break;
                    }
                }
            }         
        }

        public void keyReleased(KeyEvent e8) {

        }

    }
    class MainFrameWinLis implements WindowListener{

        public void windowOpened(WindowEvent e9) {

        }

        public void windowClosing(WindowEvent e10) {
            System.exit(0);
        }

        public void windowClosed(WindowEvent e11) {
            System.exit(0);
        }

        public void windowIconified(WindowEvent e12) {
            initSystemTray();
            mainFrame.setVisible(false);
        }

        public void windowDeiconified(WindowEvent e13) {

        }

        public void windowActivated(WindowEvent e14) {

        }

        public void windowDeactivated(WindowEvent e15) {

        }

    }
    //��С��ϵͳ���̵ķ���
    private void initSystemTray() {
        if (SystemTray.isSupported()) {
            systemTray = SystemTray.getSystemTray();
        }
        //icon = new ImageIcon("trayIconC.png");
        trayIcon = new TrayIcon(icon.getImage());
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAndKeyOfButtonChange(5));
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            MainFrameThread.information.append("�Ҳ����ļ�:trayIcon.png");
        }
    }
}
public class ClientProgram{
    public static void main(String args[]){
        MainFrameThread aaa=new MainFrameThread();
        Thread t1=new Thread(aaa);
        t1.start();
    }
}