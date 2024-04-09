package org.example.Websockets;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.catalina.User;
import org.example.Addclasses.Room;
import org.example.Addclasses.UserRec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerEndpoint("/chat")
public class Chatendpoint {
    private static final Map<String, Session> sessions=new HashMap<>();// sessionid and session
    private static final Map<String, String> roommap=new HashMap<>();// sessionid and roomname
    private static final List<String> room=new ArrayList<>();//roonnames
    private static final List<UserRec> userList = new ArrayList<>();//for usernames with its sesions
    private static int onlinememb=0;
    private void sendmessage(String message, Session session, Boolean close){
        String temproomname="";
        for (int k=0; k<userList.size(); k++){
            if(session==userList.get(k).getSession()){
                temproomname=userList.get(k).getRoomname();
                if(close){
                    userList.remove(userList.get(k));
                }
            }
        }
        List<UserRec> roomusers=new ArrayList<>();
        for(int i=0; i<userList.size(); i++){
            if(userList.get(i).getRoomname().equals(temproomname)){
                roomusers.add(userList.get(i));
            }
        }
        roomusers.forEach(item->{
            try {
                item.getSession().getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
//        sessions.forEach((key, value) ->{
//            try {
//                value.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }
    @OnOpen
    public void onOpen(Session session){
    }
    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println(message);
        String[] temp = message.split(":");
        UserRec userRec = new UserRec();
        if(temp[0].equalsIgnoreCase("nameandroomname")){
            String[] temp2=temp[1].split("\n");
            userRec.setName(temp2[0]);
            userRec.setRoomname(temp2[1]);
            userRec.setSession(session);
            userList.add(userRec);
            int roommembers=0;
            for(int i=0; i<userList.size(); i++){
                if(userRec.getRoomname().equals(userList.get(i).getRoomname())){
                    roommembers++;
                }
            }
            sendmessage(userRec.getName()+" has joined the chat"+"\n"+roommembers,session,false);
        }else if(temp[0].equalsIgnoreCase("chatText")){
            String tempname="";
            for(int i=0; i<userList.size(); i++){
                if(userList.get(i).getSession()==session){
                    tempname=userList.get(i).getName();
                }
            }
            sendmessage(tempname+": "+temp[1], session,false);
        }
    }
    @OnClose
    public void onClose(Session session, CloseReason closeReason){
        String tempname="";
        String temproomname="";
        int roommembers=0;
        for(int i=0; i<userList.size(); i++){
            if(userList.get(i).getSession()==session){
                tempname=userList.get(i).getName();
                temproomname=userList.get(i).getRoomname();
            }
        }
        for(int i=0; i<userList.size(); i++){
            if(temproomname.equals(userList.get(i).getRoomname())){
                roommembers++;
            }
        }
        sendmessage(tempname+" has left the chat"+"\n"+(roommembers-1),session,true);
    }
}
