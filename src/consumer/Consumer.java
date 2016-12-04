package consumer;

import java.net.ServerSocket;
import java.net.Socket;

public class Consumer {
    public static void main(String[] args) throws Exception{
        ServerSocket server = new ServerSocket(20012);
        Socket buffer = null;
        while(true){
            System.out.println("consumer等待buffer发起连接请求");
            buffer = server.accept();    //接收多个客户端，创建多个线程处理请求
            new consumer.Handler(buffer);
            System.out.println("buffer向consumer发起了连接请求,且连接成功");
        }
    }
}
