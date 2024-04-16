import main.UDPDatagramClient;
import main.Commands;
import java.util.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * Программа PersonLab Client
 * @author Matvei Baranov
 * @version 4.0
 */
public class Main {
    public static Logger logger = Logger.getLogger("ClientLog");
    public static Commands commands = new Commands();
    public static void main(String[] args) {
        System.out.println("Программа PersonLab Client.");
        Scanner scanner = new Scanner(System.in);
        boolean exit=false;
        boolean auth=false;
        while (!exit) {
            try {
                UDPDatagramClient client = new UDPDatagramClient(new InetSocketAddress(InetAddress.getLocalHost(), 25555),logger);
                if (auth){
                    String comstr = scanner.nextLine();
                    exit=commands.execute(client,comstr,false);
                }
                else{
                    System.out.println("Введите имя пользователя");
                    String username = scanner.nextLine();
                    System.out.println("Введите пароль");
                    String password = scanner.nextLine();
                    commands.setUserAuth(username,password);
                    auth=commands.execute(client,"authenticate onstart",false);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE,"Невозможно подключиться к серверу.", e);
                System.out.println("Невозможно подключиться к серверу!");
            }
        }
    }
}