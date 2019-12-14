# Chat Room
Complete the chat room application implementation using WebSocket.

## Background
WebSocket is a communication protocol that makes it possible to establish a two-way communication channel between a
server and a client.

## Description
### Message model
Message model is the message payload that will be exchanged between the client and the server. Message model cover all the three basic actions.
1. ENTER
2. CHAT
3. LEAVE

## WebSocketChatServer

WebSocketChatServer covers the below scenarios for now

1. When a new user joins the chat all the existing users are notified with ENTER message type
2.  When a user leaves the chat all the existing users are notified with LEAVE message type
3.  When a user sends a text message in the chat all the existing users are notified with CHAT message type


### Run the application with command
mvn clean install
mvn spring-boot:run

### Access the chat window

http://localhost:8080/
