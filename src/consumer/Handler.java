package consumer;

import message.Message;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Handler extends Thread{
    private Socket client;
    //获得输入流
    InputStream is;
    BufferedReader br;
    //获得输出流
    OutputStream os;
    PrintWriter pw;
    SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");  //时间格式

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

            System.out.println("======  开始接收buffer数据  ======= " + time.format(new Date()));
            while((messageString = br.readLine()) != null){
                Message message = Message.stringToMessage(messageString);
                System.out.println("消息“"+message.toString()+"”消费者已经收到！");
            }
            System.out.println("======  结束接收buffer数据  ======= " + time.format(new Date()));

            //给buffer一个响应
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