package Service;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.KeyListener;
import java.io.*;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;


//�������߳�
/**
 * 
 * 
 */
public class ServiceProgram {                                  //*************���࣬�����߳�

    public static void main(String args[]) {
        
        MainFrameThread MFT = new MainFrameThread();         //�������������
        Thread t1 = new Thread(MFT);
        t1.setPriority(1);
        t1.start();                                     //�����������߳�
    }
}
class MainFrameThread implements Runnable {               //*****************�������࣬���ڻ�������

    private RandomAccessFile RandomNDF;          //�����д�ļ�
    private int readPoint[] = new int[]{6, 14};     //�ļ�ָ�붨λ
    static String portN;                 //�˿ں�
    static String connectN;              //��������
    static String manageN;               //��������
    static Thread t2;                    //�����߳�
    private JFrame mainFrame;               //������
    private TextField openmanageT;          //��������򿪹���������ı���
    static TextArea information;    //�����ı���������ʾ��Ϣ
    private TextField portNumberT;          //�˿��ı���
    private TextField connectNumberT;       //���������ı���
    private TextField reInputCNT;           //�ٴ��������������ı���
    private TextField manageNumberT;        //���������ı���
    private TextField reInputMNT;           //�ٴ�������������ı���
    private JButton defaultB;               //Ĭ�ϰ�ť
    private JButton settedB;                //�趨��ť
    private JButton exitB;                  //�˳���ť
    private JButton openmanageB;            //���ð�ť
    private JDialog exitBD;                 //�˳��Ի���
    private SystemTray systemTray;         //�ɻ�ȡϵͳ��������Ҳ���ж�ϵͳ�Ƿ�֧��ϵͳ����
    private ImageIcon icon;                //�ɴ���ϵͳ����ͼ��
    private TrayIcon trayIcon;             //�����ϵͳ����
    private MouseAndKeyOfButtonChange mkbc0 = new MouseAndKeyOfButtonChange(0);        //�����˳���ť�ļ����볷��
    private MouseAndKeyOfButtonChange mkbc1 = new MouseAndKeyOfButtonChange(1);        //�������ð�ť�ļ����볷��
    private MouseAndKeyOfButtonChange mkbc2 = new MouseAndKeyOfButtonChange(2);        //����Ĭ�ϰ�ť�ļ����볷��
    private MouseAndKeyOfButtonChange mkbc3 = new MouseAndKeyOfButtonChange(3);        //�����趨��ť�ļ����볷��

