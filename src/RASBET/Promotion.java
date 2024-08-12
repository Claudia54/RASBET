package RASBET.RASBET;

public class Promotion {

    /**
     * friendCode
     * bonus -> registando-se com esta promoçao ativa recebe x bonus
     * odd multiplier
     */

    private String type;    //Type of the promotion
    private boolean state;  //Active or not
    private float amount;  //Value it has
    private float minVal;

    public Promotion(String type, float amount, float minval){
        this.type = type;
        this.state = false;
        this.amount = amount;
        this.minVal = minval;
    }

    public int stateToInt(){
        return (this.state ? 1 : 0);
    }

    public boolean getState(){
        return this.state;
    }

    public void setState(boolean state){
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getMinVal() {
        return minVal;
    }

    public void setMinVal(float minVal) {
        this.minVal = minVal;
    }

    /*public static String tostring(String type, float amount, float minVal, int state){
        StringBuilder sb = new StringBuilder();

        if(type.equals(friendBonus)){
            return "";
        }

        if(type.equals(bonus)){
            sb.append("Bonus no registo | Valor: "+amount+" | Estado atual: "+ state);
        }
        if(type.equals(multiplier)){
            sb.append("Multiplicador de Odd " +
                    "| Valor multiplicado: "+amount+" " +
                    "| Valor mínimo da Odd: "+minVal+" " +
                    "| Estado atual: "+state);
        }
        return sb.toString();
    }*/

}
