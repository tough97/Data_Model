package com.ynjt.data.tree.test;

import java.sql.*;

public class DatabaseTest {

    static {
        try {
            Class.forName(com.mysql.jdbc.Driver.class.getCanonicalName());
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final String ID_PREFIX = "jiushiaidaoshenchucaiyoutaa,hahahaha";
    private static final String[] alpha = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
    };

    private static void initTest() throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "admin");
        connection.setAutoCommit(false);
        final PreparedStatement statement = connection.prepareStatement("insert into test(code) values(?)");
        for (int i = 0; i < 1111100; i++) {
            statement.setString(1, ID_PREFIX + i);
            statement.addBatch();
            if ((i % 10000) == 0) {
                statement.executeBatch();
            }
            System.out.println("record " + i + " inserted");
        }
        statement.executeBatch();
        connection.commit();
        statement.close();
        connection.close();
    }

    private static void insertKey() throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "admin");
        connection.setAutoCommit(false);
        final PreparedStatement statement = connection.prepareStatement("insert into test_key(code, k) values(?, ?)");
        long cnt = 0;
        for(int row = 0; row < 1111100; row++){
            final String rowCode = ID_PREFIX + row;
            for(final String a : alpha){
                statement.setString(1, rowCode);
                statement.setString(2, a);
                statement.addBatch();
                cnt++;
                if((cnt % 10000) == 0){
                    statement.executeBatch();
                }
            }
        }
        statement.executeBatch();
        connection.commit();
        statement.close();
        connection.close();
    }

    public static void main(String[] args) throws SQLException {
//        insertKey();
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "admin");
        final PreparedStatement statement = connection.prepareStatement("select code from test_key where code = ? and k = ?");
        statement.setString(1, "jiushiaidaoshenchucaiyoutaa,hahahaha1111099");
        statement.setString(2, "h");
        final long start = System.currentTimeMillis();
        final ResultSet resultSet = statement.executeQuery();
        final long end = System.currentTimeMillis();
        System.out.println("Time used = " + (end - start));
        if (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
        resultSet.close();
        statement.close();
        connection.close();
    }

}
