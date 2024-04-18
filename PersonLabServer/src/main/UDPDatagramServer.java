package main;
import org.hibernate.SessionFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.*;

public class UDPDatagramServer implements Runnable{
    private static final int CountHandlers = 10;
    private Commands commands;
    private SessionFactory sessionFactory;
    private SQLManager sqlManager;
    private Logger logger;
    InetAddress address;
    int port;

    public UDPDatagramServer(InetAddress address, int port,Commands commands, SessionFactory sessionFactory, SQLManager sqlManager, Logger logger) {
        this.address=address;
        this.port=port;
        this.commands=commands;
        this.logger=logger;
        this.sessionFactory=sessionFactory;
        this.sqlManager=sqlManager;
    }
    @Override
    public void run() {
        logger.info("Сервер запущен по адресу " + address+":"+port);
        ThreadPoolExecutor executorHandler = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        ThreadPoolExecutor executorReceiver = (ThreadPoolExecutor) Executors.newFixedThreadPool(CountHandlers);
        try{
            for(int i=0;i<CountHandlers;i++){
                executorReceiver.execute(new UDPThreadReceiver(new InetSocketAddress(address, port+i),executorHandler,commands,sessionFactory,sqlManager,logger));
            }
        } catch (SocketException e) {
            System.out.println("Ошибка сокета");
            logger.log(Level.SEVERE,"Ошибка сокета", e);
        }
    }
}
