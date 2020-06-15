/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package REST;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Facades.MembersFacade;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import util.EMF_Creator;

/**
 * REST Web Service
 *
 * @author Niels
 */
@Path("groupmembers")
public class MembersResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/EXAM",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);
    private static final MembersFacade FACADE = MembersFacade.getMembersFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Creates a new instance of GroupMembersResource
     */
    public MembersResource() {
    }

    /**
     * Retrieves representation of an instance of REST.MembersResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of MembersResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    /**
     * Get method for accessing a list of members in the database.
     *
     * @return A list of all members in the database.
     */
    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAll() {
        return GSON.toJson(FACADE.getAll());
    }

    /**
     * Get method for getting a single member by student Id.
     *
     * @param sId
     * @return the requested member.
     */
    @Path("{sId}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getMemberBysId(@PathParam("sId") String sId) {
        return GSON.toJson(FACADE.getMemberBysId(sId));
    }

    /**
     * Populates the database.
     */
    @Path("populate")
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    public void populate() {
        FACADE.populate();
    }

    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getMembersCount() {
        return GSON.toJson(FACADE.getMembersCount());
    }
}
