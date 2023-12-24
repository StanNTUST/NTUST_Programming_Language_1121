
/*****************************************************************
    CS4001301 Programming Languages                   
    
    Programming Assignment #1
    
    Java programming using subtype, subclass, and exception handling
    
    To compile: %> javac Application.java
    
    To execute: %> java Application

******************************************************************/

import java.util.*;

//class for throwing exception
class BankingException extends Exception {
    BankingException () { super(); }
    BankingException (String s) { super(s); }
} 

//basic interface account with name and balance function
interface BasicAccount {
    String name();
    double balance();	
}

//basic interface withdrawable account with withdraw function and extends basic account
interface WithdrawableAccount extends BasicAccount {
    double withdraw(double amount) throws BankingException;	
}

//basic interface deposit account with withdraw function and extends basic account
interface DepositableAccount extends BasicAccount {
    double deposit(double amount) throws BankingException;	
}

//basic interface interestable account with withdraw function and extends basic account
interface InterestableAccount extends BasicAccount {
    double computeInterest() throws BankingException;	
}

//interface fullfunctional account extends withdrawable, deposit and interestable account
interface FullFunctionalAccount extends WithdrawableAccount,
                                        DepositableAccount,
                                        InterestableAccount {
}

public abstract class Account {
	
    // protected variables to store commom attributes for every bank accounts	
    protected String accountName;
    protected double accountBalance;
    protected double accountInterestRate=0.12;
    protected Date openDate;
    protected Date lastInterestDate;
    
    // public methods for every bank accounts
    public String name() {
    	return(accountName);	
    }	
    
    public double balance() {
        return(accountBalance);
    }

    abstract public double deposit(double amount, Date depositDate) throws BankingException;

    public double deposit(double amount) throws BankingException{
        Date depositDate = new Date();
        return(deposit(amount, depositDate));
    } 

    abstract double withdraw(double amount, Date withdrawDate) throws BankingException;
    
    public double withdraw(double amount) throws BankingException {
        Date withdrawDate = new Date();
        return(withdraw(amount, withdrawDate));
    }
    
    abstract double computeInterest(Date interestDate) throws BankingException;
    
    public double computeInterest() throws BankingException {
        Date interestDate = new Date();
        return(computeInterest(interestDate));
    }

    //status message
    public void status(){
        System.out.println ("Account <" + accountName + "> now has $" + accountBalance + " balance");
        return;
    }
    //withdraw message
    public void withdrawMessage(int dollar){
        System.out.println ("Account <" +accountName + "> withdraw $"+ dollar +" dollars");
    }
    //deposit message
    public void depositMessage(int dollar){
        System.out.println ("Account <" + accountName + "> deposit $" + dollar + " dollars");
    }
    //compute message
    public void computeMessage(String text){
        System.out.println ("Account <" + accountName + "> compute interest "+ text );
    }

}

/*
 *  Derived class: CheckingAccount
 *
 *  Description:
 *      Interest is computed daily; there's no fee for
 *      withdraw; there is a minimum balance of $1000.
 */
                          
class CheckingAccount extends Account implements FullFunctionalAccount {
    //constructor
    CheckingAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        openDate = new Date();
        lastInterestDate = openDate;	
    }
    
    //constructor
    CheckingAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        openDate = firstDate;
        lastInterestDate = openDate;	
    }	
    
    //function to deposit money
    public double deposit(double amount, Date depositDate) throws BankingException {
        accountBalance += amount;
        return accountBalance;
    }

    //function to withdraw money
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        // minimum balance is 1000, raise exception if violated
        if ((accountBalance  - amount) < 1000) {
            throw new BankingException ("Underfraft from Checking account name:" +accountName);
        } else {
            accountBalance -= amount;	
            return(accountBalance); 	
        }
    }
    
    //function to compute interest
    public double computeInterest (Date interestDate) throws BankingException {
        //raise exception if compute date is invalid
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException ("Invalid date to compute interest for Checking account name" +accountName);                            	
        }
        
        //compute, output and update last interest date
        int numberOfDays = (int) ((interestDate.getTime() - lastInterestDate.getTime())/ 86400000.0);
        System.out.println("Number of days since last interest is " + numberOfDays);
        double interestEarned = (double) numberOfDays / 365.0 *accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned); 
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return(accountBalance);
    }  	
}

/*
 *  Derived class: SavingAccount
 *
 *  Description:
 *      Interest is computed monthly; there's 1 dollar for
 *      transaction fee after three times in a month and reset
 *      transaction times in next month; minimum balance is $0.
 */

class SavingAccount extends Account implements FullFunctionalAccount {
    Date currentDate;
    int transactionTimes=0;
    static int transactionFee=1;