    /**
     * run����
     * ��ʼ������
     * �������������ļ�
     * �����������ļ��ж�ȡ����
     * �����˿�
     * �ȴ�����
     * �趨���ɴ���һ������*
     */
    public void run() {

        mainFrame = new JFrame("Զ��������ϵͳ");             //Զ������ϵͳ������
        mainFrame.setLocation(700, 200);                     //Զ�����洰������ʱ����ʾ����λ��
        mainFrame.setSize(303, 530);                         //Զ�����洰�ڵĴ�С
        icon = new ImageIcon("trayIconS.png");
        mainFrame.setIconImage(icon.getImage());             //�ı�frame���Ͻ�ͼ��

        mainFrame.setResizable(false);                       //�����Ըı��С
        mainFrame.addWindowListener(new MainFrameWinLis());  //�����ڵĴ����¼�����

        mainFrame.setBackground(Color.lightGray);         //����ɫΪǳ��ɫ
        mainFrame.setLayout(null);                        //��ʹ�ò��ֹ�����

        information = new TextArea();                       //�趨��Ϣ��ʾ�����λ�����С
        information.setBounds(0, 350, 300, 150);          //���ô�С��λ��
        information.setEditable(true);                   //�ɱ༭״̬
        information.setFocusable(false);                 //���ɵõ�����   Ҳ�Ͳ��ɱ༭��Ŀ������Tab��ѭ��
        mainFrame.add(information);                       //��mainFrame����Ӷ����ı������������Ϊ�˽�����Ϣ��
        information.append(getTime());                   //��ʾϵͳ����ʱ��
        information.append("���������\n");

        //�ж��ļ��Ƿ���ڣ��������򴴽�����д���ʼ���ݣ�
        File NumberDataFile = new File("NumberDataFile.txt");               //�����ļ�����
        if (!NumberDataFile.exists()) {                                 //�����ļ��Ƿ����
            try {
                NumberDataFile.createNewFile();                       //��������ڣ��򴴽��ļ�
                RandomNDF = new RandomAccessFile(NumberDataFile, "rw"); //�Զ�д��ʽ�����漴��д�ļ�
                information.append("δ���������ļ������³�ʼ���ã�\n");
                RandomNDF.seek(0);                                    //ָ�붨λ���ļ�ͷ
                RandomNDF.writeUTF("8000");
                information.append("�˿ںų�ʼ���ɹ��ã�\n");
                RandomNDF.seek(readPoint[0]);
                RandomNDF.writeUTF("123456");
                information.append("���������ʼ���ɹ��ã�\n");
                RandomNDF.seek(readPoint[1]);
                RandomNDF.writeUTF("888888");
                information.append("���������ʼ���ɹ��ã�\n");
                RandomNDF.seek(0);
            } catch (IOException ex) {
                information.append("�ļ�����ʧ�ܡã�\n");
            }
        }
        try {
            RandomNDF = new RandomAccessFile(NumberDataFile, "rw");   //����ļ����ڣ������漴��д�ļ�
            try {
                RandomNDF.seek(0);                                    //��λ���ļ�ͷ
                portN = RandomNDF.readUTF();                            //��ȡ����
                information.append("�˿ںŶ�ȡ�ɹ��ã�\n");
                RandomNDF.seek(readPoint[0]);
                connectN = RandomNDF.readUTF();
                information.append("���������ȡ�ɹ��ã�\n");
                RandomNDF.seek(readPoint[1]);
                manageN = RandomNDF.readUTF();
                information.append("���������ȡ�ɹ��ã�\n");
                RandomNDF.seek(0);
            } catch (IOException ex) {
                information.append("�ļ���ȡʧ�ܡã�\n");
            }
        } catch (FileNotFoundException ex) {
            information.append("�ļ�δ�ҵ��ã�\n");
        }

        exitB = new JButton("��  ��");                    //�����趨��ť
        exitB.setBounds(50, 70, 70, 30);                  //�趨��ť��С��λ��
        exitB.addMouseListener(mkbc0);                    //������

        openmanageB = new JButton("��  ��");              //�����˳���ť
        openmanageB.setBounds(170, 70, 70, 30);            //�趨��ť��С��λ��
        openmanageB.addMouseListener(mkbc1);               //������

        JLabel openmanage = new JLabel("����������:");      //�������������ȷ�������룬�ſɲ�������
        openmanage.setBounds(20, 20, 120, 20);
        openmanageT = new TextField(6);
        openmanageT.setBounds(145, 20, 120, 20);
        openmanageT.setEchoChar('*');

        JLabel portNumber = new JLabel("���ü����˿�:");     //�����趨�����˿ڵı�ǩ�뵥���ı���
        portNumber.setBounds(20, 125, 120, 20);
        portNumberT = new TextField(portN, 5);
        portNumberT.setBounds(145, 125, 120, 20);
        portNumberT.setEnabled(false);

        JLabel connectNumber = new JLabel("������������:");   //�����趨��������ı�ǩ���ı���
        connectNumber.setBounds(20, 165, 120, 20);
        connectNumberT = new TextField(connectN, 6);
        connectNumberT.setBounds(145, 165, 120, 20);
        connectNumberT.setEchoChar('*');
        connectNumberT.setEnabled(false);

        JLabel reInputCN = new JLabel("�ٴ���������:");      //�ٴ�������������
        reInputCN.setBounds(20, 190, 120, 20);
        reInputCNT = new TextField(connectN, 6);
        reInputCNT.setBounds(145, 190, 120, 20);
        reInputCNT.setEchoChar('*');
        reInputCNT.setEnabled(false);

        JLabel manageNumber = new JLabel("����������룺");  //�����趨��������ı�ǩ���ı���
        manageNumber.setBounds(20, 220, 120, 20);
        manageNumberT = new TextField(manageN, 6);
        manageNumberT.setBounds(145, 220, 120, 20);
        manageNumberT.setEchoChar('*');
        manageNumberT.setEnabled(false);

        JLabel reInputMN = new JLabel("�ٴ���������:");     //�ٴ������������
        reInputMN.setBounds(20, 245, 120, 20);
        reInputMNT = new TextField(manageN, 6);
        reInputMNT.setBounds(145, 245, 120, 20);
        reInputMNT.setEchoChar('*');
        reInputMNT.setEnabled(false);

        defaultB = new JButton("Ĭ  ��");                  //�����˳���ť
        defaultB.setBounds(50, 300, 70, 30);                //�趨��ť��С��λ��
        defaultB.setEnabled(false);

        settedB = new JButton("��  ��");                   //�����趨��ť
        settedB.setBounds(170, 300, 70, 30);             //�趨��ť��С��λ��
        settedB.setEnabled(false);

        mainFrame.add(exitB);                           //��Ӹ������
        mainFrame.add(openmanageB);
        mainFrame.add(portNumber);
        mainFrame.add(openmanage);
        mainFrame.add(openmanageT);
        mainFrame.add(connectNumber);
        mainFrame.add(reInputCN);
        mainFrame.add(manageNumber);
        mainFrame.add(reInputMN);
        mainFrame.add(portNumberT);
        mainFrame.add(connectNumberT);
        mainFrame.add(reInputCNT);
        mainFrame.add(manageNumberT);
        mainFrame.add(reInputMNT);
        mainFrame.add(settedB);
        mainFrame.add(defaultB);

        //mainFrame.setVisible(true);   //(������Ŀ���ǣ���ֹ��֧��ϵͳ���̣� //��ʾ�����ڣ�������������Ŀ���Ǳ�֤�����ʾȫ��
        initSystemTray();    //����ʱ����С����ϵͳ����
        startsit();          //���������̵߳ķ���

        

    }

