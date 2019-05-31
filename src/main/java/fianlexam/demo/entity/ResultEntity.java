package fianlexam.demo.entity;

import lombok.Data;

/**
 * @author Brandon.
 * @date 2019/5/31.
 * @time 23:56.
 */

@Data
public class ResultEntity<T> {
    private int code;
    private String message;
    private Object data;

    public static ResultEntity error(int code, String message){
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(code);
        resultEntity.setMessage(message);
        return resultEntity;
    }

    public static ResultEntity success(int code,String message){
        ResultEntity result = new ResultEntity();
        result.setMessage(message);
        result.setCode(1);
        return result;
    }

    public static ResultEntity success(int code, String message, Object data){
        ResultEntity result = new ResultEntity();
        result.setCode(1);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
