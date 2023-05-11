import java.util.UUID;

public class Person {

    private String firstName;
    private String lastName;
    private String email;
    private UUID id;

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.id = UUID.randomUUID();
    }

    public Person(String firstName, String lastName, String email, UUID id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ",  " + email + ",  " + id.toString();
    }
}
