package fianlexam.demo.websocketServer;

import fianlexam.demo.config.HttpSessionConfig;
import fianlexam.demo.entity.MessageEntity;
import fianlexam.demo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Brandon.
 * @date 2019/6/1.
 * @time 2:36.
 */

@Slf4j
@Component
@ServerEndpoint(value = "/play/{type}",configurator = HttpSessionConfig.class)
public class PlayServer {
    private static Map<String, Session> onlineMap = new ConcurrentHashMap<>();

    private String username;

    @OnOpen
    public void onopen(Session session, EndpointConfig endpointConfig){
        HttpSession httpSession = (HttpSession) endpointConfig.getUserProperties().get(HttpSession.class.getName());
        this.username = (String) httpSession.getAttribute("username");
        onlineMap.put(username, session);
        sendMessageToAll(JsonUtil.toJson(new MessageEntity("Enter",username,
                username + " enter the room",onlineMap.size())));
    }

    @OnMessage
    public void onMessage(Session session,String message){
        sendMessageToAll(message);
    }

    @OnClose
    public void onClose(Session session){
        onlineMap.remove(this.username);
        sendMessageToAll(JsonUtil.toJson(new MessageEntity("Leave",username,username + "leave the room",onlineMap.size())));
    }

    @OnError
    public void onError(Session session,Throwable e){
        log.error("there happen some error : " + e.getMessage());
    }

    private void sendMessageToAll(String message){
        onlineMap.forEach((username,session)->{
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
