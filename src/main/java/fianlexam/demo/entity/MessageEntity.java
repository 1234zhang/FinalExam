package fianlexam.demo.entity;

import lombok.Data;

/**
 * @author Brandon.
 * @date 2019/6/1.
 * @time 2:25.
 */

@Data
public class MessageEntity {
    private String type;

    private String username;

    private String message;

    private String hostName;

    private int onlineCount;

    public MessageEntity(String type, String username, String message, int onlineCount,String hostName){
        this.type = type;
        this.username = username;
        this.message = message;
        this.onlineCount = onlineCount;
        this.hostName = hostName;
    }
}
