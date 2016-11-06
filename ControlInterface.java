package Service;


import java.rmi.Remote;
import java.rmi.RemoteException;
public interface ControlInterface extends Remote {

    public void controlKeyPress(int keycode) throws RemoteException;    // ִ��Զ����������ָ�������İ��¶���
	
    public void controlKeyRelease(int keycode) throws RemoteException;   // ִ��Զ����������ָ��������̧����
	
    public void controlMouseMove(int x, int y) throws RemoteException;    // ִ��Զ������������ƶ�����
	
    public void controlMousePress(int buttons) throws RemoteException;    // ִ��Զ�����������ָ�������İ��¶���
	
    public void controlMouseRelease(int buttons) throws RemoteException;  // ִ��Զ�����������ָ���������ͷŶ���
	
    public void controlMouseWheel(int wheelAmt) throws RemoteException;   // ִ��Զ�������������ֶ���

    public void sleepTime(int t) throws RemoteException;                   // �趨����ʱ�䣬�Ժ���Ϊ��λ
}