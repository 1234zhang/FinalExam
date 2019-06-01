package fianlexam.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Brandon.
 * @date 2019/6/1.
 * @time 2:22.
 */

@Configuration
@Slf4j
public class WebsocketConfig{
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        log.info("the serverEndpointExporter be created");
        return new ServerEndpointExporter();
    }
}
