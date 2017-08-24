import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by mpampis on 1/10/2016.
 */
public class Client {

    private static int clients = 1;

    private Socket socket = null;
    private int ID = -1;
    private String username;
    private DataOutputStream toClient;


    public Client(Socket socket,String username){
        this.socket = socket;
        this.username = username;
        this.ID = clients++;
        ChatServer.clientList.add(this);
        try{
            this.toClient = new DataOutputStream(socket.getOutputStream());
        }
        catch(IOException e){

        }

    }

    public Client(){
        // Empty constructor
    }

    public static int getClients() {
        return clients;
    }

    public static void setClients(int clients) {
        Client.clients = clients;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void sendMsg(String username,String message){
        if(username.equals(this.getUsername())) return;
        try{
            toClient.writeUTF(username);
            toClient.writeUTF(message);
            toClient.flush();
        }
        catch(IOException e){
            System.out.println("Client αποσυνδεθηκε "+this.getUsername()+" ID: "+this.getID());
            ChatServer.deleteClient(this.getID());
        }
    }
}