    //�������������̵߳ķ���
    static void startsit() {
        startSendImageThread startSIT = new startSendImageThread();  //���������̶߳���
        t2 = new Thread(startSIT);
        t2.start();                                                 //���������߳�
        MainFrameThread.information.append(getTime());
    }

    static String getTime(){                                  //���ϵͳʱ�䷽��
        GregorianCalendar gc=new GregorianCalendar();
        String now=gc.get(Calendar.HOUR_OF_DAY)+"ʱ"+gc.get(Calendar.MINUTE)+"��"+gc.get(Calendar.SECOND)+"��";
        return now;
    }

    //�������ڼ�����
    class MainFrameWinLis implements WindowListener {

        public void windowOpened(WindowEvent e) {
        }

        public void windowClosing(WindowEvent e) {      //����رհ�ťʱ����С����ϵͳ����
            initSystemTray();
        }

        public void windowClosed(WindowEvent e) {
        }

        public void windowIconified(WindowEvent e) {
        }

        public void windowDeiconified(WindowEvent e) {
        }

        public void windowActivated(WindowEvent e) {
        }

        public void windowDeactivated(WindowEvent e) {
        }
    }

    //ʵ�ְ�ť�¼�������
    class MouseAndKeyOfButtonChange implements MouseListener, KeyListener {

        int whereB;

        MouseAndKeyOfButtonChange(int wherebcopy) {
            whereB = wherebcopy;
        }

        public void mouseClicked(MouseEvent e1) {
        }

