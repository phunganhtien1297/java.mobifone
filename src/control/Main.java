package control;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.beans.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Scanner;

import model.KhachHang;

public class Main {
    public Connection connection = null;
    public Statement statement, statement1;
    ResultSet resultSet, resultSet1;
    PreparedStatement stmt;

    String JDBC_DB_URL = "jdbc:oracle:thin:@";
    String JDBC_USER = "";
    String JDBC_PASS = "";

    public static void main(String[] args) {
        Main m = new Main();
    }

    public Main() {
        readFile_Scanner();
        getData();
    }

    private void readFile_Scanner() {
        String url = "input.txt";
        FileInputStream fileInputStream = null;
        Scanner scanner = null;

        try {
            // Đọc dữ liệu từ File với Scanner
            fileInputStream = new FileInputStream(url);
            scanner = new Scanner(fileInputStream);

            while (scanner.hasNextLine()) {
                // System.out.println(scanner.nextLine());
                ArrayList<KhachHang> arrayListKH = new ArrayList<KhachHang>();
                String line = scanner.nextLine();
                try {
                    String[] sub_tmp = line.split(";");
                    String id = sub_tmp[0];
                    String firstname = sub_tmp[1];
                    String lastname = sub_tmp[2];
                    String room = sub_tmp[3];
                    String valid = sub_tmp[4];

                    if (Integer.parseInt(id) > 0) {
                        KhachHang kh = new KhachHang(id, firstname, lastname, room, valid);
                        arrayListKH.add(kh);

                        inserttData(arrayListKH);
                    }
                } catch (Exception e) {
                    System.out.println("err: " + e.getMessage());
                }
            }
        } catch (Exception e) {

        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
                if (scanner != null)
                    scanner.close();
            } catch (IOException ex) {

            }
        }
    }

    /**
     * 
     * @param JDBC_DB_URL
     * @param JDBC_PASS
     * @param JDBC_USER
     */
    private void getConnection(String JDBC_DB_URL, String JDBC_PASS, String JDBC_USER) {
        try {
            if ((connection != null && connection.isClosed()) || connection == null) {
                Class.forName("oracle.jdbc.OracleDriver");
                connection = DriverManager.getConnection(JDBC_DB_URL, JDBC_USER, JDBC_PASS);
                System.out.println("Connect DB Success");
            }

        } catch (Exception ex) {
            System.out.println("Exception: getConnection " + ex.getMessage());
        }

    }

    private void inserttData(ArrayList<KhachHang> arrayListKH) {
        String insertTableSQL = "insert into user (id,firstname,lastname,room,valid) VALUES (?,?,?,?,?)";
        PreparedStatement preparedStatement = null;
        try {
            getConnection(JDBC_DB_URL, JDBC_PASS, JDBC_USER);
            int a = 1;
            if (connection != null) {
                for (int i = 0; i < arrayListKH.size(); i++) {
                    preparedStatement = connection.prepareStatement(insertTableSQL);

                    KhachHang k = arrayListKH.get(i);
                    preparedStatement.setString(a, k.getId());
                    preparedStatement.setString(a++, k.getFirstname());
                    preparedStatement.setString(a++, k.getLastname());
                    preparedStatement.setString(a++, k.getRoom());
                    preparedStatement.setString(a++, k.getValid());
                    preparedStatement.executeUpdate();
                }
                System.out.println("Insert du lieu vao db thanh cong");
            }

        } catch (Exception throwables) {
            System.out.println("Exception inserttData:  " + throwables.getMessage());

        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Exception: finally inserttData " + e.getMessage());
            }
        }
    }

    private void getData() {
        try {
            getConnection(JDBC_DB_URL, JDBC_PASS, JDBC_USER);
            String qr = "select * from user where valid != 0";

            stmt = connection.prepareStatement(qr);

            // stmt.setString(1, "1");
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                ArrayList<KhachHang> arr = new ArrayList<KhachHang>();
                try {
                    String id = resultSet.getString("id");
                    String firstname = resultSet.getString("firstname");
                    String lastname = resultSet.getString("lastname");
                    String room = resultSet.getString("room");
                    String valid = resultSet.getString("valid");

                    KhachHang kh = new KhachHang(id, firstname, lastname, room, valid);
                    arr.add(kh);
                    System.out.println(id + "|" + firstname + "|" + lastname + "|" + room + "|" + valid);

                    writeFile(arr);
                } catch (Exception e) {
                    System.out.println("err: " + e.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception: getData " + ex.getMessage());
        } finally {
            try {
                connection.close();
                stmt.close();
                resultSet.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private void writeFile(ArrayList<KhachHang> arr) {
        FileWriter fw = null;
        try {
            fw = new FileWriter("output.txt", true);
            for (int i = 0; i < arr.size(); i++) {
                KhachHang k = arr.get(i);
                fw.write(k.getId() + '|' + k.getFirstname() + '|' + k.getLastname() + "|"
                        + k.getRoom() + '|' + k.getValid());
                fw.write("\n");
            }
        } catch (Exception ex) {
            System.out.println("Err WriteFile: " + ex.getMessage());
        }finally{
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}