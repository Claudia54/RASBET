package com.example.demo.RASBET_DATA;

import com.example.demo.RASBET_LN.Promotion;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SystemDAO extends DAO{

    public SystemDAO() {
        super();
    }

    // verifies promo

    public float getAmount(String type){
        float amount = 0;
        super.startConnStmt();
        String sql = "select amount from Promotion where idPromo = '"+type+"';";
        ResultSet res = super.querySQL(sql);
        amount = resultSetGetFirstFloat(res, 0);
        super.closeConn();
        return amount;
    }

    public List<Promotion> getPromotions(){
        super.startConnStmt();
        String sql = "select * from Promotion;";
        ResultSet result = querySQL(sql);

        List<Promotion> promotions = new ArrayList<>();
        try{
            while (result.next()){
                Promotion promo = new Promotion(result.getString(1), result.getFloat(2), result.getFloat(3), result.getInt(4) != 0);
                /*if(!promo.equals("")){
                    promotions.add(promo);
                }*/
                promotions.add(promo);
            }
        }catch (Exception e){
            System.out.println("Result Set can't be interpreted.");
        }
        super.closeConn();
        return promotions;
    }

    public void changeStatePromo(String type, int state){
        super.startConnStmt();
        String sql = "update Promotion set active = "+state+" where idPromo = '"+type+"';";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that checks if a promotion of a certain type exists.
     * It returns true, if the promotion exist
     * @param type String
     * @return boolean
     */
    public boolean existsPromotionofType(String type){
        super.startConnStmt();
        String sql = "select * from Promotion where idPromo = '"+type+"';";
        ResultSet res = super.querySQL(sql);
        boolean output = super.resultSetIsNotEmpty(res);
        return output;
    }

    /**
     * Function that checks if the promotion of a given type, is in a certain state.
     * The function returns true if it is in state.
     * @param type String
     * @param state int
     * @return boolean
     */
    public boolean checksPromotionState(String type, int state){
        super.startConnStmt();
        String sql = "select * from Promotion where idPromo = '"+type+"' and active = "+state+";";
        ResultSet res = super.querySQL(sql);
        boolean output = super.resultSetIsNotEmpty(res);
        return output;
    }



    /*
     * -------------------------------------------------------
     *                     Notification
     * -------------------------------------------------------
     */

    /**
     * Function that gets the notification that the user with email should receive.
     * If no notification should be shared, this function will return "".
     * @param email String
     * @return String
     */
    public String checkNotification(String email){
        super.startConnStmt();
        String sql = "select notification from Notification where email = '"+email+"';";
        ResultSet res = super.querySQL(sql);
        String notification = "";
        try{
            if(res.next()){
                notification = super.resultSetGetFirstString(res);
                String dropNot = "delete from Notification where email = '"+email+"';";
                executeSQL(dropNot);
            }
            //Se estiver vazio não faz nada;
        }
        catch (Exception e){}

        super.closeConn();
        return notification;
    }

    /**
     * Function that allows to add a notification of bet won that should be shown next time the user logs in.
     * @param email String
     * @param notification String
     */
    public void addNotification (String email, String notification){
        super.startConnStmt();
        String sql = "insert into Notification values ('"+email+"','"+notification+"');";
        super.executeSQL(sql);
        super.closeConn();
    }

}
