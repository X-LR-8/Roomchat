package org.example.Websockets;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.catalina.User;
import org.example.Addclasses.Room;
import org.example.Addclasses.UserRec;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerEndpoint("/chat")
public class Chatendpoint {
    private static final List<UserRec> userList = new ArrayList<>();//for usernames with its sesions
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
            int tempmaxmemb=Integer.parseInt(temp2[2]);
            userRec.setName(temp2[0]);
            userRec.setRoomname(temp2[1]);
            userRec.setSession(session);
            int roommembers=1;
            for(int i=0; i<userList.size(); i++){
                if(userRec.getRoomname().equals(userList.get(i).getRoomname())){
                    roommembers++;
                }
            }
            if(roommembers<=tempmaxmemb){
                userList.add(userRec);
                Instant instant = Instant.ofEpochMilli(Instant.now().toEpochMilli());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String date = formatter.format(instant.atZone(ZoneId.systemDefault()));
                sendmessage("["+date+"] "+userRec.getName()+" has joined the chat"+"\n"+roommembers,session,false);
            }else{
                try {
                    session.getBasicRemote().sendText("status404");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else if(temp[0].equalsIgnoreCase("chatText")){
            String tempname="";
            for(int i=0; i<userList.size(); i++){
                if(userList.get(i).getSession()==session){
                    tempname=userList.get(i).getName();
                }
            }
            Instant instant = Instant.ofEpochMilli(Instant.now().toEpochMilli());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String date = formatter.format(instant.atZone(ZoneId.systemDefault()));
            sendmessage("["+date+"] "+tempname+": "+temp[1], session,false);
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
        Instant instant = Instant.ofEpochMilli(Instant.now().toEpochMilli());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String date = formatter.format(instant.atZone(ZoneId.systemDefault()));
        sendmessage("["+date+"] "+tempname+" has left the chat"+"\n"+(roommembers-1),session,true);
    }
}
