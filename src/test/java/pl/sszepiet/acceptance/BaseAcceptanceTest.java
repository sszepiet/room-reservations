package pl.sszepiet.acceptance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import pl.sszepiet.customer.Customer;
import pl.sszepiet.room.Room;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseAcceptanceTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    EntityManager entityManager;

    static String readResourceAsString(String resourceName) {
        return new String(readFileAsByteArray(resourceName), StandardCharsets.UTF_8);
    }

    private static byte[] readFileAsByteArray(String resourceName) {
        try {
            return Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(resourceName).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Value("${local.server.port}")
    private int port;

    String getUrl() {
        return getBaseUrl() + getPath();
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    abstract String getPath();

    Customer getExistingCustomer() {
        return getCustomers().get(0);
    }

    List<Customer> getCustomers() {
        TypedQuery<Customer> query = entityManager.createQuery("select c from Customer c", Customer.class);
        return query.getResultList();
    }

    List<Room> getRooms() {
        TypedQuery<Room> query = entityManager.createQuery("select distinct r from Room r left join r.reservations res", Room.class);
        return query.getResultList();
    }
}
