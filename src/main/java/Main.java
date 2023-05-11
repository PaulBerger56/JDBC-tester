import java.net.URL;
import java.util.ArrayList;
import java.sql.*;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        ArrayList<Person> currentSavedPeople = getCurrentTableData();
        ArrayList<Person> people = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        String choices = "Please Choose one of the following options\n" +
                "1. Print the Current Saved People\n" +
                "2. Add People to the Database\n" +
                "3. Clear the Database\n" +
                "4. Print the Choices Again\n" +
                "5. Quit the program";
        System.out.println(choices);

        while(true) {
            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    printTable();
                    break;
                case "2":
                    addPeopleToList(scanner);
                    break;
                case "3":
                    clearTable(scanner, currentSavedPeople);
                    break;
                case "4":
                    System.out.println(choices);;
                    break;
                case "5":
                    System.out.println("Thank you for using this program. \n" +
                            "Quitting......");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Sorry, that was an incorrect input. Please make another selection.");
            }
        }
    }

    public static void addPeopleToList(Scanner scanner) {
        String tempFirstName;
        String tempLastName;
        String tempEmail;
        ArrayList<Person> tempList = new ArrayList<>();

        while (true) {
            System.out.println();
            System.out.println("Press enter to enter a person or type 1 to quit");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                break;
            }
            System.out.print("First Name: ");
            tempFirstName = scanner.nextLine();

            System.out.print("Last Name: ");
            tempLastName = scanner.nextLine();

            System.out.print("Email: ");
            tempEmail = scanner.nextLine();

            tempList.add(new Person(tempFirstName, tempLastName, tempEmail));
        }

        while (true) {
            System.out.println("Do you want to add these people to the database? Y/N");
            String yOrN = scanner.nextLine();
            if (!yOrN.equalsIgnoreCase("y") && !yOrN.equalsIgnoreCase("n")) {
                System.out.println("invalid choice");
                continue;
            } else if (yOrN.equalsIgnoreCase("n")) {
                System.out.println("People Have Not Been Added to Database");
                tempList.clear();
                break;
            } else if (yOrN.equalsIgnoreCase("y")) {
                addToDatabase(tempList);
                break;
            }
        }

    }


    public static void addToDatabase(ArrayList<Person> people) {

        System.out.println("Adding People to Database");

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(LoginDetails.URL, LoginDetails.USER, LoginDetails.PASSWORD);

            for (Person p : people) {
                String query = "insert into person (first_name, last_name, email, id)" +
                        "values ('" + p.getFirstName() + "', '" + p.getLastName() + "', '" + p.getEmail() + "', '" + p.getId() + "')";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.execute();
            }

            conn.close();

        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    public static void printTable() {
        ArrayList<Person> printable = getCurrentTableData();
        System.out.println("People currently on the list: ");
        for (Person p : printable) {
            System.out.println(p.toString());
        }
    }

    public static ArrayList<Person> getCurrentTableData() {

        ArrayList<Person> tempInputList = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(LoginDetails.URL, LoginDetails.USER, LoginDetails.PASSWORD);

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM person");

            while (rs.next()) {
                String tempFirstName = rs.getString(1);
                String tempLastName = rs.getString(2);
                String tempEmail = rs.getString(3);
                UUID tempUUID = UUID.fromString(rs.getString(4));

                tempInputList.add(new Person(tempFirstName, tempLastName, tempEmail, tempUUID));
            }
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempInputList;
    }

    public static void clearTable(Scanner scanner, ArrayList<Person> people) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(LoginDetails.URL, LoginDetails.USER, LoginDetails.PASSWORD);

            String query = "TRUNCATE table person";

            while(true) {
                System.out.println("Do you want to clear the entire table? Y/N");
                String yOrN = scanner.nextLine();

                if (!yOrN.equalsIgnoreCase("y") && !yOrN.equalsIgnoreCase("n")) {
                    System.out.println("invalid choice");
                    continue;
                } else if (yOrN.equalsIgnoreCase("n")) {
                    System.out.println("Clearing Table Aborted");
                    break;
                } else if(yOrN.equalsIgnoreCase("y")) {
                    System.out.println("If you are absolutely sure that you want to clear the table type 7");
                    String confirmation = scanner.nextLine();
                    if(confirmation.equals("7")) {
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.execute();
                        System.out.println("Table has been cleared");
                        break;
                    } else {
                        System.out.println("Clearing Table Aborted");
                        break;
                    }
                }
            }
            conn.close();
            people.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
