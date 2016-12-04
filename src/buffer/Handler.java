package buffer;

import message.Message;
import util.RedisUtil;
import util.ObjectUtil;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Handler extends Thread{
    SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");  //时间格式

    private Socket client = null;
    private static byte[] rediskey = "key".getBytes();
    //获得输入流
    InputStream is;
    BufferedReader br;
    //获得输出流
    OutputStream os;
    PrintWriter pw;

    public Handler(Socket client) throws IOException {
        this.client = client;

        is = client.getInputStream();
        br = new BufferedReader(new InputStreamReader(is));

        os = client.getOutputStream();
        pw =new PrintWriter(os);

        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void run(){

        try{
            String messageString = null;
            System.out.println("=====  消息开始进入redis队列  ======= " + time.format(new Date()));
            while((messageString = br.readLine()) != null){
                Message message = Message.stringToMessage(messageString);
                RedisUtil.lpush(rediskey,ObjectUtil.object2Bytes(message));
                System.out.println("消息“"+message.toString()+"”已经进入redis队列");
            }
            System.out.println("=====  消息结束进入redis队列  ======= " + time.format(new Date()));

            new SendToConsumer();

            //给客户一个响应
            String reply="received";
            pw.write(reply);
            pw.flush();

            pw.close();
            os.close();
            br.close();
            is.close();
            client.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}