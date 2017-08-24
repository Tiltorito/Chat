import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by mpampis on 2/10/2016.
 */

public class Client {

    private static final String hostname = "localhost";
    private static final int port = 6000;

    public static void main(String[] args){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Scanner scanner = new Scanner(System.in);
                        Socket socket = new Socket(hostname, port);

                        DataInputStream fromServer = new DataInputStream(socket.getInputStream());
                        DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());

                        System.out.print("Η συνδεση πετυχε βαλε ενα username: ");
                        String username = scanner.nextLine();

                        toServer.writeUTF(username);
                        toServer.flush();


                        int ID = fromServer.readInt();

                        System.out.println("Ο σερβερ σου εδωσε το ID: "+ID);

                        new Thread(() ->{
                            while(true){
                                try {
                                    String name = fromServer.readUTF();
                                    String message = fromServer.readUTF();
                                    System.out.println("Ο χρηστης "+name+" ειπε --> "+message);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    System.exit(1);
                                }
                            }
                        }).start();
                        String command;
                        String msg;
                        int commandID = 1;
                        String address = "";
                        while(true){

                            command = scanner.next();
                            if(command.equals("SendAll")) {
                                toServer.writeInt(1);
                                msg = scanner.nextLine();
                                toServer.writeUTF(msg);
                            }
                            else if(command.equals("SendTo")){
                                address = scanner.next();
                                msg = scanner.nextLine();
                                toServer.writeInt(2);
                                toServer.writeUTF(address);
                                toServer.writeUTF(msg);

                            }
                            else{
                                System.out.println("λαθος εντολη :/");
                            }


                            toServer.flush();
                        }
                    }
                    catch(IOException e){
                        System.out.println("Αποσυνδεθηκες :(");
                        System.exit(1);
                    }
                }
            }).start();

        }

}

