package com.example.demo.RASBET_LN.User;

class ExceptionNegativeValue extends java.lang.Exception {

    private String message;

    public ExceptionNegativeValue(){
            this.message = "The value determined is negative. Deposit/Withdraw can't be concluded.";
        }
        @Override
    public String getMessage() {
            return this.message;
        }

}

class ExceptionNotEnoughMoney extends java.lang.Exception {

    private String message;

    public ExceptionNotEnoughMoney(){
            this.message = "The value set is higher than the value on the wallet.";
        }

    public String getMessage(){
            return this.message;
        }

}


