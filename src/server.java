import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class server {
	public static void main(String[] args) throws IOException {
		SocketThread socketThread = null;
		Socket socket = null;
		int i = 0;
		List<SocketThread> st = new ArrayList<>();

		// 接受服务器
		ServerSocket server = new ServerSocket(8090);
		// 等待链接
		while (true) {
			socket = server.accept();
			socketThread = new SocketThread(socket, "编号" + i, st);
			i++;
			Thread th = new Thread(socketThread);
			st.add(socketThread);
			th.start();
		}

	}
}

class SocketThread implements Runnable {

	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	String msg = null;
	Socket socket = null;
	List<SocketThread> st = null;

	SocketThread temp = null;
	String name = null;

	public SocketThread(Socket socket, String name, List<SocketThread> st) {
		this.socket = socket;
		this.name = name;
		this.st = st;

		try {
			this.dis = new DataInputStream(this.socket.getInputStream());
			this.dos = new DataOutputStream(this.socket.getOutputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while (true) {
			try {
				msg = this.name + "说:" + dis.readUTF();

				for (int i = 0; i < st.size(); i++) {
					temp = st.get(i);

					temp.dos.writeUTF(msg);
					temp.dos.flush();
				}
			} catch (IOException e) {
					try {
						dos.close();
						dis.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					

			}
		}

	}
}