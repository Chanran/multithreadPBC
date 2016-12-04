package message;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = -389326121047047723L;
    private int id;
    private String content;
    public Message(int id, String content) {
        this.id = id;
        this.content = content;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String toString(){
        return this.getId() + "----" + this.getContent();
    }
    public static Message stringToMessage(String string){
        String[] tmp = string.split("----");
        int id = Integer.parseInt(tmp[0]);
        String content = tmp[1];
        Message message = new Message(id,content);
        return message;
    }
}