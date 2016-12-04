package buffer;

import java.net.ServerSocket;
import java.net.Socket;

public class Buffer{
    public static void main(String[] args) throws Exception{
        ServerSocket server = new ServerSocket(20011);
        Socket client = null;

        while(true){
            System.out.println("buffer等待producer发起连接请求");

            client = server.accept();    //接收多个客户端，创建多个线程处理请求

            //between buffer(client) and consumer(server)
            new SendToConsumer(); //创建线程专门读取redis,发送到consumer.

            //between producer(client) and buffer(server)
            new buffer.Handler(client); //创建线程处理producer传过来的消息

            System.out.println("buffer向producer发起了连接请求,且连接成功");
        }
    }
}