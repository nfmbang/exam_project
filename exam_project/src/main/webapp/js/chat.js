/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// With https://babeljs.io/repl, you can make the following code compatible with ES5
document.querySelector("#index form").addEventListener("submit", e => {
    e.preventDefault();


    // Assumes the user is logged with the username
    const $username = e.target.elements.username;
    const username = $username.value || $username.placeholder;

    // Replaces #index with #chat
    document.querySelector("#index").classList.add("inactive");
    document.querySelector("#chat").classList.remove("inactive");

    let oldSender = "system";
    const addMessage = ({sender, text}) => {
        const $messages = document.getElementById("chat");
        if (oldSender !== sender) {
            const $li = document.createElement("li");
            $li.classList.add("sender");
            $li.textContent = sender;
            $messages.appendChild($li);
            oldSender = sender;
        }

        const $li = document.createElement("li");
        $li.classList.add("message");
        $li.textContent = text;
        $messages.appendChild($li);
        $li.scrollIntoView();
    };

    // Opens and initializes a socket
    const uri = `https://nbang.dk/CA1/cettia?channel=lounge&username=${encodeURIComponent(username)}`;
    const socket = cettia.open(uri);
    const addSystemMessage = text => addMessage({sender: "system", text});
    const showEditor = ()=>{
        document.getElementById("editor").style.display = "block";
    }
    socket.on("open", () => showEditor());
    socket.on("close", () => addSystemMessage("All transports failed to connect or the connection was disconnected."));
    socket.on("waiting", (delay) => addSystemMessage(`The socket will reconnect after ${delay} ms`));
    socket.on("message", message => addMessage(message));

    // Configures the editor
    const $editor = document.querySelector("#editor > div[contenteditable]");
    window.addEventListener("keypress", e => {
        if (e.target !== $editor && e.key.trim() && e.key.length === 1) {
            e.preventDefault();
            $editor.innerText += e.key;

            const selection = window.getSelection();
            const range = document.createRange();
            selection.removeAllRanges();
            range.selectNodeContents($editor);
            range.collapse(false);
            selection.addRange(range);
            $editor.focus();
        }
    }, false);
    $editor.addEventListener("keypress", e => {
        if (!e.shiftKey && e.code === "Enter") {
            e.preventDefault();

            const text = $editor.innerText;
            if (text) {
                socket.send("message", {text});
                $editor.innerHTML = "";
            }
        }
    }, false);
}, false);