package buffer;


import message.Message;
import util.ObjectUtil;
import util.RedisUtil;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SendToConsumer extends Thread{
    SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");  //时间格式

    private Socket buffer;
    private static byte[] rediskey = "key".getBytes();

    InputStream is;
    BufferedReader br;
    BufferedWriter bw;

    SendToConsumer() throws Exception {
        try {
            buffer = new Socket("localhost", 20012);
            is = buffer.getInputStream();
            br =new BufferedReader(new InputStreamReader(is));
            bw = new BufferedWriter(new OutputStreamWriter(buffer.getOutputStream()));

            start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        byte[] bytes = null;
        System.out.println("=====  开始发送消息给消费者  ======= " + time.format(new Date()));
        while( (bytes = RedisUtil.rpop(rediskey)) != null ){
            Message message = null;
            try {
                message = Message.stringToMessage((ObjectUtil.bytes2Object(bytes)).toString());
                if(message != null){
                    System.out.println("消息“"+message.toString()+"”已发送到消费者");
                    bw.write(message.toString());
                    bw.newLine();
                    bw.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            is.close();
            br.close();
            bw.close();
            buffer.close();
        }catch (Exception e){

        }
        System.out.println("=====  结束发送消息给消费者  ======= " + time.format(new Date()));
    }
}
