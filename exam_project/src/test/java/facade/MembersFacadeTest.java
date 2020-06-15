package facade;

import Facades.MembersFacade;
import Entities.Members;
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
public class MembersFacadeTest {

    private static EntityManagerFactory emf;
    private static MembersFacade facade;
    private static Members A, B, C;

    public MembersFacadeTest() {
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
        facade = MembersFacade.getMembersFacade(emf);

//Setup variables
        A = new Members("cph-nb168", "Niels B", "Rød");
        B = new Members("cph-mn521", "Martin W", "Rød");
        C = new Members("cph-jh409", "Jonatan H", "Gul-Rød");
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
            em.createNamedQuery("Members.deleteAllRows").executeUpdate();

            em.persist(A);
            em.persist(B);
            em.persist(C);

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
    public void testGetAllMembers() {
        List<Members> members = facade.getAll();
        assertThat(members, containsInAnyOrder(A, B, C));
    }

}
