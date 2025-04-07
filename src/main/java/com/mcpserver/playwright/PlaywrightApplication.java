package com.mcpserver.playwright;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class PlaywrightApplication {

		public static void main(String[] args) {
		SpringApplication.run(PlaywrightApplication.class, args);
		}

//		@Bean
//		public List<ToolCallback> registerTools (PlayWrightService playwrightService){
//		return List.of(ToolCallbacks.from(playwrightService));
//	}

}
