import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class Main {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/glow";
    static final String USER = "sa";
    static final String PASS = "test";

    public static void main(String[] args) {
        Connection connection;
        Statement statement;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
            statement = connection.createStatement();
            FileWriter writer = new FileWriter("out.txt");

            ResultSet rs = statement.executeQuery("Select TABLE_COLS.TABLE_NAME, TABLE_COLS.COLUMN_NAME ," +
                    "TABLE_COLS.COLUMN_TYPE, TABLE_LIST.PK from TABLE_COLS, TABLE_LIST " +
                    "where TABLE_COLS.TABLE_NAME = TABLE_LIST.TABLE_NAME;");

            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String columnName = rs.getString("COLUMN_NAME");
                String columnType = rs.getString("COLUMN_TYPE");
                String[] pkList = rs.getString("PK").split(", ");
                for (String pk : pkList){
                    if (pk.equalsIgnoreCase(columnName)){
                        writer.append(tableName).append(", ").append(pk).append(", ").append(columnType)
                                .append('\n');
                    }
                }

            }

            writer.flush();

        } catch(SQLException | ClassNotFoundException | IOException se) {
            se.printStackTrace();
        }
    }
}