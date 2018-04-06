package package_01_lesson;

import java.sql.*;
import java.util.*;

public class Main {
    static Connection conn;
    public static void main(String[] args) throws Exception {

        String url = "jdbc:mysql://localhost:3306/universety";
        String username = "root";
        String password = "rootadmin";

        try  {
            Connection connection = conn = DriverManager.getConnection(url, username, password);
            createTable();
            for (int i = 0; i < 50; i++) {
                addStudent(i);
            }
            selectStudent(33);
            deleteStudent(15);
            updateStudent(5, "Jack Daniel");
            createNewTable();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    static void updateStudent(int i, String name) throws Exception {
        String query = "update student set fullname = ? where id = ?;";
        PreparedStatement preStat = conn.prepareStatement(query);
        preStat.setString(1, name);
        preStat.setInt(2, i);
        preStat.executeUpdate();
        preStat.close();
    }


    static void createNewTable() throws Exception {
        String dropQuery = "drop table if exists univer;";
        String createQuery = "create table univer (id int primary key auto_increment, uname varchar(30));";
        String alterQuery = "alter table student add foreign key(univer) references univer(id);";
        Statement stat = conn.createStatement();
        stat.execute(dropQuery);
        stat.execute(createQuery);
        stat.execute(alterQuery);
        stat.close();
    }

    static void createTable() throws Exception {
        String dropQuery = "drop table if exists student;";

        String query = "create table student("
                + "id int primary key auto_increment,"
                + "fullname varchar(30),"
                + "age int, "
                + "univer int"
                + ");";
        Statement stm = conn.createStatement();
        stm.execute(dropQuery);
        stm.execute(query);
        stm.close();
    }

    static void addStudent(int i) throws Exception {
        String query = "insert into student (fullname,age) values (?, ?);";
        PreparedStatement preStm = conn.prepareStatement(query);
        preStm.setString(1, "Jokhn One");
        preStm.setInt(2, 20 + i);
        preStm.executeUpdate();
        preStm.close();
    }

    static void deleteStudent(int i) throws Exception {
        String query = "delete from student where id = ?;";
        PreparedStatement preStat = conn.prepareStatement(query);
        preStat.setInt(1, i);
        preStat.executeUpdate();
        preStat.close();
    }

    static void selectStudent(int i) throws Exception {
        String query = "select * from student where id = ?;";
        PreparedStatement preStm = conn.prepareStatement(query);
        preStm.setInt(1, i);
        ResultSet res = preStm.executeQuery();
        List<String> students = new ArrayList();

        while (res.next()) {
            students.add(
                    "id " + res.getInt("id")
                            + "\t|" + "Full Name " + res.getString("fullname") + "\t|" + "Age " + res.getInt("age")
            );
        }
        preStm.close();
        students.forEach(System.out::println);
    }
}

