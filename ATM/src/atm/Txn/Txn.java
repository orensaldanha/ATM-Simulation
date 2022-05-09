package atm.Txn; //user defined package named atm.Txn

import java.sql.*; //import Connection, Statement, ResultSet, DriverManager, SQLException class for connecting to the database

public class Txn {
    double amount;
    int account_n;
        public Txn(double amt, int acc_n) {
        //Constructor for Txn class that takes amount and account number as parameters
        amount = amt;
        account_n = acc_n;        
        
        //Create new Transaction
        String sql = "INSERT INTO ATMLOGIN.ATM_Txn (Txn_Date,Txn_Time,ACCOUNTNUMBER,CRDR,Amount) VALUES (" + "CURRENT_DATE" + "," + "CURRENT_TIME" +  "," + account_n + "," + "'DR'" + "," + amount + ")";
        
        //Add transaction to database
        try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/ATMDB", "atmlogin", "loginatm");  Statement statement = connection.createStatement();) {
            statement.executeUpdate(sql);
        } catch (SQLException se) {
            System.out.println(se);
        }
    }
}
