package com.example.android.shop.Adapters;


import com.example.android.shop.Utils.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlUtils {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String SQL;
    private String[] columnNames = {};
    private List<List<Object>> rows = new ArrayList<List<Object>>();
    private ResultSetMetaData metaData;
    private boolean editableTable = true;
    private boolean ErrorExists = false;
    private String ErrorMessage = "";
    private String ErrorCause = "";
    private int ErrorNumber;

    public MysqlUtils(String url, String driverName,
                      String user, String passwd) {
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, user, passwd);
            statement = connection.createStatement();
        }catch (ClassNotFoundException ex) {
            ErrorExists = true;
            ErrorCause = ex.toString();
            ErrorNumber = 0;
            System.err.println("le pilote de la base de donnée ne peut être trouvé.");
            System.err.println(ex);
        }catch (SQLException ex) {
            ErrorExists = true;
            ErrorCause = ex.toString();
            ErrorNumber = ex.getErrorCode();
            System.err.println(ErrorNumber);
            System.err.println("Connexion a la base de donnée impossible.");
            System.err.println(ex.toString() + ex.getErrorCode());
        }
    }

    /**
     * Estabilish a connection with the specified params
     * @return
     */
    public static MysqlUtils connect(){
        MysqlUtils result = new MysqlUtils( Utilities.URL,
                                            Utilities.DRIVER_NAME,
                                            Utilities.USER,
                                            Utilities.PASSWORD);
        return result;
    }

    /**
     * returns the number of rows of the submitted query
     * @return number of rows
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * returns the numbers of column of the submitted query
     * @return number of columns
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * returns the element qt the specified position a row, acolumn
     * @param aRow the row of the specified element
     * @param aColumn the column of the specified column
     * @return the element of the specified position
     */
    public Object getValueAt(int aRow, int aColumn) {
        List<Object> row = rows.get(aRow);
        return row.get(aColumn);
    }

    /**
     * Methode to execute a query in the database
     * @param query the query passed as parameter to be executed
     */
        public void executeQuery(String query) {
        if (connection == null || statement == null) {
            System.err.println("aucune base de donnée ouverte pour exécuter la requête.");
            return;
        }

        try {
            resultSet = statement.executeQuery(query);
            metaData = resultSet.getMetaData();

            int numberOfColumns = metaData.getColumnCount();
            columnNames = new String[numberOfColumns];
            // Get the column names and cache them.
            // Then we can close the connection.
            for (int column = 0; column < numberOfColumns; column++) {
                columnNames[column] = metaData.getColumnLabel(column + 1);
            }

            // Get all rows.
            rows = new ArrayList<List<Object>>();


            while (resultSet.next()) {
                List<Object> newRow = new ArrayList<Object>();
                for (int i = 1; i <= numberOfColumns; i++) {
                    newRow.add(resultSet.getObject(i));
                }
                rows.add(newRow);
            }

            ErrorExists = false;
        } catch (SQLException ex) {
            ErrorNumber = ex.getErrorCode();
            ErrorCause = ex.toString();
            ErrorMessage = "la requete ne peut être executer";
            ErrorExists = true;
            System.err.println(query);
            System.err.println(ErrorMessage);
            System.err.println(ex);
        }
    }

    public void print(){
        System.out.println("row X column : (" + getRowCount() +
                " X " + getColumnCount() + ")");
        for (int i=0; i < getRowCount(); i++){
            for (int j=0; j < getColumnCount(); j++){
                System.out.print(getValueAt(i,j) + "\t");
            }
            System.out.println();
        }
    }
}
