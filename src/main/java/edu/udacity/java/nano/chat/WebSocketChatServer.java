package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */


@Component
@ServerEndpoint("/chat")
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();



    private  void sendMessageToAll(Message msg) {

        for(String key:onlineSessions.keySet()){
            Session session=onlineSessions.get(key);
            sendMessage(session,msg);
        }

    }


    private void sendMessage(Session session,Message msg)
    {
        if(session.isOpen()){
            try {
                session.getBasicRemote().sendText(JSON.toJSON(msg).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session) {

          onlineSessions.put(session.getId(),session);
          Message msg=new Message(null,null,"ENTER",onlineSessions.size());
          sendMessageToAll(msg);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session,  String jsonStr) {
        Message message = JSON.parseObject(jsonStr, Message.class);
        message.setType("SPEAK");
        message.setOnlineCount(onlineSessions.size());
        sendMessageToAll(message);
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        onlineSessions.remove(session.getId());
        Message msg=new Message(null,null,"LEAVE",onlineSessions.size());
        sendMessageToAll(msg);
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
