package REST;

import Entities.Car;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.EMF_Creator;
import util.EMF_Creator.DbSelector;
import util.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class CarResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    //Read this line from a settings-file  since used several places
    private static final String TEST_DB = "jdbc:mysql://localhost:3307/CA1_test";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static Car c1, c2, c3;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //emf = Persistence.createEntityManagerFactory("pu");
        /* emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/CA1_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.DROP_AND_CREATE);
         */
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        httpServer = startServer();

        //Setup variables
        c1 = new Car(2000, "VW", "Golf", 10000, "Angela Merkel", "am11111");
        c2 = new Car(2008, "Ford", "Ka", 15000, "Donald Trump", "dt22222");
        c3 = new Car(2017, "Audi", "RS7", 800000, "Kim Jung Un", "kj33333");

        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;

        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {

        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Car.deleteAllRows").executeUpdate();

            em.persist(c1);
            em.persist(c2);
            em.persist(c3);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testAll() throws Exception {
        given().contentType(ContentType.JSON)
                .get("/car/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("make", containsInAnyOrder(c1.getMake(), c2.getMake(), c3.getMake()));
    }

}
