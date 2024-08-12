package RASBET.RASBET_DATA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAO {

    private DBConnection dbc;
    private Statement stmt;

    public DAO(){
        this.dbc = null;
        this.stmt = null;
    }

    public ResultSet querySQL(String query) {
        ResultSet output = null;
        try {
            output = this.stmt.executeQuery(query);
        } catch (SQLException s) {
            System.out.println("Error executing query."+ s.getMessage());
        }
        return output;
    }

    public boolean executeSQL(String query) {
        boolean b = true;
        try {
            this.stmt.execute(query);
        } catch (SQLException s) {
            b = false;
            System.out.println("Error executing query."+ s.getMessage());
        }
        return b;
    }

    public void startConnStmt() {
        try {
            this.dbc = new DBConnection();
            this.stmt = this.dbc.createStatement();
        } catch (SQLException s) {
            System.out.println("Error creating statement.");
        }
    }

    public void closeConn(){
        this.dbc.closeConnection();
    }



    /**
     * Auxiliar function that determines if a result set as values. Returns true if it isn't empty
     * @param input ResultSet
     * @return boolean
     */
    public boolean resultSetIsNotEmpty(ResultSet input) {
        boolean b = false;
        try {
            if (input.next())
                b = true;
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
        return b;
    }

    /**
     * Aux function that gets the first int of the resultSet. In case of error the function will return the defaultVal
     * @param input ResultSet
     * @param defaultVal int
     * @return int
     */
    public int resultSetGetFirstInt(ResultSet input, int defaultVal) {
        int output = defaultVal;
        try {
            input.next();
            output = input.getInt(1);
        } catch (SQLException s) {
            System.out.println("There is no first int.");
        }
        return output;
    }

    /**
     * Aux function that gets the first float of the resultSet. In case of error the function will return the defaultVal
     * @param input ResultSet
     * @param defaultVal float
     * @return float
     */
    public float resultSetGetFirstFloat(ResultSet input, float defaultVal) {
        float output = defaultVal;
        try {
            input.next();
            output = input.getFloat(1);
        } catch (SQLException s) {
            System.out.println("There is no first int.");
        }
        return output;
    }

    /**
     * Aux function that gets the first int of the resultSet. In case of error the function will return ""
     * @param input ResultSet
     * @return String
     */
    public String resultSetGetFirstString(ResultSet input) {
        String output = "";
        try {
            input.next();
            output = input.getString(1);
        } catch (SQLException e) {
            System.out.println("Error while trying to consult ResultSet.");
        }
        return output;
    }

}
