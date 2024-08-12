package RASBET.RASBET;

class ExceptionEmail extends Exception{
    private String msg;

    public ExceptionEmail(String warning){
        this.msg = warning;
    }

    public String getMessage() {
        return this.msg;
    }

}

class ExceptionPassword extends Exception{
    private String msg;

    public ExceptionPassword(){
        this.msg = "The password given doesn't follow the guidelines set for passwords.";
    }

    public String getMessage() {
        return this.msg;
    }
}

class ExceptionAge extends Exception{
    private String msg;

    public ExceptionAge(){
        this.msg = "The minimal age required to sign in is 18.";
    }

    public String getMessage() {
        return this.msg;
    }
}

class ExceptionNIF extends Exception{
    private String msg;

    public ExceptionNIF(String nif){
        this.msg = "The nif \""+nif+"\" already exists.";
    }

    public String getMessage() {
        return this.msg;
    }
}

class ExceptionCC extends Exception{
    private String msg;

    public ExceptionCC(String cc){
        this.msg = "The cc \""+cc+"\" already exists.";
    }

    public String getMessage() {
        return this.msg;
    }
}

class ExceptionClosed extends Exception{

    private String message;

    public ExceptionClosed(){
        this.message = "This game is closed. Its state can't be changed.";
    }
    @Override
    public String getMessage() {
        return this.message;
    }

}

class ExceptionNegativeValue extends Exception{

    private String message;

    public ExceptionNegativeValue(){
        this.message = "The value determined is negative. Deposit/Withdraw can't be concluded.";
    }
    @Override
    public String getMessage() {
        return this.message;
    }

}

class ExceptionNotEnoughMoney extends Exception{

    private String message;

    public ExceptionNotEnoughMoney(){
        this.message = "The value set is higher than the value on the wallet.";
    }

    public String getMessage(){
        return this.message;
    }

}




