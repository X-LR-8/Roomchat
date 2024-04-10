async function changeusername(){
    document.querySelector(".popup").style.display = "flex";
}
const webSocket =new WebSocket('ws://localhost:8989/roomchat/chat');
var mainroomname;
var fixedmaxmemb;
window.onload=async function(){
    var urlparams = new URLSearchParams(window.location.search);
    var modifiedname = urlparams.get('roomname');
    var modifiedmaxmemb=urlparams.get('maxmemb')
    var fixedname = decodeURIComponent(modifiedname);
    fixedmaxmemb=decodeURIComponent(modifiedmaxmemb);
    var roomname=document.getElementById("roomname");
    roomname.textContent=fixedname;
    mainroomname=roomname.textContent;
}
async function leaveroom(){
    window.close();
    window.open("index.html");
}
// usage: addUser and sendMessage

async function adduser(){
    var name=document.getElementById("username").value;
    document.querySelector(".popup").style.display = "none";
    var username=document.getElementById("name");
    username.textContent=name;
    webSocket.send("nameandroomname:"+name+"\n"+mainroomname+"\n"+fixedmaxmemb);
    // webSocket.send("username:"+name);
}
function sendMessage(){
    const textIn = document.getElementById("messageinput").value;
    webSocket.send("chatText:"+textIn);
}
webSocket.onmessage = function (event) {
    var receiveddata=event.data;
    var split=event.data.split("\n");
    var mainchat=document.getElementById("mainchat");
    var onlinememb=document.getElementById("onlinememb");
    if(split[1]!=null){
        if(split[0].includes("has joined the chat") || split[0].includes("has left the chat")){
            var servermessagelabel=document.createElement("label");
            servermessagelabel.textContent=split[0];
            if(split[0].includes("has joined the chat")){
                onlinememb.textContent="Members online: "+split[1];
                servermessagelabel.style.color="green";
            }else if(split[0].includes("has left the chat")){
                onlinememb.textContent="Members online: "+split[1];
                servermessagelabel.style.color="red";
            }
            mainchat.appendChild(servermessagelabel);
        }
    }else{
        var textmessagelabel=document.createElement("label");
        console.log(receiveddata);
        textmessagelabel.textContent=receiveddata;
        textmessagelabel.style.color="blue";
        mainchat.appendChild(textmessagelabel);
    }
    if(event.data.includes("status404")){
        alert("Room is full!");
    }
    console.log(event.data+" javascriptidanonmessage")
};