import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    ServerSocket serverSocket;
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    public Server() {
        try {
            serverSocket = new ServerSocket(3232);
            System.out.println("SERVER: Waiting for connection...");
            socket = serverSocket.accept();
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
            getDataFromClient();
            sendDataToClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //startReading() --> getDataFromClient()
    public void getDataFromClient() {
        //Thread: To read data from Client line by line
        Runnable runnable1 = ()->{
            //Debug Statement
            System.out.println("getDataFromClient() -> runnable1 started...");
            try {
                while(!socket.isClosed()) {
                    String clientsMessage = bufferedReader.readLine();
                    if(clientsMessage.equals("EXIT")) {
                        System.out.println("Session terminated by Client...");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: " + clientsMessage);
                }
            }catch (SocketException se) {
                System.out.println("CONNECTION TERMINATED");
            }catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(runnable1).start();
    }

    //startWriting() --> sentDataToClient();
    public void sendDataToClient() {
        //Thread: Take data from user and will send it to client
        Runnable runnable2 = ()->{
            //Debug Statement
            System.out.println("sentDataToClient() -> runnable2 started...");
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
        new Server();
    }
}