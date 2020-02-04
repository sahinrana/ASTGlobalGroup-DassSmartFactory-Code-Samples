package com.dasssmart.websocket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.dasssmart.core.DassUtils;
import com.dasssmart.domain.JobSch;
import com.google.gson.Gson;

@Configuration
@EnableWebSocket
@EnableScheduling
public class WebsocketConfig implements WebSocketConfigurer {

	private static Logger logger = LoggerFactory.getLogger(WebsocketConfig.class);

	@Value("${websocket.maxTextMessageBufferSize}")
	int maxTextMessageBufferSize;

	@Value("${websocket.maxBinaryMessageBufferSize}")
	int maxBinaryMessageBufferSize;

	@Value("${websocket.maxSessionIdleTimeout}")
	int maxSessionIdleTimeout;

	@Value("${websocket.maxfixedWebsocketConfigThreadPool}")
	int maxfixedThreadPool;

	@Value("${config.oauth2.privateKey}")
	private String privateKey;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

		registry.addHandler(couponWebSocketTransactionsHandler(), "/websocket/machine/status").setAllowedOrigins("*") .withSockJS();
		registry.addHandler(stopReasonAlertWebSocketTransactionsHandler(), "/websocket/stopreason/alert")
				.setAllowedOrigins("*").withSockJS();

	}

	@Bean("DassSmartWebSocketTransactionsHandler")
	public DassSmartWebSocketTransactionsHandler couponWebSocketTransactionsHandler() {
		return new DassSmartWebSocketTransactionsHandler();
	}

	@Bean("StopReasonAlertWebSocketTransactionsHandler")
	public StopReasonAlertWebSocketTransactionsHandler stopReasonAlertWebSocketTransactionsHandler() {
		return new StopReasonAlertWebSocketTransactionsHandler();
	}

	 

	public static class DassSmartWebSocketTransactionsHandler extends TextWebSocketHandler {
		private ExecutorService multiThreadExecutor = Executors.newFixedThreadPool(100);

		public DassSmartWebSocketTransactionsHandler() {

		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {

			String urlGETList = "http://localhost:6199/workstation/WorkStationsWebSocket";

			RestTemplate restTemplate = new RestTemplate();

			try {
				ResponseEntity<List<JobSch>> rateResponse = restTemplate.exchange(urlGETList, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<JobSch>>() {
						});
				List<JobSch> JobSches = rateResponse.getBody();
				for (JobSch jobSch : JobSches) {

					try {
//						multiThreadExecutor.submit(() -> {
							try {
								
								System.out.println("GIDEN:"+new Gson().toJson(jobSch));
								session.sendMessage(new TextMessage(new Gson().toJson(jobSch)));
							} catch (IOException e) {
								 
								e.printStackTrace();
							}
//						});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				System.out.println(new Gson().toJson(JobSches));

				DassUtils.machineUserSessionList.add(session);

			} catch (Exception e) {
				logger.error("error {}", e);
			}

		}

		@Override
		public void handleTextMessage(WebSocketSession session, TextMessage message)
				throws IOException, InterruptedException {

		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
			if (session != null) {
				DassUtils.machineUserSessionList.remove(session);

			}
		}

		@Override
		public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
			if (session != null) {
				DassUtils.machineUserSessionList.remove(session);

			}

			super.handleTransportError(session, exception);
		}

	}

	public static class StopReasonAlertWebSocketTransactionsHandler extends TextWebSocketHandler {

		public StopReasonAlertWebSocketTransactionsHandler() {

		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			DassUtils.stopReasonUserSessionList.add(session);

		}

		@Override
		public void handleTextMessage(WebSocketSession session, TextMessage message)
				throws IOException, InterruptedException {

		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
			if (session != null) {
				DassUtils.stopReasonUserSessionList.remove(session);

			}
		}

		@Override
		public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
			if (session != null) {
				DassUtils.stopReasonUserSessionList.remove(session);

			}

			super.handleTransportError(session, exception);
		}

	}

}
