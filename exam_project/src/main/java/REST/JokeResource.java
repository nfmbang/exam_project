/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package REST;

import Entities.Joke;
import Facades.JokeFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import util.EMF_Creator;

/**
 * REST Web Service
 *
 * @author Martin
 */
@Path("joke")
public class JokeResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/CA1",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);
    private static final JokeFacade FACADE = JokeFacade.Get(EMF);

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private UriInfo context;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello() {
        Joke j = FACADE.getRandom();
        return GSON.toJson(j);
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public String all() {
        List<Joke> j = FACADE.getAll();
        return GSON.toJson(j);
    }
    @Path("/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getById(@PathParam("id") long id) {
        return GSON.toJson(FACADE.getById(id));
    }
    
    @GET
    @Path("pop")
    public void populate() {
        FACADE.populate();
    }

    @POST
    @Path("vote")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String vote(@FormParam("param1") String joke,
                   @FormParam("param2") int score){
        Joke j = GSON.fromJson(joke, Joke.class);
        j.setScore(j.getScore()+score);
        j.setVotes(j.getVotes()+1);
        FACADE.update(j);
        return GSON.toJson(j);
    }

}
