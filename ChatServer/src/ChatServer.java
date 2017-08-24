import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by mpampis on 1/10/2016.
 */
public class ChatServer {

    private static final int port = 6000;

    public static ArrayList<Client> clientList = new ArrayList<Client>();

    public static void main(String[] args){

        System.out.println("O σερβερ ξεκινησε στην πορτα --> "+port);

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new HandleAclient(clientSocket)).start();
                }
            }
            catch(IOException e){
                System.out.println("ΚΑΤΙ ΠΗΓΕ ΣΚΑΤΑ!");
            }

        }).start();
    }

    public synchronized static void deleteClient(int ID){
        if(ID == -1) return;
        int size = clientList.size();

        for(int index = 0;index < size;index++){
            if(clientList.get(index).getID() == ID){
                clientList.remove(index);
            }
        }
    }

    public static void updateAllClients(String username,String message){
        new Thread(() -> {
            int size = clientList.size();
            for(int i = 0;i < size;i++){
                clientList.get(i).sendMsg(username,message);
            }
        }).start();
    }

    public static void updateSpecificClient(String username,String message,String client){
        new Thread(() -> {
            int size = clientList.size();
            for(int i = 0;i < size;i++){
                if(clientList.get(i).getUsername().equals(client)){
                    clientList.get(i).sendMsg(username,message);
                }
            }
        }).start();
    }

}
