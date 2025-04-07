package com.mcpserver.playwright;

import org.springframework.ai.autoconfigure.mcp.server.MpcWebMvcServerAutoConfiguration;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfiguration {


    @Bean
    public ToolCallbackProvider playWrightTools(PlayWrightService playWrightService) {
        return MethodToolCallbackProvider.builder().toolObjects(playWrightService).build();
    }
}
