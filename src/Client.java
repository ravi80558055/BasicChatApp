import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;

    public Client() {
        System.out.println("CLIENT: Sending connection request to server...");
        try {
            socket = new Socket("127.0.0.1",3232);
            System.out.println("CONNECTION ESTABLISHED");
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
            getDataFromServer();
            sendDataToServer();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDataFromServer() {
        Runnable runnable1 = ()->{
            System.out.println("getDataFromServer() -> runnable1");
            try {
                while(!socket.isClosed()) {
                    String message = bufferedReader.readLine();
                    if(message.equals("EXIT")) {
                        System.out.println("Session terminated by Server...");
                        socket.close();
                        break;
                    }
                    System.out.println("SERVER: " + message);
                }
            }catch (SocketException se) {
                System.out.println("CONNECTION TERMINATED");
            }catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(runnable1).start();
    }

    public void sendDataToServer() {
        Runnable runnable2 = ()->{
            System.out.println("sendDataToServer() -> runnable2");
            try {
                while(!socket.isClosed()) {
                    BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(System.in));
                    String message = bufferedReader1.readLine();
                    printWriter.println(message);
                    printWriter.flush();
                    if(message.equals("EXIT")) {
                        socket.close();
                        break;
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(runnable2).start();
    }

    public static void main(String[] args) {
        new Client();
    }
}
