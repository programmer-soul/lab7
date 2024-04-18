package main;

import com.google.common.primitives.Bytes;
import dao.UserDAO;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

public class UDPThreadHandler implements Runnable {
    private final int PacketSize = 1024;
    private final int DataSize = PacketSize - 1;
    SocketAddress clientAddr;
    DatagramSocket datagramSocket;
    private Commands commands;
    Request request;
    ResponsePerson person;
    SQLManager sqlManager;
    private Logger logger;
    UDPThreadHandler(Request request, ResponsePerson person, SocketAddress clientAddr, DatagramSocket datagramSocket, Commands commands, SQLManager sqlManager, Logger logger){
        this.commands=commands;
        this.request=request;
        this.person=person;
        this.logger=logger;
        this.sqlManager=sqlManager;
        this.clientAddr=clientAddr;
        this.datagramSocket=datagramSocket;
    }
    @Override
    public void run()  {
        UserDAO user= new UserDAO(request.getUsername(),request.getPassword());
        user.setId(sqlManager.authenticateUser(request.getUsername(),request.getPassword()));
        ResponseManager responsemanager  = commands.execute(request.getCommand(),request.getParametr(),person,user,sqlManager,false);
        if (responsemanager.size()==0){
            responsemanager.addResponse("Неизвестная ошибка!");
            logger.info("ResponseManager Неизвестная ошибка!" );
        }
        Thread UDPsender = new Thread() {
            public void run() {
                logger.info("Отправка данных клиенту " + clientAddr);
                var data = SerializationUtils.serialize(responsemanager.responses);
                try {
                    sendData(data, clientAddr);
                    logger.info("Отправлен ответ клиенту " + clientAddr);
                } catch (Exception e) {
                    logger.log(Level.SEVERE,"Ошибка ввода-вывода.", e);
                }

                if (responsemanager.isPerson()){
                    var data2 = SerializationUtils.serialize(responsemanager.persons);
                    try {
                        sendData(data2, clientAddr);
                        logger.info("Отправлен ответ клиенту " + clientAddr);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE,"Ошибка ввода-вывода.", e);
                    }
                }
                datagramSocket.disconnect();
                logger.info("Отключение от клиента " + clientAddr);
            }
        };
        UDPsender.start();
    }
    public void sendData(byte[] data, SocketAddress addr) throws IOException {
        byte[][] ret = new byte[(int)Math.ceil(data.length / (double)DataSize)][DataSize];

        int start = 0;
        for(int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(data, start, start + DataSize);
            start += DataSize;
        }

        logger.info("Отправляется " + ret.length + " чанков...");
        for(int i = 0; i < ret.length; i++) {
            var chunk = ret[i];
            if (i == ret.length - 1) {
                var lastChunk = Bytes.concat(chunk, new byte[]{1});
                var dp = new DatagramPacket(lastChunk, PacketSize, addr);
                datagramSocket.send(dp);
                logger.info("Последний чанк размером " + chunk.length + " отправлен на сервер.");
            } else {
                var dp = new DatagramPacket(ByteBuffer.allocate(PacketSize).put(chunk).array(), PacketSize, addr);
                datagramSocket.send(dp);
                logger.info("Чанк размером " + chunk.length + " отправлен на сервер.");
            }
        }
        logger.info("Отправка данных завершена");
    }
}