        public void mousePressed(MouseEvent e2) {
            if (e2.getButton() == MouseEvent.BUTTON1) {         //ֻ�����������
                switch (whereB) {
                    case 0: {
                        if (manageN.equals(openmanageT.getText())) {                           //�������������ȷ�������Ի���ȷ�ϡ�
                            openmanageT.setText(null);
                            exitBD = new JDialog(mainFrame, "�˳�ȷ��", true);
                            exitBD.setBounds(400, 300, 400, 200);
                            exitBD.setLayout(null);
                            exitBD.setResizable(false);
                            JButton yesB = new JButton("��");
                            yesB.setBounds(70, 100, 70, 30);
                            yesB.addMouseListener(new MouseAndKeyOfButtonChange(4));
                            JButton noB = new JButton("��");
                            noB.setBounds(240, 100, 70, 30);
                            noB.addMouseListener(new MouseAndKeyOfButtonChange(5));
                            JLabel sureL = new JLabel("ȷ��Ҫ�˳���");
                            sureL.setBounds(150, 30, 120, 20);
                            exitBD.add(sureL);
                            exitBD.add(yesB);
                            exitBD.add(noB);
                            exitBD.setVisible(true);//�˴���һ�����������Ϊ�����ִ�д˴��룬��ģʽΪ�棬��ȴ��Ի����߳�ֹͣ���޷�ִ������Ĵ��롣
                        } else {
                            JDialog exiterrorN = new JDialog(mainFrame, "error", true);         //�������벻��ȷ����ʾ��������
                            openmanageT.setText(null);
                            exiterrorN.setBounds(400, 300, 300, 100);
                            exiterrorN.setResizable(false);
                            exiterrorN.setLayout(null);
                            JLabel errorNumber = new JLabel("����������������룡�ã�");
                            errorNumber.setBounds(50, 20, 170, 20);
                            exiterrorN.add(errorNumber);
                            exiterrorN.setVisible(true);
                        }
                    }//case 0   �˳���ť
                    break;
                    case 1: {
                        if (manageN.equals(openmanageT.getText())) {              //����������ȷ���򼤻������ı����밴ť������������򲻿���
                            MainFrameThread.information.append(MainFrameThread.getTime());
                            information.append("����ϵͳ���áã�\n");
                            openmanageB.setEnabled(false);
                            openmanageB.removeMouseListener(this);
                            openmanageT.setEnabled(false);
                            portNumberT.setEnabled(true);
                            connectNumberT.setEnabled(true);
                            reInputCNT.setEnabled(true);
                            manageNumberT.setEnabled(true);
                            reInputMNT.setEnabled(true);
                            defaultB.setEnabled(true);
                            defaultB.addMouseListener(mkbc2);
                            settedB.setEnabled(true);
                            settedB.addMouseListener(mkbc3);
                            openmanageT.setText(null);
                        } else {
                            openmanageT.setText(null);
                            JDialog secreterror = new JDialog(mainFrame, "error", true);  //�������������ʾ��������
                            secreterror.setBounds(400, 300, 300, 100);
                            secreterror.setLayout(null);
                            secreterror.setResizable(false);
                            JLabel errorNumber = new JLabel("����������������룡�ã�");
                            errorNumber.setBounds(50, 20, 170, 20);
                            secreterror.add(errorNumber);
                            secreterror.setVisible(true);
                        }
                    }//case 1   �򿪹���ť
                    break;
                    case 2: {
                        try {
                            information.append("���ڻָ�Ĭ��ֵ�����ã�\n");
                            RandomNDF.seek(0);
                            RandomNDF.writeUTF("8000");
                            portNumberT.setText("8000");
                            portN="8000";
                            information.append("�˿ںŻָ��ɹ��ã�\n");
                            RandomNDF.seek(readPoint[0]);
                            RandomNDF.writeUTF("123456");
                            connectNumberT.setText("123456");
                            reInputCNT.setText("123456");
                            connectN="123456";
                            information.append("��������ָ��ɹ��ã�\n");
                            RandomNDF.seek(readPoint[1]);
                            RandomNDF.writeUTF("888888");
                            manageNumberT.setText("888888");
                            reInputMNT.setText("888888");
                            manageN="888888";
                            information.append("��������ָ��ɹ��ã�\n");
                            RandomNDF.seek(0);
                            openmanageB.setEnabled(true);
                            openmanageB.addMouseListener(mkbc1);
                            openmanageT.setEnabled(true);
                            portNumberT.setEnabled(false);
                            connectNumberT.setEnabled(false);
                            reInputCNT.setEnabled(false);
                            manageNumberT.setEnabled(false);
                            reInputMNT.setEnabled(false);
                            defaultB.setEnabled(false);
                            defaultB.removeMouseListener(mkbc2);
                            settedB.setEnabled(false);
                            settedB.removeMouseListener(mkbc3);
                        } catch (IOException ex) {
                            information.append("�ָ�ʧ������Ϊ�ļ�д��ʧ�ܡã�\n");
                        }
                    }//case 2   Ĭ��
                    break;
                    case 3: {
                        try {
                            String pnt1 = portNumberT.getText();
                            String pnt2 = connectNumberT.getText();
                            String pnt3 = reInputCNT.getText();
                            String pnt4 = manageNumberT.getText();
                            String pnt5 = reInputMNT.getText();
                            try {
                                Integer intNumber = new Integer(pnt1);
                                if (intNumber < 1024 || intNumber > 65535) {
                                    information.append("�˿ںű������1024С��65535��(\n");
                                    information.append("�����������δ�ܸı�ϵͳ���á�(\n");
                                    portNumberT.setText(portN);
                                    connectNumberT.setText(connectN);
                                    reInputCNT.setText(connectN);
                                    manageNumberT.setText(manageN);
                                    reInputMNT.setText(manageN);
                                    RandomNDF.seek(0);
                                    openmanageB.setEnabled(true);
                                    openmanageB.addMouseListener(mkbc1);
                                    openmanageT.setEnabled(true);
                                    portNumberT.setEnabled(false);
                                    connectNumberT.setEnabled(false);
                                    reInputCNT.setEnabled(false);
                                    manageNumberT.setEnabled(false);
                                    reInputMNT.setEnabled(false);
                                    defaultB.setEnabled(false);
                                    defaultB.removeMouseListener(mkbc2);
                                    settedB.setEnabled(false);
                                    settedB.removeMouseListener(mkbc3);
                                } else {
                                    switch (textFiledNumberCompare(pnt2, pnt3)) {
                                        case 1: {
                                            information.append("���������롱�����������δ�ܸı�ϵͳ���á�(\n");
                                            portNumberT.setText(portN);
                                            connectNumberT.setText(connectN);
                                            reInputCNT.setText(connectN);
                                            manageNumberT.setText(manageN);
                                            reInputMNT.setText(manageN);
                                            RandomNDF.seek(0);
                                            openmanageB.setEnabled(true);
                                            openmanageB.addMouseListener(mkbc1);
                                            openmanageT.setEnabled(true);
                                            portNumberT.setEnabled(false);
                                            connectNumberT.setEnabled(false);
                                            reInputCNT.setEnabled(false);
                                            manageNumberT.setEnabled(false);
                                            reInputMNT.setEnabled(false);
                                            defaultB.setEnabled(false);
                                            defaultB.removeMouseListener(mkbc2);
                                            settedB.setEnabled(false);
                                            settedB.removeMouseListener(mkbc3);
                                        }
                                        break;
                                        case 2: {
                                            information.append("���������롱�����������δ�ܸı�ϵͳ���á�(\n");
                                            portNumberT.setText(portN);
                                            connectNumberT.setText(connectN);
                                            reInputCNT.setText(connectN);
                                            manageNumberT.setText(manageN);
                                            reInputMNT.setText(manageN);
                                            RandomNDF.seek(0);
                                            openmanageB.setEnabled(true);
                                            openmanageB.addMouseListener(mkbc1);
                                            openmanageT.setEnabled(true);
                                            portNumberT.setEnabled(false);
                                            connectNumberT.setEnabled(false);
                                            reInputCNT.setEnabled(false);
                                            manageNumberT.setEnabled(false);
                                            reInputMNT.setEnabled(false);
                                            defaultB.setEnabled(false);
                                            defaultB.removeMouseListener(mkbc2);
                                            settedB.setEnabled(false);
                                            settedB.removeMouseListener(mkbc3);
                                        }
                                        break;
                                        case 0: {                                        
                                            switch (textFiledNumberCompare(pnt4, pnt5)) {
                                                case 1: {
                                                    information.append("���������롱�����������δ�ܸı�ϵͳ���á�(\n");
                                                    portNumberT.setText(portN);
                                                    connectNumberT.setText(connectN);
                                                    reInputCNT.setText(connectN);
                                                    manageNumberT.setText(manageN);
                                                    reInputMNT.setText(manageN);
                                                    RandomNDF.seek(0);
                                                    openmanageB.setEnabled(true);
                                                    openmanageB.addMouseListener(mkbc1);
                                                    openmanageT.setEnabled(true);
                                                    portNumberT.setEnabled(false);
                                                    connectNumberT.setEnabled(false);
                                                    reInputCNT.setEnabled(false);
                                                    manageNumberT.setEnabled(false);
                                                    reInputMNT.setEnabled(false);
                                                    defaultB.setEnabled(false);
                                                    defaultB.removeMouseListener(mkbc2);
                                                    settedB.setEnabled(false);
                                                    settedB.removeMouseListener(mkbc3);
                                                }
                                                break;
                                                case 2: {
                                                    information.append("���������롱�����������δ�ܸı�ϵͳ���á�(\n");
                                                    portNumberT.setText(portN);
                                                    connectNumberT.setText(connectN);
                                                    reInputCNT.setText(connectN);
                                                    manageNumberT.setText(manageN);
                                                    reInputMNT.setText(manageN);
                                                    RandomNDF.seek(0);
                                                    openmanageB.setEnabled(true);
                                                    openmanageB.addMouseListener(mkbc1);
                                                    openmanageT.setEnabled(true);
                                                    portNumberT.setEnabled(false);
                                                    connectNumberT.setEnabled(false);
                                                    reInputCNT.setEnabled(false);
                                                    manageNumberT.setEnabled(false);
                                                    reInputMNT.setEnabled(false);
                                                    defaultB.setEnabled(false);
                                                    defaultB.removeMouseListener(mkbc2);
                                                    settedB.setEnabled(false);
                                                    settedB.removeMouseListener(mkbc3);
                                                }
                                                break;
                                                case 0: {
                                                    RandomNDF.seek(0);
                                                    RandomNDF.writeUTF(pnt1);
                                                    portN=String.valueOf(pnt1);
                                                    RandomNDF.seek(readPoint[0]);
                                                    RandomNDF.writeUTF(connectNumberT.getText());
                                                    connectN=String.valueOf(pnt2);
                                                    RandomNDF.seek(readPoint[1]);
                                                    RandomNDF.writeUTF(manageNumberT.getText());
                                                    manageN=String.valueOf(pnt4);
                                                    MainFrameThread.information.append(MainFrameThread.getTime());
                                                    information.append("ϵͳ���óɹ�\n");
                                                    openmanageB.setEnabled(true);
                                                    openmanageB.addMouseListener(mkbc1);
                                                    openmanageT.setEnabled(true);
                                                    portNumberT.setEnabled(false);
                                                    connectNumberT.setEnabled(false);
                                                    reInputCNT.setEnabled(false);
                                                    manageNumberT.setEnabled(false);
                                                    reInputMNT.setEnabled(false);
                                                    defaultB.setEnabled(false);
                                                    defaultB.removeMouseListener(mkbc2);
                                                    settedB.setEnabled(false);
                                                    settedB.removeMouseListener(mkbc3);
                                                }
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            } catch (RuntimeException i) {
                                information.append("�˿ںű���Ϊ���֣������������ַ���(\n");
                                portNumberT.setText(portN);
                            }
                        } catch (IOException ex) {
                            information.append("�����������ǿ���˳���(\n");
                            System.exit(1);
                        }
                        break;
                    }//  �趨
                    case 4: {
                        System.exit(0);
                    }//case 4   ��
                    break;
                    case 5: {
                        exitBD.dispose();
                    }//case 5   ��
                    break;
                    default:
                        break;
                    case 6: {
                        mainFrame.setVisible(true);
                        systemTray.remove(trayIcon);
                    }
                    break;
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }

        int textFiledNumberCompare(String str1, String str2) {      //����ϵͳ����ʱ������У�鷽��

            int n;
            if (str1.length() != 6) {
                information.append("�������Ϊ6λ\n");    //����6λ��
                n = 1;
                return n;
            }
            if (!str1.equals(str2)) {
                information.append("�����������벻ƥ��\n");    //�������벻ƥ�䣻
                n = 2;
                return n;
            }
            n = 0;
            return n;

        }
    }

//��ʼ��ϵͳ���̵ķ���
    private void initSystemTray() {
        if (SystemTray.isSupported()) {
            systemTray = SystemTray.getSystemTray();
        }
        trayIcon = new TrayIcon(icon.getImage());
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAndKeyOfButtonChange(6));
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            MainFrameThread.information.append("�Ҳ����ļ�:trayIcon.png");
        }
    }
}

//���������̵߳���
class startSendImageThread implements Runnable {

    static ServerSocket serverSocket;
    static Socket socket = null;
    static Thread t3;                 //ͼƬ�����߳�
    private String readedConnectN = "$$$$$$";   //����������ŵ�Ŀ���Ƿ�ֹ��connectNȡ��ͬ���ĳ�ʼ��ֵ��
    private Integer intNumber = new Integer(MainFrameThread.portN);     //�������String����ת��ΪInteger����
    static ObjectInputStream ois;
    static InputStream os;

    public void run() {
        
        try {
            serverSocket = new ServerSocket(intNumber.intValue(), 1);              //�����˿�
            MainFrameThread.information.append("�����߳������ã�\n�˿ںţ�" + intNumber.intValue() + "   �����С����ã�\n");  //����Ϣ�����У���ʾ״̬
            socket = serverSocket.accept();               //�ȴ�����
            } catch (Exception e) {
            try {
                startSendImageThread.socket.close();
                startSendImageThread.socket = null;
                startSendImageThread.serverSocket.close();
                MainFrameThread.information.append("�����˿�ʧ�ܻ�ȴ�����ʧ��\n");
                MainFrameThread.information.append("���������߳�\n");
                MainFrameThread.startsit();
            } catch (IOException ex) {
                MainFrameThread.information.append("�׽��ֹر�ʧ�ܣ�ǿ���˳�\n");
                System.exit(1);
            }
        }

        try {                                              //�ж������Ƿ���ȷ
            socket.setOOBInline(false);
            os = socket.getInputStream();
            ois =new ObjectInputStream(os);
            readedConnectN = (String) ois.readObject();  
        } catch (Exception ex) {
            try {
                startSendImageThread.socket.close();
                startSendImageThread.socket = null;
                startSendImageThread.serverSocket.close();
                startSendImageThread.ois.close();
                startSendImageThread.os.close();
                MainFrameThread.information.append("�ͻ������������ȡʧ��\n");
                MainFrameThread.information.append("�ر����佨�������ӣ����������߳�\n");
                MainFrameThread.startsit();
            } catch (IOException et) {
                MainFrameThread.information.append("�׽��ֹر�ʧ�ܣ�ǿ���˳�\n");
                System.exit(1);
            }
        }
        if (MainFrameThread.connectN.equals(readedConnectN)) {
            SendImageThread SIT = new SendImageThread(socket);         //����ͼƬ�����̶߳���
            t3 = new Thread(SIT);
            t3.setPriority(5);
            t3.start();                                             //����ͼƬ�����߳�
            MainFrameThread.information.append(MainFrameThread.getTime());
            MainFrameThread.information.append("������֤�ɹ�\n");
            try {
                serverSocket.close();                                       //�رռ�������������ֻ�ܱ�һ̨������أ�
                MainFrameThread.information.append("������������Ϣ�����߳�\n");
                MainFrameThread.information.append("�����̹߳ر�\n");
                MainFrameThread.information.append(MainFrameThread.getTime());
                MainFrameThread.information.append("���ڷ���������Ϣ����\n");
            } catch (IOException ex) {
                MainFrameThread.information.append("serverSocket�쳣��ǿ���˳�\n");
                System.exit(1);
            }
        } else {
            try {
                startSendImageThread.socket.close();
                startSendImageThread.socket = null;
                startSendImageThread.serverSocket.close();
                startSendImageThread.ois.close();
                startSendImageThread.os.close();
                MainFrameThread.information.append(MainFrameThread.getTime());
                MainFrameThread.information.append("�ͻ��������������\n");
                MainFrameThread.information.append("�ر����佨�������ӣ����������߳�\n");
                MainFrameThread.startsit();
            } catch (IOException ex) {
                MainFrameThread.information.append("�׽��ֻ�������ر�ʧ�ܣ���ǿ����ֹ\n");
                System.exit(1);
            }
        }

    }
}

//����ץͼ�߳�
/**
 * ʵ��ץͼ
 * ʵ��ͼƬ�����߳�
 * ���ݵ�ǰ���λ�û���ͼ�����Ա��λ������ڿͻ��˿ɼ�
 */
class SendImageThread implements Runnable {

    static int sleepTime=0;
    private Dimension screenSize;                          //���ڴ洢��Ļ�ߴ�
    private Rectangle rectangle;                           //���ڶ�������
    static Robot robot;                                   //���ڵ���ץͼ����
    private BufferedImage image;                           //���ڴ洢�Ƚ�ͼ��
    private BufferedImage screenImage;                     //���ڴ洢��������

    SendImageThread(Socket socket) {              //���캯��
        try {
            socket.setSendBufferSize(1024 * 1024);   //���û�������С
            socket.setTcpNoDelay(true);            //ȷ�����ݼ�ʱ����
            robot = new Robot();                     //����Robot��������ץͼ
            } catch (Exception e) {
            MainFrameThread.information.append("�޷���socket��������\n");
        }
    }

    public void run() {

        InetAddress ipAddI=startSendImageThread.socket.getLocalAddress();  //����InetAddress����
        String ipAddS=ipAddI.getHostAddress();                            //��ñ���Ip��ַ
        ControlService cs=new ControlService();                         //�����������
        cs.runns(robot, ipAddS);                                      //����RMI����������

        Toolkit toolkit = Toolkit.getDefaultToolkit();     //��̬����getDefaultToolkit�������ڻ�ȡĬ�Ϲ��߰��� ����Toolkit���󣬸ö������ʹ��Toolkit���ж��巽����������ʵ��ץͼ��
        screenSize = toolkit.getScreenSize();              //��ȡ��Ļ��С������Dimension����
        rectangle = new Rectangle(screenSize);             //����һ������Ļ�ߴ��С��ͬ������
        OutputStream outputStream = null;                  //���������
        BufferedOutputStream bout = null;                  //�������������
        try {
            outputStream = startSendImageThread.socket.getOutputStream();        //���socket�����
            bout = new BufferedOutputStream(outputStream);  //���������
            } catch (Exception e1) {
            MainFrameThread.information.append("��ȡ�����ʧ�ܣ�ǿ����ֹ\n");
            System.exit(1);
        }
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bout);    //JPEG�������������
        Image imageMouse = toolkit.getImage("mouseimage.png");
        
        while (startSendImageThread.socket != null) {                                                  //�������ͼ��
            try {
                Thread.sleep(sleepTime);
                int x = (int) MouseInfo.getPointerInfo().getLocation().getX();    //�����굱ǰλ��
                int y = (int) MouseInfo.getPointerInfo().getLocation().getY();
                screenImage = robot.createScreenCapture(rectangle);              //ץȡͼ��
                Graphics g = screenImage.getGraphics();                          //����ͼ�����
                g.drawImage(imageMouse, x, y, null);                            //�������λ�û�ͼ������ʾ���
                if (image == null) {
                    image = screenImage;                                         //�Ƚ�ͼƬ����ͬ����
                    } else {
                    image = screenImage;
                }
                encoder.encode(image);             //���뷢��
            } catch (Exception e) {
                try {
                    if (startSendImageThread.socket != null && !startSendImageThread.socket.isClosed()) {
                        startSendImageThread.socket.close();
                    }
                    startSendImageThread.socket = null;
                    bout.close();
                    outputStream.close();
                    startSendImageThread.serverSocket.close();
                    startSendImageThread.ois.close();
                    startSendImageThread.os.close();
                    MainFrameThread.information.append(MainFrameThread.getTime());
                    MainFrameThread.information.append("�ͻ����ѹر�����\n");
                    MainFrameThread.information.append("������Ϣ�����߳��ѹرգ����������߳�\n");
                    MainFrameThread.startsit();
                } catch (IOException ex) {
                    MainFrameThread.information.append("�׽��ֻ�������ر�ʧ�ܣ�ǿ����ֹ\n");
                    System.exit(1);
                }
            }
        }
    }

    private boolean ImageEquals(BufferedImage image1, BufferedImage image2) {  //ͼƬ�ȽϷ���
        int w1 = image1.getWidth();
        int h1 = image1.getHeight();
        int w2 = image2.getWidth();
        int h2 = image2.getHeight();
        if (w1 != w2 || h1 != h2) {
            return false;
        }
        for (int i = 0; i < w1; i += 4) {
            for (int j = 0; j < h1; j += 4) {
                if (image1.getRGB(i, j) != image2.getRGB(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
}
