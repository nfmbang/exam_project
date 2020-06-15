package facade;

import Entities.Car;
import Facades.CarFacade;
import dto.CarDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.EMF_Creator;
import util.EMF_Creator.DbSelector;
import util.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class CarFacadeTest {

    private static EntityManagerFactory emf;
    private static CarFacade facade;
    private static Car c1, c2, c3;

    public CarFacadeTest() {
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
        facade = CarFacade.getFacade(emf);

//Setup variables
        c1 = new Car(2000, "VW", "Golf", 10000, "Angela Merkel", "am11111");
        c2 = new Car(2008, "Ford", "Ka", 15000, "Donald Trump", "dt22222");
        c3 = new Car(2017, "Audi", "RS7", 800000, "Kim Jung Un", "kj33333");
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
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

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testGetAllCars() {
        List<Car> cars = facade.getAllCars();

        assertThat(cars, containsInAnyOrder(c1, c2, c3));

    }

}
