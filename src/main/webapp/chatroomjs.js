async function changeusername(){
    document.querySelector(".popup").style.display = "flex";
}
var mainroomname;
window.onload=async function(){
    var urlparams = new URLSearchParams(window.location.search);
    var modifiedname = urlparams.get('roomname');
    var fixedname = decodeURIComponent(modifiedname);
    var roomname=document.getElementById("roomname");
    roomname.textContent=fixedname;
    mainroomname=roomname;
    // webSocket.send("roomname:"+fixedname);
}
async function leaveroom(){
    window.close();
    window.open("index.html");
}
// usage: addUser and sendMessage
const webSocket =new WebSocket('ws://localhost:8989/roomchat/chat');
async function adduser(){
    var name=document.getElementById("username").value;
    document.querySelector(".popup").style.display = "none";
    webSocket.send("nameandroomname:"+name+"\n"+mainroomname);
    // webSocket.send("username:"+name);
}
function sendMessage(){
    const textIn = document.getElementById("messageinput").value;
    webSocket.send("chatText:"+textIn);
}
webSocket.onmessage = function (event) {
    console.log(event.data+" es aris swori funqciaaaa")
};