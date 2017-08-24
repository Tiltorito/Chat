import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by mpampis on 1/10/2016.
 */
public class HandleAclient implements Runnable {
    private Socket socket;
    private DataInputStream fromClient;
    private DataOutputStream toClient;
    private String clientName = "Αγνωστος";
    private Client client = new Client();
    private boolean isConnected = false;

    public HandleAclient(Socket socket){

        this.socket = socket;
        try {

            this.fromClient = new DataInputStream(socket.getInputStream());
            this.toClient = new DataOutputStream(socket.getOutputStream());

            clientName = fromClient.readUTF();

            client = new Client(socket,clientName);
            System.out.println("Client συνδεθηκε "+client.getUsername()+" ID: "+client.getID());
            isConnected = true;
        }
        catch(IOException e){
            System.out.println("Client αποσυνδεθηκε "+client.getUsername()+" ID: "+client.getID());
            ChatServer.deleteClient(client.getID());
            isConnected = false;

        }

    }

    @Override
    public void run() {
        try {
            toClient.writeInt(client.getID());

        }
        catch(IOException e){
            e.printStackTrace();
        }
        String message;
        while(isConnected){
            try{
                int command = fromClient.readInt();


                switch (command){
                    case 1:
                        message = fromClient.readUTF();
                        ChatServer.updateAllClients(client.getUsername(),message);
                        System.out.println("O "+client.getUsername()+" εστειλε: "+message);
                        break;
                    case 2:
                        String address = fromClient.readUTF();
                        message = fromClient.readUTF();
                        System.out.println("O "+client.getUsername()+" εστειλε: "+message+" στον "+address);
                        ChatServer.updateSpecificClient(client.getUsername(),message,address);
                }

            }
            catch(IOException e){
                System.out.println("Client αποσυνδεθηκε "+client.getUsername()+" ID: "+client.getID());
                isConnected = false;

            }
        }
    }


}
