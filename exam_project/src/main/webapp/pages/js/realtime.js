/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

const addMessage = ({sender, text}) => {
    const $messages = document.querySelector("div.messages-container > ul");
    const $li = document.createElement("li");
    $li.classList.add("message");
    $li.textContent = text;
    $messages.appendChild($li);
    $li.scrollIntoView();
}

const uri = `http://localhost:8080/CA1/cettia?channel=joke`;
const socket = cettia.open(uri);
const addSystemMessage = text => addMessage({sender: "system", text});

socket.on("message", message => addMessage(message));
socket.on("connecting", () => addSystemMessage("The socket starts a connection."));
socket.on("open", () => addSystemMessage("The socket establishes a connection."));
socket.on("close", () => addSystemMessage("All transports failed to connect or the connection was disconnected."));
socket.on("waiting", (delay) => addSystemMessage(`The socket will reconnect after ${delay} ms`));


