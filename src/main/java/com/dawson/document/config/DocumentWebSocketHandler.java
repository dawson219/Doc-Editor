package com.dawson.document.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class DocumentWebSocketHandler extends TextWebSocketHandler {
	private final Map<String, List<WebSocketSession>> sessionsByDocumentId = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String documentId = getDocumentIdFromSession(session);
		sessionsByDocumentId.putIfAbsent(documentId, new ArrayList<>());
		sessionsByDocumentId.get(documentId).add(session);
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String documentId = getDocumentIdFromSession(session);
		for (WebSocketSession webSocketSession : sessionsByDocumentId.get(documentId)) {
			if (webSocketSession.isOpen() && !webSocketSession.equals(session)) {
				webSocketSession.sendMessage(new TextMessage(message.getPayload()));
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		String documentId = getDocumentIdFromSession(session);
		sessionsByDocumentId.get(documentId).remove(session);
	}

	private String getDocumentIdFromSession(WebSocketSession session) {
		String path = session.getUri().getPath();
		return path.split("/")[3];
	}
}