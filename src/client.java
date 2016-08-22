import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client = new Socket("localhost", 8090);
		ArrayList <String> str = new ArrayList<>();
		new Thread(new send(client)).start();
		new Thread(new recive(client)).start();
	}
}

class send implements Runnable {
	
	protected BufferedReader is		= null;
	protected DataOutputStream dos  = null;
	protected String msg 			= null;
	protected boolean flag 			= false;

	public send(Socket socket) throws IOException {
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			is = new BufferedReader(new InputStreamReader(System.in));
			flag = true;
		} catch (IOException e) {
			dos.close();
			is.close();
		}
	}

	@Override
	public void run() {
		System.out.println(flag);
		while (flag) {
			try {
				msg = is.readLine();
				System.out.println("我说"+msg);
				dos.writeUTF(msg);
				System.out.println("传输完毕");
			} catch (IOException e) {
				System.out.println("异常");
					flag=false;
					//dos.close();
					//is.close();
			}
		}
	}
}


class recive implements Runnable {

	protected DataInputStream dis = null;
	protected String msg = null;
	protected boolean flag =false;
	
	public recive(Socket socket) {
		try {
			dis = new DataInputStream(socket.getInputStream());
			flag= true;
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (flag) {
			try {
				msg = dis.readUTF();
				System.out.println(msg);
			} catch (IOException e) {
				try {
					flag=false;
					dis.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
	}

}