package com.siberianhealth.eventregistration;

import com.siberianhealth.eventregistration.model.Person;
import com.siberianhealth.eventregistration.tickets.Ticket;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
/**
 * Original version of this file was part of InterClient 2.01 examples
 * 
 * Copyright InterBase Software Corporation, 1998. Written by
 * com.inprise.interbase.interclient.r&d.PaulOstler :-)
 * 
 * Code was modified by Roman Rokytskyy to show that Firebird JCA-JDBC driver
 * does not introduce additional complexity in normal driver usage scenario.
 * 
 * A small application to demonstrate basic, but not necessarily simple, JDBC
 * features.
 * 
 * Note: you will need to hardwire the path to your copy of employee.fdb as well
 * as supply a user/password in the code below at the beginning of method
 * main().
 */




public class Model {

    private static final String IP_CHROME = "192.168.0.101";
    private static final String IP_DALAMBER = "192.168.0.42";
    private static String SERVER_IP = null;

    private static final String JDBC_CONNECTION = "jdbc:firebirdsql";
    private static final String DB_NAME = "er-test";
    private static final String USER_DEFAULT = "SYSDBA";
    private static final String PASSWORD_DEFAULT = "masterkey";




    public enum TicketStatus{TICKET_EXIST, E_NO_TICKETS, E_TICKET_ALREADY_LANDED, E_TICKET_ERROR};



    private static Connection connection = null;


    private static Model ourInstance = null;

    public static Model getInstance() {

        if (ourInstance == null){
            ourInstance = new Model();


            String ip;
            if (android.os.Build.MODEL.indexOf("Nexus") >= 0){

                ip = IP_CHROME;
            }
            else {

                ip = IP_DALAMBER;
            }


            try{
                setServerIP(ip);
            }
            catch (Exception e){

            }
        }

        return ourInstance;
    }



    private Model() {
    }


    public String getServerIP(){

        return SERVER_IP;
    }

    public static  void setServerIP(String new_ip_address) throws Exception{

        SERVER_IP = new_ip_address;

        connect();
    }


