package package_homework_01;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.*;

public class Main {
    static Connection conn;
    static int cntUniver = 0;
    static int cntHobby = 0;

    public static void main(String[] args) throws Exception {

        String url = "jdbc:mysql://localhost:3306/homework_01";
        String username = "root";
        String password = "rootadmin";

        try {
            conn = DriverManager.getConnection(url, username, password);

            createTable();
            while (true) {
                menu();
                switch (new Scanner(System.in).next()) {
                    case "1":
                        addHuman();
                        break;
                    case "2":
                        loadFromFile();
                        break;
                    case "3":
                        deleteHuman();
                        break;
                    case "4":
                        printAllHumanInfo();
                        break;
                    case "7":
                        return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    static void menu() {
        System.out.println(" ----------------------------------------- ");
        System.out.println(" |             Action menu               | ");
        System.out.println(" ----------------------------------------- ");
        System.out.println(" | 1. Add human to DB                    | ");
        System.out.println(" | 2. Load human's to DB from file       | ");
        System.out.println(" | 3. Delete human from DB               | ");
        System.out.println(" | 4. Show info about all human's        | ");
        System.out.println(" | 5. Show info about human at id        | ");
        System.out.println(" | 6. Change human info at id            | ");
        System.out.println(" | 7. Exit program                       | ");
        System.out.println(" ----------------------------------------- ");
        System.out.printf("   Your choice:  ");
    }

    static void createTable() throws Exception {

        String[] insTable2 = {"insert into univer(name) values ('Univer');",
                "insert into univer(name) values ('Lisotech');",
                "insert into univer(name) values ('Politech');",
                "insert into univer(name) values ('Medik');"};

        String[] insTable3 = {
                "insert into hobby(name) values ('Photograph');",
                "insert into hobby(name) values ('Music');",
                "insert into hobby(name) values ('Books');",
                "insert into hobby(name) values ('Sport');",
                "insert into hobby(name) values ('Modeling');"
        };

        String dropTable1 = "drop table if exists human;";
        String crTable1 = "create table human(id int primary key auto_increment, fname varchar(20), lname varchar(30), age int, city varchar(20), univer_id int, hobby_id int); ";

        String dropTable2 = "drop table if exists univer;";
        String crTable2 = "create table univer(id int primary key auto_increment, name varchar(20));";
        String altTable2 = "alter table human add foreign key (univer_id) references univer(id);";

        String dropTable3 = "drop table if exists hobby;";
        String crTable3 = "create table hobby(id int primary key auto_increment, name varchar(20));";
        String altTable3 = "alter table human add foreign key (hobby_id) references hobby(id);";

        Statement stmt = conn.createStatement();

        stmt.execute(dropTable1);
        stmt.execute(crTable1);

        stmt.execute(dropTable2);
        stmt.execute(crTable2);
        stmt.execute(altTable2);

        stmt.execute(dropTable3);
        stmt.execute(crTable3);
        stmt.execute(altTable3);
        stmt.close();

        for (int i = 0; i < insTable2.length; i++) {
            PreparedStatement preStm = conn.prepareStatement(insTable2[i]);
            preStm.executeUpdate();
            preStm.close();
        }
        cntUniver = insTable2.length;

        for (int i = 0; i < insTable3.length; i++) {
            PreparedStatement preStm = conn.prepareStatement(insTable3[i]);
            preStm.executeUpdate();
            preStm.close();
        }
        cntHobby = insTable3.length;
    }

    static void addHuman() throws SQLException {
        System.out.printf("Enter First Name");
        String fName = new Scanner(System.in).next();
        System.out.printf("Enter Last Name");
        String lName = new Scanner(System.in).next();
        System.out.printf("Enter Age");
        int age = new Scanner(System.in).nextInt();
        System.out.printf("Enter city");
        String city = new Scanner(System.in).next();

        String query = "INSERT INTO HUMAN(fname, lname, age, city, univer_id, hobby_id) values (?, ?, ?, ?, ?, ?);";

        PreparedStatement preStm = conn.prepareStatement(query);
        preStm.setString(1, fName);
        preStm.setString(2, lName);
        preStm.setInt(3, age);
        preStm.setString(4, city);
        preStm.setInt(5, new Random().nextInt(cntUniver));
        preStm.setInt(6, new Random().nextInt(cntHobby));

        preStm.executeUpdate();
        preStm.close();
    }

    static void loadFromFile() throws Exception {

        String query = "INSERT INTO HUMAN(fname, lname, age, city, univer_id, hobby_id) values (?, ?, ?, ?, ?, ?);";

        File f = new File("random.txt");
        if (f.exists()) {

            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            try {
                String line;
                String[] arr = new String[2];
                while ((line = br.readLine()) != null) {
                    int i = 0;
                    for (String temp : line.split(" ")) {
                        arr[i++] = temp;
                    }
                    PreparedStatement preStm = conn.prepareStatement(query);
                    preStm.setString(1, arr[0]);
                    preStm.setString(2, arr[1]);
                    preStm.setInt(3, new Random().nextInt(45) + 20);
                    preStm.setString(4, "Toronto");
                    preStm.setInt(5, new Random().nextInt(cntUniver) + 1);
                    preStm.setInt(6, new Random().nextInt(cntHobby) + 1);

                    preStm.executeUpdate();
                    preStm.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fr.close();
                br.close();
            }

        } else {
            System.out.println("File not found....");
            return;
        }
    }

    static void deleteHuman() throws Exception {
        String query = "SELECT ID, FNAME, LNAME FROM HUMAN";
        List<String> list = new ArrayList<>();
        PreparedStatement preStm = conn.prepareStatement(query);
        ResultSet res = preStm.executeQuery();
        while (res.next()) {
            list.add(
                    res.getInt("id") + "\t| "
                            + "First Name " + res.getString("fname") + "\t| "
                            + "Last Name " + res.getString("lname"));
        }
        preStm.close();

        list.forEach(System.out::println);

        System.out.println();
        System.out.printf("Enter human id to remove database ");
        int delID = new Scanner(System.in).nextInt();
        String delQuery = "DELETE FROM HUMAN WHERE id = ?;";

        PreparedStatement prStm = conn.prepareStatement(delQuery);
        prStm.setInt(1, delID);
        prStm.executeUpdate();
    }

    static void printAllHumanInfo() throws Exception {

        String query = "SELECT * FROM human h JOIN univer u ON u.id = h.univer_id JOIN hobby ho ON ho.id = h.hobby_id;";

    }
}