    //constructor
	SavingAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        openDate = new Date();
        lastInterestDate = openDate;
        currentDate=openDate;
    }
    
    //constructor
    SavingAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        openDate = firstDate;
        lastInterestDate = openDate;
        currentDate=openDate;
    }

    //function for check if a month is passed or not
    public int check(Date date){
        //compute the month
        int numberOfMonths = (int) ((date.getTime() - currentDate.getTime()) / (86400000.0*30));
        
        //if time pass more than a month, reset the current date and transaction times
        if(numberOfMonths>0){
            currentDate.setTime(currentDate.getTime()+86400000*30*numberOfMonths);
            return 1;
        }
        //else transaction times add 1
        else return transactionTimes+1;
    }

    //function to deposit money
    public double deposit(double amount, Date depositDate) throws BankingException {
        //check transaction times
        transactionTimes=check(depositDate);

        //if transaction times more than 3, minus transaction fee
        if(transactionTimes>3) accountBalance-=transactionFee;

        accountBalance += amount;
        return accountBalance;
    }

    //function to withdraw money
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        //minimum balance is 0, raise exception if violated
        if((accountBalance  - amount)<0) 
            throw new BankingException ("Underfraft from Saving account name: " + accountName);
        //check transaction times
        transactionTimes=check(withdrawDate);

        //if transaction times more than 3, minus transaction fee
        if(transactionTimes>3) {
            //if underfraft after decrease transaction fee, throw error
            if((accountBalance  - amount-transactionFee)<0) 
                throw new BankingException ("Underfraft from Saving account name: " + accountName);
            
            accountBalance-=transactionFee;
        }

        accountBalance -= amount;	
        return(accountBalance); 	                                    	
    }
    
    //function to compute interest 
    public double computeInterest (Date interestDate) throws BankingException {
        //raise exception if compute date is invalid
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException ("Invalid date to compute interest for Saving account name " +accountName);
        }
                                        	
         //compute interest monthly
        int numberOfMonths = (int) ((interestDate.getTime() - lastInterestDate.getTime())/ (86400000.0*30));
        System.out.println("Number of months since last interest is " + numberOfMonths);

         //no interest within a month
        if(numberOfMonths==0){
            throw new BankingException ("Invalid date less than a month to compute interest for Saving account name " +accountName);
        }
        
        //compute, output and update last interest date
        double interestEarned = (double) numberOfMonths / 12.0 *accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned); 
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return(accountBalance);                            
    }  	
}

/*
 *  Derived class: CDAccount
 *
 *  Description:
 *      Interest is computed monthly; there's 250 fee for
 *      withdraw fee within a year;
 *      you can't deposit anything within a year; 
 *      there is a minimum balance of $0.
 */

class CDAccount extends Account implements FullFunctionalAccount {
    //to store rest month which can be computed
    int restMonth=12;

    //constructor
	CDAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        openDate = new Date();
        lastInterestDate = openDate;	
    }
    
    //constructor
    CDAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        openDate = firstDate;
        lastInterestDate = openDate;	
    }

    //function to deposit money
    public double deposit(double amount, Date deposiDate) throws BankingException {
        if (deposiDate.getTime()<=openDate.getTime()+86400000.0*365)
            throw new BankingException ("Cannot deposit in CD account before 1 year: " +accountName);
        
        
        else {
            accountBalance += amount;	
            return(accountBalance); 	
        }            
     }

    //function to withdraw money
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        // minimum balance is 0, raise exception if violated
        if((accountBalance  - amount)<0) 
            throw new BankingException ("Underfraft from CD account name: " + accountName);

        if (withdrawDate.getTime()<=openDate.getTime()+86400000.0*365){
            // minimum balance is 0, raise exception if violated
            if((accountBalance  - amount-250)<0) 
                throw new BankingException ("Underfraft from CD account name: " + accountName);
            accountBalance -= 250;
        }
            
        accountBalance -= amount;
        return(accountBalance); 	                                     	
    }
    
    //function to compute interest 
    public double computeInterest (Date interestDate) throws BankingException {
        //raise exception if compute date is invalid
        if (interestDate.before(lastInterestDate)) 
            throw new BankingException ("Invalid date to compute interest for CD account name " + accountName);
                          	
        //no interest after a year
        if (restMonth==0)
            throw new BankingException ("The interest payments stop after a year for CD account name " + accountName);
        
        int numberOfMonths = (int) ((interestDate.getTime() - lastInterestDate.getTime()) / (86400000.0*30));

        //compute the rest month can be computed
        if(restMonth-numberOfMonths<0){
            numberOfMonths=restMonth;
            restMonth=0;
        }
        else{
            restMonth-=numberOfMonths;
        }
        
        System.out.println("Number of months since last interest is " + numberOfMonths);

        //no interest within a month
        if(numberOfMonths==0)
            throw new BankingException ("Invalid date less than a month to compute interest for CD account name " +accountName);

        //compute, output and update last interest date
        double interestEarned = (double) numberOfMonths / 12.0 *accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned); 
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return(accountBalance);                            
    }  	
}

/*
 *  Derived class: LoanAccount
 *
 *  Description:
 *      Interest is computed daily; there's no fee for
 *      withdraw; there is a minimum balance of $0;no
 *      money increase if balance is positive.
 */

class LoanAccount extends Account implements FullFunctionalAccount {
    //constructor
	LoanAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        openDate = new Date();
        lastInterestDate = openDate;	
    }
    
    //constructor
    LoanAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        openDate = firstDate;
        lastInterestDate = openDate;	
    }	

    //function to deposit money
    public double deposit(double amount, Date depositDate) throws BankingException {
        accountBalance += amount;
        return accountBalance;
    }

    //function to withdraw money
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        // minimum balance is 0, raise exception if violated
        if((accountBalance  - amount)<0)  throw new BankingException ("Underfraft from Loan account name: " + accountName);

        accountBalance -= amount;
        return(accountBalance);
    }
    
    //function to compute interest 
    public double computeInterest (Date interestDate) throws BankingException {
        //raise exception if compute date is invalid
        if (interestDate.before(lastInterestDate)) 
            throw new BankingException ("Invalid date to compute interest for Loan account name " + accountName);                            	

        
        //compute and output
        int numberOfDays = (int) ((interestDate.getTime() - lastInterestDate.getTime())/ 86400000.0);
        System.out.println("Number of days since last interest is " + numberOfDays);
        double interestEarned = (double) numberOfDays / 365.0 *accountInterestRate * accountBalance;

        //if balance not negative, interest is 0
        if(accountBalance>0) interestEarned=0;

        //output and update last interest date
        System.out.println("Interest earned is " + interestEarned); 
        lastInterestDate = interestDate;
        accountBalance += interestEarned;

        return(accountBalance);                            
    }  	
}