    /*
     * Сажаем людей на выбранные места
     */
    public boolean PrintTickets(String device_id, String ticket_id){

        ResultSet rs = null;
        CallableStatement stmt;

        String upToNCharacters = device_id.substring(0, 9);

        try {

            stmt = connection.prepareCall("{ call SR_PRINT_TICKET(?,?) }");
            stmt.setString(1, upToNCharacters);
            stmt.setInt(2, Integer.parseInt(ticket_id));
            stmt.execute();

            return true;
        }
        catch (java.sql.SQLException e) {
            e.printStackTrace();
            System.out.println("sql exception: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        //return false;
    }


    /*
     * Сажаем людей на выбранные места
     */
    public boolean LandTickets(String tickets){

        ResultSet rs = null;
        CallableStatement stmt;

        try {

            stmt = connection.prepareCall("{ call SR_LAND_TICKETS(?) }");
            stmt.setString(1, tickets);
            stmt.execute();

            return true;
        } catch (java.sql.SQLException e) {
            return false;

        } catch (Exception e) {
            return false;
        }

        //return false;
    }

    /*
     * Проверяем, есть ли билет на мероприятие
     */
    public ArrayList<Ticket> GetTickets(int id){

        ArrayList<Ticket> results = new ArrayList<Ticket>();

        ResultSet rs = null;
        CallableStatement stmt;

        try {

            stmt = connection.prepareCall("{ call SR_GET_TICKETS(?) }");

            stmt.setInt(1, id);
            stmt.execute();

            rs = stmt.getResultSet();


            while (rs.next()) {

                Ticket ticket = new Ticket();

                ticket.setId(    rs.getString("O_TICKET") );
                ticket.setName(  rs.getString("O_NAME") );
                ticket.setPlace( rs.getString("O_SECTOR"), rs.getInt("O_ROW"), rs.getInt("O_COL") );

                results.add( ticket );

            }


            return results;


        } catch (java.sql.SQLException e) {


            return null;

        } catch (Exception e) {

            return null;

        }
    }


    /*
     * Проверяем, есть ли билет на мероприятие
     */
    public TicketStatus CheckTicket(int id){

        //ResultSet results = null;

        try {

            CallableStatement stmt = connection.prepareCall("{ call SR_CHECK_TICKET(?) }");

            stmt.setInt(1, id);
            stmt.execute();


        } catch (java.sql.SQLException e) {

            String type = parseException(e);

            switch (type) {

                case "E_NO_TICKETS":  return TicketStatus.E_NO_TICKETS;

                case "E_TICKET_ALREADY_LANDED":  return TicketStatus.E_TICKET_ALREADY_LANDED;

                default:
                    return TicketStatus.E_TICKET_ERROR;
            }

        }

        return TicketStatus.TICKET_EXIST;
    }




    private String parseException(java.sql.SQLException e){

        showSQLException(e);
        String[] list = e.getMessage().split("\n");
        return list[1];
    }



    public Person getPerson(String contract) {


        ResultSet rs = null;

        try {


            CallableStatement stmt = connection.prepareCall("{ call SR_FIND_CONTRACT(?) }");

            stmt.setString(1, contract);
            stmt.execute();


            Person newPerson = new Person();

            newPerson.setContract( contract );
            newPerson.setId( stmt.getInt("O_EMPLOYEE") );
            newPerson.setName( stmt.getString("O_NAME") );
            newPerson.setPhoto( stmt.getBlob("O_PHOTO") );


            return newPerson;

        } catch (java.sql.SQLException e) {
            System.out.println("Unable to step thru results of query");
            showSQLException(e);
            return null;
        } catch (Exception e) {

            return null;
        }
    }


    /**
     * Make a connection to an employee.fdb on your local machine, and
     * demonstrate basic JDBC features.
     * <p>
     * On the commandline a JDBC-url, username and password can be passed,
     * otherwise defaults are used.
     */
    //public static void main(String args[]) throws Exception {
    public static void connect() throws Exception {

        /* If localhost is not recognized, try using your local machine's name
         * or the loopback IP address 127.0.0.1 in place of localhost.
         * 
         * The Firebird JDBC driver recognizes a number of different URLs:
         * 
         * Pure Java / Type 4:
         * jdbc:firebirdsql://<host>[:<port>]/<alias-or-path-to-db>
         * NOTE: On linux <alias-or-path-to-db> MUST include the root /
         * Alternative URL format:
         * jdbc:firebirdsql:<host>[/port]:<alias-or-path-to-db>
         * Alternative prefix:
         * jdbc:firebirdsql:java:
         * Prefixes specifically for OpenOffice / LibreOffice Base:
         * jdbc:firebirdsql:oo:
         * jdbc:firebird:oo:
         * 
         * Native / Type 2:
         * jdbc:firebirdsql:native://<host>[:<port>]/<alias-or-path-to-db>
         * Alternative URL format:
         * jdbc:firebirdsql:native:<host[/port]:<alias-or-path-to-db>
         * 
         * Native / Type 2 local connection:
         * jdbc:firebirdsql:local:<alias-or-path-to-db>
         * 
         * Firebird Embedded:
         * jdbc:firebirdsql:embedded:alias-or-path-to-db>
         * 
         * The URL can be extended with properties to configure the connection 
         * (similar to a HTTP query string) by adding to the end of the string:
         * ?<property>[=<value>](&<property>[=<value>])*
         * 
         * Examples:
         * Connecting to employee.fdb on localhost using the type4 driver with UTF8:
         * jdbc:firebirdsql://localhost/C:/database/employee.fdb?lc_ctype=UTF8
         * 
         * Connecting to dialect1.fdb using native with WIN1252 and resultset tracking disabled:
         * jdbc:firebirdsql://localhost/C:/database/dialect1.fdb?dialect=1&lc_ctype=WIN1252&noResultSetTracking
         */
        /*
        String databaseURL = args.length == 0 ? URL_DEFAULT : args[0];
        String user = args.length < 2 ? USER_DEFAULT : args[1];
        String password = args.length < 3 ? PASSWORD_DEFAULT : args[2];
        */

        String databaseURL = JDBC_CONNECTION + ":" + SERVER_IP + ":" + DB_NAME;
        String user = USER_DEFAULT;
        String password = PASSWORD_DEFAULT;

        /* Here are the JDBC objects we're going to work with.
         * We're defining them outside the scope of the try block because
         * they need to be visible in a finally clause which will be used
         * to close everything when we are done.
         */
        java.sql.Driver driver = null;

        Statement stmt = null;
        ResultSet rs = null;


        System.setProperty("FBAdbLog", "true");


        try {
            //register Driver
            Class.forName("org.firebirdsql.jdbc.FBDriver");

        } catch (java.lang.ClassNotFoundException e) {
            // A call to Class.forName() forces us to consider this exception
            System.out.println("Firebird JCA-JDBC driver not found in class path");
            System.out.println(e.getMessage());

            throw new Exception("Firebird JCA-JDBC driver not found in class path");
            //return;
        }


         /* At this point the driver should be registered with the DriverManager.
          * Try to find the registered driver that recognizes Firebird URLs
            */
        try {
            // We pass the entire database URL, but we could just pass "jdbc:firebirdsql:"
            driver = java.sql.DriverManager.getDriver(databaseURL);
            System.out.println("Firebird JCA-JDBC driver version " + driver.getMajorVersion() + "."
                    + driver.getMinorVersion() + " registered with driver manager.");
        } catch (java.sql.SQLException e) {
            System.out.println("Unable to find Firebird JCA-JDBC driver among the registered drivers.");

            showSQLException(e);
            throw new Exception("Unable to find Firebird JCA-JDBC driver among the registered drivers.");

            //return;
        }

        try {
            connection = DriverManager.getConnection(databaseURL, user, password);
            System.out.println("Connection established.");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to establish a connection through the driver manager.");

            showSQLException(e);
            throw new Exception("Unable to establish a connection through the driver manager.");

            //return;
        }


        System.setProperty("FBAdbLog", "true");
  /*

        try{
            // Disable the default autocommit so we can undo our changes later
            try {
                con.setAutoCommit(false);
                System.out.println("Auto-commit is disabled.");
            } catch (java.sql.SQLException e) {
                System.out.println("Unable to disable autocommit.");
                showSQLException(e);
                return;
            }

            // Now that we have a connection, let's try to get some meta data...
            try {
                java.sql.DatabaseMetaData dbMetaData = con.getMetaData();

                // Ok, let's query a driver/database capability
                if (dbMetaData.supportsTransactions())
                    System.out.println("Transactions are supported.");
                else
                    System.out.println("Transactions are not supported.");

                // What are the views defined on this database?
                java.sql.ResultSet tables = dbMetaData.getTables(null, null, "%", new String[] { "VIEW" });
                while (tables.next()) {
                    System.out.println(tables.getString("TABLE_NAME") + " is a view.");
                }
                tables.close();
            } catch (java.sql.SQLException e) {
                System.out.println("Unable to extract database meta data.");
                showSQLException(e);
            }

            ///* Let's try to submit some static SQL on the connection.
             //* Note: This SQL should throw an exception on employee.fdb because
             //* of an integrity constraint violation.

            try {
                stmt = con.createStatement();
                stmt.executeUpdate("update employee set salary = salary + 10000");
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                System.out.println("Unable to increase everyone's salary.");
                showSQLException(e);
                // We expected this to fail, so don't return, let's keep going...
            }

            // Let's submit some static SQL which produces a result set.
            // Notice that the statement stmt is reused with a new SQL string.
            //
            try {
                rs = stmt.executeQuery("select full_name from employee where salary < 50000");
            } catch (java.sql.SQLException e) {
                System.out.println("Unable to submit a static SQL query.");
                showSQLException(e);
                // We can't go much further without a result set, return...
                return;
            }

            // The query above could just as easily have been dynamic SQL,
            // eg. if the SQL had been entered as user input.
            // As a dynamic query, we'd need to query the result set meta data
            // for information about the result set's columns.
            //
            try {
                java.sql.ResultSetMetaData rsMetaData = rs.getMetaData();
                System.out.println("The query executed has " + rsMetaData.getColumnCount() + " result columns.");
                System.out.println("Here are the columns: ");
                for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                    System.out.println(rsMetaData.getColumnName(i) + " of type " + rsMetaData.getColumnTypeName(i));
                }
            } catch (java.sql.SQLException e) {
                System.out.println("Unable to extract result set meta data.");
                showSQLException(e);
            }

            // Ok, lets step thru the results of the query...
            try {
                System.out.println("Here are the employee's whose salary < $50,000");
                while (rs.next()) {
                    System.out.println(rs.getString("full_name"));
                }
            } catch (java.sql.SQLException e) {
                System.out.println("Unable to step thru results of query");
                showSQLException(e);
                return;
            }


        } finally {
            System.out.println("Closing database resources and rolling back any changes we made to the database.");

            // Now that we're all finished, let's release database resources.
            try {
                if (rs != null)
                    rs.close();
            } catch (java.sql.SQLException e) {
                showSQLException(e);
            }
            
            try {
                if (stmt != null)
                    stmt.close();
            } catch (java.sql.SQLException e) {
                showSQLException(e);
            }

            // Before we close the connection, let's rollback any changes we may have made.
            try {
                if (con != null)
                    con.rollback();
            } catch (java.sql.SQLException e) {
                showSQLException(e);
            }
            try {
                if (con != null)
                    con.close();
            } catch (java.sql.SQLException e) {
                showSQLException(e);
            }
        }

        */
    }

    /**
     * Display an SQLException which has occurred in this application.
     * @param e SQLException
     */
    private static void  showSQLException(java.sql.SQLException e) {
        /* Notice that a SQLException is actually a chain of SQLExceptions,
         * let's not forget to print all of them
         */
        java.sql.SQLException next = e;
        while (next != null) {
            System.out.println(next.getMessage());
            System.out.println("Error Code: " + next.getErrorCode());
            System.out.println("SQL State: " + next.getSQLState());
            next = next.getNextException();
        }
    }
}
