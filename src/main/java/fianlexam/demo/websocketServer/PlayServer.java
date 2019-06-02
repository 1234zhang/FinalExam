package fianlexam.demo.websocketServer;

import fianlexam.demo.config.HttpSessionConfig;
import fianlexam.demo.entity.MessageEntity;
import fianlexam.demo.entity.ResultEntity;
import fianlexam.demo.service.PlayService;
import fianlexam.demo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

    static PlayService playService;

    static RedisTemplate<String,String> redisTemplate;

    HttpSession httpSession;

    @Autowired
    public void setPlayService(PlayService playService,RedisTemplate<String,String> redisTemplate){
        PlayServer.playService = playService;
        PlayServer.redisTemplate = redisTemplate;
    }
    @OnOpen
    public void onopen(Session session, EndpointConfig endpointConfig){
        HttpSession httpSession = (HttpSession) endpointConfig.getUserProperties().get(HttpSession.class.getName());
        this.httpSession = httpSession;
        this.username = (String) httpSession.getAttribute("username");
        onlineMap.put(username, session);
        sendMessageToAll(JsonUtil.toJson(new MessageEntity("Enter",username,
                username + " enter the room",onlineMap.size(),"大厅")));
    }

    @OnMessage
    public void onMessage(Session session,String message){
        MessageEntity messageEntity = JsonUtil.fromJson(message);
        if("Play".equals(messageEntity.getType())){
            ResultEntity resultEntity = playService.moveChess(messageEntity.getUsername(),messageEntity.getMessage());
            if(-1 == resultEntity.getCode()){
                sendOnePerson(messageEntity,-1);
            }
            else if(-2 == resultEntity.getCode()) {
                messageEntity.setMessage(JsonUtil.toJson(resultEntity));
                sendOnePerson(messageEntity,-2);
            }else {
                messageEntity.setMessage(JsonUtil.toJson(resultEntity));
                messageEntity.setUsername("系统");
                sendToPlayRoom(messageEntity);
            }
        }else {
            sendMessageToAll(message);
        }
    }

    @OnClose
    public void onClose(Session session){
        onlineMap.remove(this.username);
        sendMessageToAll(JsonUtil.toJson(new MessageEntity("Leave",username,username + "leave the room",onlineMap.size(),"大厅")));
    }

    @OnError
    public void onError(Session session,Throwable e){
       e.printStackTrace();
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

    private void sendToPlayRoom(MessageEntity messageEntity){
        String people = redisTemplate.opsForValue().get(messageEntity.getHostName() + "host");
        String[] member = people.split(",");
        for (String username : member){
            try {
                onlineMap.get(username).getBasicRemote().sendText(
                        JsonUtil.toJson(messageEntity)
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendOnePerson(MessageEntity messageEntity,int code){
        Session session = onlineMap.get(messageEntity.getUsername());
        if(code == -1) {
            messageEntity.setMessage(JsonUtil.toJson(ResultEntity.error(-1, "暂时未轮到您")));
        }
        try {
            session.getBasicRemote().sendText(
                    JsonUtil.toJson(messageEntity)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
