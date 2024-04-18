package main;

import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.SessionFactory;

import java.util.concurrent.ThreadPoolExecutor;

import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class UDPThreadReceiver implements Runnable {
    private final int PacketSize = 1024;
    private final int DataSize = PacketSize - 1;
    private ThreadPoolExecutor executorHandler;
    private Commands commands;
    private Logger logger;
    private SessionFactory sessionFactory;
    private SQLManager sqlManager;
    private boolean running = true;
    private DatagramSocket datagramSocket;

    public UDPThreadReceiver(InetSocketAddress socketAddress,ThreadPoolExecutor executorHandler, Commands commands, SessionFactory sessionFactory, SQLManager sqlManager, Logger logger) throws SocketException {
        this.executorHandler=executorHandler;
        this.commands=commands;
        this.logger=logger;
        this.sessionFactory=sessionFactory;
        this.sqlManager=sqlManager;
        datagramSocket = new DatagramSocket(null);//socketAddress
        datagramSocket.setReuseAddress(true);
        datagramSocket.bind(socketAddress);
        datagramSocket.setSoTimeout(100);
    }
    /**
     * Запуск цикла проверки на получение данных
     */
    @Override
    public void run()  {
        while (running) {
            Pair<Byte[], SocketAddress> dataPair;
            try {
                dataPair = receiveData();
            } catch (Exception e) {
                logger.log(Level.SEVERE,"Ошибка получения данных.", e);
                datagramSocket.disconnect();
                continue;
            }

            var dataFromClient = dataPair.getKey();
            SocketAddress clientAddr = dataPair.getValue();

            try {
                datagramSocket.connect(clientAddr);
                logger.info("Соединено с " + clientAddr);
            } catch (Exception e) {
                logger.log(Level.SEVERE,"Ошибка соединения с клиентом.", e);
            }
            Request request;
            try {
                request = (Request)SerializationUtils.deserialize(ArrayUtils.toPrimitive(dataFromClient));
                logger.info("Обработка " + request.getCommand() + " из " + clientAddr);
                System.out.println("Обработка " + request.getCommand() + " из " + clientAddr);
            } catch (SerializationException e) {
                logger.log(Level.SEVERE,"Невозможно десериализовать объект запроса Request.", e);
                datagramSocket.disconnect();
                continue;
            }

            ResponsePerson person = null;
            if (request.getCommand().equals("update")||request.getCommand().equals("insert")||request.getCommand().equals("replace_if_lower")){
                try {
                    RequestPerson requestperson = (RequestPerson)SerializationUtils.deserialize(ArrayUtils.toPrimitive(dataFromClient));
                    person=requestperson.person;
                } catch (SerializationException e) {
                    logger.log(Level.SEVERE,"Невозможно десериализовать объект запроса RequestPerson.", e);
                    datagramSocket.disconnect();
                    continue;
                }
            }

            executorHandler.submit(new UDPThreadHandler(request, person, clientAddr, datagramSocket, commands, sqlManager, logger));

        }
        datagramSocket.close();
    }
    /**
     * Получает данные с клиента.
     * Возвращает пару из данных и адреса клиента
     */
    public Pair<Byte[], SocketAddress> receiveData() throws IOException {
        var received = false;
        var result = new byte[0];
        SocketAddress addr = null;

        while(!received) {
            var data = new byte[PacketSize];
            var dp = new DatagramPacket(data, PacketSize);
            try{
                datagramSocket.receive(dp);
                addr = dp.getSocketAddress();
                logger.info("Получено \"" + new String(data) + "\" от " + dp.getAddress());
                logger.info("Последний байт: " + data[data.length - 1]);

                if (data[data.length - 1] == 1) {
                    received = true;
                    logger.info("Получение данных от " + dp.getAddress() + " окончено");
                }
                result = Bytes.concat(result, Arrays.copyOf(data, data.length - 1));
            } catch (SocketTimeoutException ignored) {
            }
        }
        return new ImmutablePair<>(ArrayUtils.toObject(result), addr);
    }

    public void stop() {
        running = false;
    }

}
