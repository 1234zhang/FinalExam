package fianlexam.demo.util;

import fianlexam.demo.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Brandon.
 * @date 2019/6/1.
 * @time 0:01.
 */

@Slf4j
@ControllerAdvice
@ResponseBody
public class ExceptionUtil{
    @ExceptionHandler
    public ResultEntity getNull(NullPointerException e) throws Exception{
        log.error("find a null error : " + e.getMessage());
        return ResultEntity.error(-1,null);
    }
    @ExceptionHandler
    public ResultEntity getError(Exception e) {
        //log.error("find a unknown error : " + e.getMessage());
        e.printStackTrace();
        return ResultEntity.error(-99,"未知错误");
    }
}
