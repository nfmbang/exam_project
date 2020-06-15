/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cettia;

/**
 *
 * @author Martin
 */
import io.cettia.Server;
import io.cettia.ServerSocket;
import static io.cettia.ServerSocketPredicates.tag;
import io.cettia.asity.action.Action;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * An example action to handle {@link Server}.
 */
public class Chat implements Action<Server> {

    @Override
    public void on(Server server) {
        server.onsocket((ServerSocket socket) -> {
            // On the receipt of a transport, the server creates a socket with a NULL state
            System.out.println(socket + " is created");

            Action<Void> logState = v -> System.out.println(socket + " transitions to " + socket.state());
            // If it performs the handshake successfully, or the connection is recovered by the client reconnection
            socket.onopen(logState);
            // If it fails to perform the handshake, or the connection is disconnected for some reason
            socket.onclose(logState);
            // After one minute has elapsed since disconnection
            socket.ondelete(logState);

            // Sets a username
            socket.set("username", findParam(socket.uri(), "username"));
            // Joins the lounge channel where everyone gets together
            socket.tag("channel:lounge");

            // If a message is given, broadcasts it to everyone in the lounge channel
            socket.on("message", (Map<String, Object> input) -> {
                String text = (String) input.get("text");

                Map<String, Object> output = new LinkedHashMap<>();
                output.put("sender", socket.get("username"));
                output.put("text", text);

                System.out.println(socket.get("username") + "@" + socket.id() + " sends '" + text + "' to the lounge");
                server.find(tag("channel:lounge")).send("message", output);

                // If you prefer to deal with each socket directly,
                // server.find(s -> s.tags().contains("channel:lounge")).execute(s -> s.send("message", output));
            });

            // If you want to rewrite the above event handler with POJOs
            // socket.on("message", raw -> {
            //   ChatInputMessage in = new ObjectMapper().convertValue(raw, ChatInputMessage.class);
            //   ChatOutputMessage out = new ChatOutputMessage(socket.get("username"), in.getText());
            //   server.find(tag("channel:lounge")).send("message", out);
            // });
        });
    }

    private String findParam(String uri, String key) {
        String value = null;
        String regex = "(?:^|.*&)" + key + "=([^&]+).*";
        String query = java.net.URI.create(uri).getQuery();
        if (query.matches(regex)) {
            value = query.replaceAll(regex, "$1");
        }

        return value;
    }
}
