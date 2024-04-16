package main;

import com.google.common.primitives.Bytes;
import dao.UserDAO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.SessionFactory;

import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.IntStream;
/**
 * Класс отвечает за обмен данными между клиентом и сервером
 * по протоколу UDP с использованием Datagram
 * @author Matvei Baranov
 */
public class UDPDatagramServer {
  private final InetSocketAddress addr;
  private final Commands commands;
  private Runnable hook;

  private boolean running = true;

  private final int PacketSize = 1024;
  private final int DataSize = PacketSize - 1;

  private final DatagramSocket datagramSocket;
  private SessionFactory sessionFactory;
  private SQLManager sqlManager;
  private final Logger logger;

  public UDPDatagramServer(InetSocketAddress addr, Commands commands, SessionFactory sessionFactory,SQLManager sqlManager, Logger logger) throws SocketException {
    this.addr=addr;
    this.commands=commands;
    this.sessionFactory=sessionFactory;
    this.sqlManager=sqlManager;
    this.logger=logger;
    this.datagramSocket = new DatagramSocket(addr);
    this.datagramSocket.setReuseAddress(true);
  }
  /**
   * Запуск цикла проверки на получение данных
   */
  public void run() {
    logger.info("Сервер запущен по адресу " + addr);
    while (running) {
      Pair<Byte[], SocketAddress> dataPair;
      try {
        dataPair = receiveData();
      } catch (Exception e) {
        logger.log(Level.SEVERE,"Ошибка получения данных.", e);
        disconnectFromClient();
        continue;
      }

      var dataFromClient = dataPair.getKey();
      var clientAddr = dataPair.getValue();

      try {
        connectToClient(clientAddr);
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
        disconnectFromClient();
        continue;
      }

      ResponsePerson person = null;
      if (request.getCommand().equals("update")||request.getCommand().equals("insert")||request.getCommand().equals("replace_if_lower")){
        try {
          RequestPerson requestperson = (RequestPerson)SerializationUtils.deserialize(ArrayUtils.toPrimitive(dataFromClient));
          person=requestperson.person;
        } catch (SerializationException e) {
          logger.log(Level.SEVERE,"Невозможно десериализовать объект запроса RequestPerson.", e);
          disconnectFromClient();
          continue;
        }
      }
      UserDAO user= new UserDAO(request.getUsername(),request.getPassword());
      user.setId(sqlManager.authenticateUser(request.getUsername(),request.getPassword()));
      ResponseManager responsemanager  = commands.execute(request.getCommand(),request.getParametr(),person,user,sqlManager,false);
      if (responsemanager.size()==0){
        responsemanager.addResponse("Неизвестная ошибка!");
        logger.info("ResponseManager Неизвестная ошибка!" );
      }

      var data = SerializationUtils.serialize(responsemanager.responses);
      try {
        if (hook != null) hook.run();
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

      disconnectFromClient();
      logger.info("Отключение от клиента " + clientAddr);
    }

    close();
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
      datagramSocket.receive(dp);

      addr = dp.getSocketAddress();
      logger.info("Получено \"" + new String(data) + "\" от " + dp.getAddress());
      logger.info("Последний байт: " + data[data.length - 1]);

      if (data[data.length - 1] == 1) {
        received = true;
        logger.info("Получение данных от " + dp.getAddress() + " окончено");
      }
      result = Bytes.concat(result, Arrays.copyOf(data, data.length - 1));
    }
    return new ImmutablePair<>(ArrayUtils.toObject(result), addr);
  }
  /**
   * Отправляет данные клиенту
   */
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

  /**
   * Вызывает хук после каждого запроса.
   * @param hook, вызываемый после каждого запроса
   */
  public void setHook(Runnable hook) {
    this.hook = hook;
  }
  public void stop() {
    running = false;
  }
  public void connectToClient(SocketAddress addr) throws SocketException {
    datagramSocket.connect(addr);
  }
  public void disconnectFromClient() {
    datagramSocket.disconnect();
  }
  public void close() {
    datagramSocket.close();
  }
}
