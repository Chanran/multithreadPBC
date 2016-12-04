package producer;

import message.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import util.ByteUtil;

import static util.ObjectUtil.object2Bytes;

public class Producer {

    public static void main(String[] args) throws IOException {

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");  //时间格式

        boolean flag = true;
        while(flag){

            //客户端请求与本机在20011端口建立TCP连接
            Socket client = new Socket("localhost", 20011);
            client.setSoTimeout(10000);

            //键盘输入
            Scanner scan = new Scanner(System.in);

            //得到socket读写流
            OutputStream os = client.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            //输入流
            InputStream is = client.getInputStream();
            BufferedReader br =new BufferedReader(new InputStreamReader(is));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));


            System.out.println("=======  是否开始传输？Y/N  ======= " + time.format(new Date()));
            String choice = scan.next();

            System.out.println("输入起始消息id和结束消息id（如1 1000）：");
            int begin = scan.nextInt();
            int end = scan.nextInt();

            if ("N".equals(choice) || "n".equals(choice)) {
                flag = false;
            } else if ("Y".equals(choice) || "y".equals(choice)) {
                int i = begin;
                while(i <= end) {
                    try {
                        System.out.println("=======  开始传输数据  ======= " + time.format(new Date()));

                        for (i = begin; i <= end; i++) {
                            Message message = new Message(i, "这是第" + i + "条消息");
                            bw.write(message.toString());
                            bw.newLine();
                            bw.flush();
                            System.out.println("消息“" + message.toString() + "”已发送给buffer");
                        }

                        System.out.println("=======  结束传输数据  ======= " + time.format(new Date()));


                        //等待服务器消息
                /*String reply=null;
                while(!((reply=br.readLine())==null)){
                    System.out.println("接收服务器的信息："+reply);
                    if (reply == "bye"){
                        flag = false;
                    }
                }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                pw.close();
                os.close();
                br.close();
                is.close();
                client.close();
            } else {
                System.out.println("错误输入！请重新输入！");
            }
        }
    }
}