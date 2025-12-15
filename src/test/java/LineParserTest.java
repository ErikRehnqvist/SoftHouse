import model.Address;
import model.Family;
import model.Person;
import model.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.LineParser;
import util.StateTracker;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LineParserTest {

    private LineParser parser;
    private List<Person> people;
    private StateTracker stateTracker;

    @BeforeEach
    void setUp() {
        parser = new LineParser();
        people = new ArrayList<>();
        stateTracker = new StateTracker();
    }

    @Test
    void shouldCreatePersonAndUpdateStateTracker() {
        parser.parseLine("P|John|Smith", people, stateTracker);

        assertEquals(1, people.size());

        Person person = people.getFirst();
        assertEquals("John", person.getFirstName());
        assertEquals("Smith", person.getLastName());

        assertSame(person, stateTracker.getPerson());
        assertNull(stateTracker.getFamily());
    }

    @Test
    void shouldAddAddressToPersonWhenNoFamilyExists() {
        parser.parseLine("P|John|Smith", people, stateTracker);
        parser.parseLine("A|Sveavägen|Stockholm|12345", people, stateTracker);

        Person person = people.getFirst();
        Address address = person.getAddress();

        assertNotNull(address);
        assertEquals("Sveavägen", address.getStreet());
        assertEquals("Stockholm", address.getCity());
        assertEquals("12345", address.getPostCode());
    }

    @Test
    void shouldAddPhoneToPersonWhenNoFamilyExists() {
        parser.parseLine("P|John|Smith", people, stateTracker);
        parser.parseLine("T|07081234567|08123456", people, stateTracker);

        Phone phone = people.getFirst().getPhone();

        assertNotNull(phone);
        assertEquals("07081234567", phone.getCellPhone());
        assertEquals("08123456", phone.getPhone());
    }

    @Test
    void shouldCreateFamilyAndAttachToPerson() {
        parser.parseLine("P|John|Smith", people, stateTracker);
        parser.parseLine("F|SmithFamily|1980", people, stateTracker);

        Person person = people.getFirst();
        List<Family> families = person.getFamilies();

        assertEquals(1, families.size());

        Family family = families.getFirst();
        assertEquals("SmithFamily", family.getName());
        assertEquals("1980", family.getBirthYear());

        assertSame(family, stateTracker.getFamily());
    }

    @Test
    void shouldAddAddressToFamilyWhenFamilyExists() {
        parser.parseLine("P|John|Smith", people, stateTracker);
        parser.parseLine("F|SmithFamily|1980", people, stateTracker);
        parser.parseLine("A|Domkyrkovägen|Uppsala|54321", people, stateTracker);

        Family family = stateTracker.getFamily();
        Address address = family.getAddress();

        assertNotNull(address);
        assertEquals("Domkyrkovägen", address.getStreet());
        assertEquals("Uppsala", address.getCity());
        assertEquals("54321", address.getPostCode());
    }

    @Test
    void shouldThrowExceptionWhenFamilyWithoutPerson() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> parser.parseLine("F|SmithFamily|1980", people, stateTracker)
        );

        assertTrue(exception.getMessage().contains("Family without a belonging person"));
    }

    @Test
    void shouldThrowExceptionWhenAddressWithoutPersonOrFamily() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> parser.parseLine("A|Street|City|12345", people, stateTracker)
        );

        assertTrue(exception.getMessage().contains("Address without a belonging person"));
    }
}
