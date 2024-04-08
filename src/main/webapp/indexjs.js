async function createroom() {
    var roomname=document.getElementById("roomname").value;
    var maxmemb=document.getElementById("maxnum").value;
    console.log(name+" "+maxmemb);
    var url="/roomchat"+"/room"+`?roomname=${roomname}&maxmemb=${maxmemb}`;
    var response=await fetch(url,{method:"POST"});
    // location.reload();
}

async function openpopup() {
    document.querySelector(".popup").style.display = "flex";
}

window.onload = async function () {
    console.log("asdfasdf");
    var url = "/roomchat" + "/room";
    var response = await fetch(url, {method: "GET"});
    var body = await response.text();
    console.log(body);
    var rooms = JSON.parse(body);
    createfieldsets(rooms);
}

async function createfieldsets(rooms) {
    const roomdiv = document.getElementById("roomdiv");
    rooms.forEach(item => {

        const fieldset = document.createElement("fieldset");
        fieldset.classList.add("forfieldset");

        const chatname = document.createElement("label");
        chatname.textContent = item.name;
        fieldset.appendChild(chatname);

        const members = document.createElement("label");
        members.textContent = "/"+item.maxmemb;
        members.style.position = "absolute";
        members.style.right = "5px";
        fieldset.appendChild(members);
        fieldset.onclick=function (){
            var modifiedname=encodeURIComponent(item.name)
            window.open("chatroom.html?roomname="+modifiedname);
        }
        roomdiv.appendChild(fieldset);
    });
}