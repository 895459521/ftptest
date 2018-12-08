package cn.charmsongo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


public class FtpTool {

	//ftp����
	private FTPClient ftp;
	
	private InputStream is = null;
	private OutputStream os  = null;
	private FileOutputStream fos = null;
	private FileInputStream fis = null;
	
	
	/**
	 * ��֤��¼
	 * @param ip
	 * @param port
	 * @param name
	 * @param pwd
	 * @return
	 */
	public boolean login(String ip,int port, String name, String pwd) {
		try {
			ftp = new FTPClient();
			ftp.connect(ip, port);
			System.out.println(ftp.login(name, pwd));
			if(!ftp.login(name, pwd)){
				return false;
			}
			ftp.setCharset(Charset.forName("UTF-8"));
			ftp.setControlEncoding("UTF-8");

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	 
	/**
	 * ��ȡftpĳһ�ļ���·�����µ��ļ�����,���ڲ鿴�ļ��б�
	 * @param ip
	 * @param port
	 * @param name
	 * @param pwd
	 * @param remotedir Զ�̵�ַĿ¼
	 * @return
	 */
    public boolean getFilesName(String ip,int port, String name, String pwd, String remotedir) {
        try {
        	if(!login(ip, port, name, pwd)){
				return false;
			}
        	
            //��ȡftp���棬ָ���ļ��� ������ļ����֣�����������
            FTPFile[] files = ftp.listFiles(remotedir);
            //��ӡ��ftp���棬ָ���ļ��� ������ļ�����
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i].getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally{
        	this.close();
        }
        return true;
    }
    
    
    /**
     * �ϴ��ļ� ����һ
     * @param ip
     * @param port
     * @param name
     * @param pwd
     * @param remotepath Զ�̵�ַ�ļ�·��
     * @param localpath �����ļ�·��
     * @return
     */
    public boolean putFileOne(String ip,int port, String name, String pwd,String remotepath,String localpath) {
        try {
        	if(!login(ip, port, name, pwd)){
				return false;
			}
            //�����ص� localpath �ļ��ϴ���ftp�ĸ�Ŀ¼�ļ������棬��������Ϊ remotepath�е�����
        	 return ftp.storeFile(remotepath, new FileInputStream(new File(localpath)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally{
        	this.close();
        }
//        return true;
    }

    
    /**
     * �ϴ��ļ��ĵڶ��ַ������Ż��˴����ٶ�
     * @param ip
     * @param port
     * @param name
     * @param pwd
     * @param remotepath Զ�̵�ַ�ļ�·��
     * @param localpath �����ļ�·��
     * @return
     */
    public boolean putFileTwo(String ip,int port, String name, String pwd,String remotepath,String localpath) {
        try {
        	if(!login(ip, port, name, pwd)){
				return false;
			}
        	
            os = ftp.storeFileStream(remotepath);
            fis = new FileInputStream(new File(localpath));

            byte[] b = new byte[1024];
            int len = 0;
            while ((len = fis.read(b)) != -1) {
                os.write(b,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
        	this.close();
		}
        return true;
    }

    
    /**
     * �����ļ� ����һ
     * @param ip
     * @param port
     * @param name
     * @param pwd
     * @param remotepath Զ�̵�ַ�ļ�·��
     * @param localpath �����ļ�·��
     * @return
     */
    public boolean getFileOne(String ip,int port, String name, String pwd,String remotepath,String localpath) {
        try {
        	if(!login(ip, port, name, pwd)){
				return false;
			}
        	
            //��ftp��Դ�� remotepath �ļ����ص�����Ŀ¼�ļ������棬��������Ϊ localpath �е�����
        	return ftp.retrieveFile(remotepath, new FileOutputStream(new File(localpath)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally{
        	this.close();
        }
//        return true;
    }

	
    /**
     * �����ļ��ĵڶ��ַ������Ż��˴����ٶ�
     * @param ip
     * @param port
     * @param name
     * @param pwd
     * @param remotepath Զ�̵�ַ�ļ�·��
     * @param localpath  �����ļ�·��
     * @return
     */
	public boolean getFileTwo(String ip,int port, String name, String pwd,String remotepath,String localpath) {

		try {
			if(!login(ip, port, name, pwd)){
				return false;
			}

			is = ftp.retrieveFileStream(remotepath);
			fos = new FileOutputStream(new File(localpath));

			byte[] b = new byte[1024];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				fos.write(b,0,len);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally {
			this.close();
		}
		return true;
	}

	private void close(){
		
		if(ftp.isConnected()){
			try {
				ftp.disconnect();
			} catch (IOException e) {
			}
		}
		if(is != null){
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		if(os != null){
			try {
				os.close();
			} catch (IOException e) {
			}
		}
		if(fis != null){
			try {
				fis.close();
			} catch (IOException e) {
			}
		}
		if(fos != null){
			try {
				fos.close();
			} catch (IOException e) {
			}
		}
	}

	public static void main(String[] args) {

		//		TestFileApi m = new TestFileApi("192.168.19.147",21,"tom","tom");
		FtpTool f = new FtpTool();
//		System.out.println(f.getFileOne("192.168.19.147",21,"tom","tom","/home/tom/User.txt","F:/User.txt"));
//		f.putFileTwo("192.168.19.147",21,"tom","tom","/home/tom/User1.txt","F:/User.txt");
		f.getFilesName("192.168.19.100",21,"administrator","1111", "/");
//		f.putFileOne("192.168.19.147",211,"tom","tom","/home/tom/User.txt","F:/User.txt");
//		f.login("192.168.19.147",21,"tom","tom");
//        boolean rv = f.getFileTwo("192.168.19.100",21,"administrator","Jh-Cs1)2(","/User.txt","F:/User.txt");
	}

}
