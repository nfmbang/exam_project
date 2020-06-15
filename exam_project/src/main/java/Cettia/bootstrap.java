/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cettia;

import Facades.JokeFacade;
import io.cettia.DefaultServer;
import io.cettia.Server;
import static io.cettia.ServerSocketPredicates.tag;
import io.cettia.asity.action.Action;
import io.cettia.asity.bridge.jwa1.AsityServerEndpoint;
import io.cettia.asity.bridge.servlet3.AsityServlet;
import io.cettia.transport.http.HttpTransportServer;
import io.cettia.transport.websocket.WebSocketTransportServer;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

@WebListener
public class bootstrap implements ServletContextListener {

    private static Server instance;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        Server server = new DefaultServer();
        instance = server;

        server.onsocket(socket -> {

            System.out.println(socket + " is created");

            Action<Void> logState = v -> System.out.println(socket + " transitions to " + socket.state());
            // If it performs the handshake successfully, or the connection is recovered by the client reconnection
            socket.onopen(logState);
            // If it fails to perform the handshake, or the connection is disconnected for some reason
            socket.onclose(logState);
            // After one minute has elapsed since disconnection
            socket.ondelete(logState);

            socket.tag("channel:" + findParam(socket.uri(), "channel"));

            // Sets a username
            if (findParam(socket.uri(), "channel").equals("lobby")) {
                socket.set("username", findParam(socket.uri(), "username"));
            }
            // Joins the lounge channel where everyone gets together
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
        });

        HttpTransportServer httpTransportServer = new HttpTransportServer().ontransport(server);
        // Servlet
        ServletContext context = event.getServletContext();
        Servlet servlet = new AsityServlet().onhttp(httpTransportServer);
        ServletRegistration.Dynamic reg = context.addServlet(AsityServlet.class.getName(), servlet);
        reg.setAsyncSupported(true);
        reg.addMapping("/cettia");

        WebSocketTransportServer wsTransportServer = new WebSocketTransportServer().ontransport(server);
        // Java WebSocket API
        ServerContainer container = (ServerContainer) context.getAttribute(ServerContainer.class
                .getName());
        ServerEndpointConfig config = ServerEndpointConfig.Builder.create(AsityServerEndpoint.class,
                "/cettia")
                .configurator(new Configurator() {
                    @Override
                    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
                        return endpointClass.cast(new AsityServerEndpoint().onwebsocket(wsTransportServer));
                    }
                })
                .build();
        try {
            container.addEndpoint(config);
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
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

    public static Server getServer() {
        return instance;
    }
;
}
