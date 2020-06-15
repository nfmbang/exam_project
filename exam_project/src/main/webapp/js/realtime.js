/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

const addMessage2 = ({sender, text}) => {
    const $messages = document.getElementById("stream");
    const $li = document.createElement("li");
    $li.classList.add("message");
    $li.textContent = text;
    $messages.appendChild($li);
    $li.scrollIntoView();
}

const uri2 = `https://nbang.dk/CA1/cettia?channel=joke`;
const socket2 = cettia.open(uri2);
const addSystemMessage = text => addMessage2({sender: "system", text});

socket2.on("message", message => addMessage2(message));
socket2.on("connecting", () => addSystemMessage("The socket starts a connection."));
socket2.on("open", () => addSystemMessage("The socket establishes a connection."));
socket2.on("close", () => addSystemMessage("All transports failed to connect or the connection was disconnected."));
socket2.on("waiting", (delay) => addSystemMessage(`The socket will reconnect after ${delay} ms`));


