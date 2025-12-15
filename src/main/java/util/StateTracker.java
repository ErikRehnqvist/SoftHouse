package util;

import model.Family;
import model.Person;

public class StateTracker {
    private Person person;
    private Family family;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }
}
