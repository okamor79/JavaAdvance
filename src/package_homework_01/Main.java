package package_homework_01;

import java.sql.*;
import java.util.*;

public class Main {
    static Connection conn;
    int cntUniver = 0;
    int cntHobby = 0;

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

        String dropTable1 = "drop table if exists human;";
        String crTable1 = "create table human(id int primary key auto_increment, fname varchar(20), lname varchar(30), age int, city varchar(20), univer_id int, hobby_id int); ";

        String dropTable2 = "drop table if exists univer;";
        String crTable2 = "create table univer(id int primary key auto_increment, name varchar(20));";

        String dropTable3 = "drop table if exists hobby;";


        Statement stmt = conn.createStatement();

        stmt.execute(dropTable1);
        stmt.execute(crTable1);

        stmt.execute(dropTable2);
        stmt.execute(crTable2);
        
        stmt.execute(dropTable3);
    //    stmt.execute(crTable);
        stmt.close();
    }
}
