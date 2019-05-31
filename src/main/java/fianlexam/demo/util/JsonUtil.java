package fianlexam.demo.util;

import com.google.gson.Gson;
import fianlexam.demo.entity.MessageEntity;

/**
 * @author Brandon.
 * @date 2019/6/1.
 * @time 2:32.
 */

public class JsonUtil {
    private static Gson gson = new Gson();

    public static String toJson(MessageEntity messageEntity){
        return gson.toJson(messageEntity);
    }

    public static MessageEntity fromJson(String messageJson){
        return gson.fromJson(messageJson,MessageEntity.class);
    }
}
