
/*****************************************************************
    CS4001301 Programming Languages                   
    
    Programming Assignment #1
    
    Java programming using subtype, subclass, and exception handling
    
    To compile: %> javac Application.java
    
    To execute: %> java Application

******************************************************************/

import java.util.*;



public class Application {
	
public static void main( String args [])  {
    Account a;
    double ret;
    
    a = new CheckingAccount("John Smith", 1500.0);
    
    try {
    	ret = a.withdraw(100.00);
    	System.out.println ("Account <" + a.name() + "> now has $" + ret + " balance\n");
    } catch (Exception e) {
       stdExceptionPrinting(e, a.balance());	
    }
    
    a = new CheckingAccount("John Smith", 1500.0);
    
    try {
    	ret = a.withdraw(600.00);
    	System.out.println ("Account <" + a.name() + "> now has $" + ret + " balance\n");
    } catch (Exception e) {
       stdExceptionPrinting(e, a.balance());	
    }
    
    /* put your own tests here ....... */
	// if your implementaion is correct, you can do the following with polymorphic array accountList
    
    

    // initial variables
	Account[] accountList;
	int withdrawDollar=1000;
    int depositDollar=2000;
    Date currentDate;
    Calendar currentCal;
	accountList = new Account[4];
		
	// buid 4 different accounts in the same array
	accountList[0] = new CheckingAccount("John Smith", 1500.0);
	accountList[1] = new SavingAccount("William Hurt", 1500.0);
	accountList[2] = new CDAccount("Woody Allison", 1500.0);
	accountList[3] = new LoanAccount("Judi Foster", -2500.0);

    //set time and time zone
    currentDate=new Date();
    currentCal = Calendar.getInstance();
    currentCal.setTime(currentDate);

	// compute interest for all accounts
	for (int count = 0; count < accountList.length; count++) {
        System.out.println("");

        // to clarify which the current account is and it's status
        System.out.print ("Account <" +accountList[count].name() + "> is ");
        if(count==0) System.out.println("Checking Account");
        else if(count==1) System.out.println("Saving Account");
        else if(count==2) System.out.println("CD Account");
        else if(count==3) System.out.println("Loan Account");
		accountList[count].status();

        //test for minimum balance 1000 in checking account
        System.out.println ("Withdraw dollars test");
        withdrawTest(accountList[count], withdrawDollar, 0, currentDate, currentCal);

        //test for no less than 0 balance in other accounts
        System.out.println ("Withdraw dollars test2");
        withdrawTest(accountList[count], withdrawDollar, 0, currentDate, currentCal);

        //test for cd account unable to deposit money until a year later
        System.out.println ("deposit dollars test");
        depositTest(accountList[count],depositDollar,0, currentDate, currentCal);

        //test for input invalid date in each account
        System.out.println ("invalid date test");
        computeInterestTest(accountList[count], "for a day", -1, currentDate, currentCal);

        //test for input daily date in each account
        System.out.println ("compute interest's daily test");
        computeInterestTest(accountList[count], "for a day", 1, currentDate, currentCal);

        //test for input monthly date in each account
        System.out.println ("compute interest's monthly test");
        computeInterestTest(accountList[count], "for a month", 30, currentDate, currentCal);

        //test for transaction fee in saving account
        if(count==1){
            System.out.println ("transaction fee in saving account's test after three transactions");
            depositTest(accountList[count],depositDollar,0, currentDate, currentCal);
            depositTest(accountList[count],depositDollar,0, currentDate, currentCal);

            //reset transaction fee after a month
            System.out.println("After above one month, reset transaction times");
            withdrawTest(accountList[count], withdrawDollar, 60, currentDate, currentCal);
        }
        //test for deposit money after a year in cd account
        if(count==2){
            System.out.println ("compute interest in CD account's test in a year");
            computeInterestTest(accountList[count],"in a year",365, currentDate, currentCal);
            System.out.println ("no more interest in CD account's test after a year");
            computeInterestTest(accountList[count],"after a year",395, currentDate, currentCal);
            System.out.println ("deposit money in CD account's test after a year");
            depositTest(accountList[count],depositDollar,365, currentDate, currentCal);
            System.out.println ("no withdraw fee in CD account's test after a year");
            withdrawTest(accountList[count],withdrawDollar,365, currentDate, currentCal);
        }
        //test for deposit money and no interest if asset is positive in loan account
        if(count==3){
            //test for deposit money if asset is positive
            System.out.println ("deposit money in loan account's test if the asset is positive");
            depositTest(accountList[count],depositDollar,0, currentDate, currentCal);
            withdrawTest(accountList[count], withdrawDollar, 0, currentDate, currentCal);
            
            //test for no interest if asset is positive
            System.out.println ("no compute balance's test if the asset is positive");
            computeInterestTest(accountList[count], "for a month", 60, currentDate, currentCal);
        }

        System.out.println ("");
	}
}

// function for deposit test
static void depositTest(Account account,int depositDollar,int datePass,Date currentDate,Calendar currentCal){
    //set time and time zone
    Date newDate=currentDate;
    currentCal.add(Calendar.DATE, datePass);
    newDate = currentCal.getTime();

    //deposit money test
    try {
        account.depositMessage(depositDollar);
	    account.deposit(depositDollar,newDate);
    	account.status();
    } catch (Exception e) {
        stdExceptionPrinting(e, account.balance());	
    }
    currentCal.add(Calendar.DATE, -datePass);
}

// function for withdraw test
static void withdrawTest(Account account,int withdrawDollar,int datePass,Date currentDate,Calendar currentCal){
    //set time and time zone
    Date newDate=currentDate;
    currentCal.add(Calendar.DATE, datePass);
    newDate = currentCal.getTime();

    //withdraw money test
    try {
        account.withdrawMessage(withdrawDollar);
    	account.withdraw(withdrawDollar,newDate);
    	account.status();
    } catch (Exception e) {
        stdExceptionPrinting(e, account.balance());	
    }
    currentCal.add(Calendar.DATE, -datePass);
}

// function for compute interest test
static void computeInterestTest(Account account,String message,int datePass,Date currentDate,Calendar currentCal){
    //set time and time zone
    Date newDate=currentDate;
    currentCal.add(Calendar.DATE, datePass);
    newDate = currentCal.getTime();

    //compute interest money test
    try {
        account.computeMessage(message);
	    account.computeInterest(newDate);
        account.status();
    } catch (Exception e) {
        stdExceptionPrinting(e, account.balance());	
    }
    currentCal.add(Calendar.DATE, -datePass);
}


// exception printing
static void stdExceptionPrinting(Exception e, double balance) {
    System.out.println("EXCEPTION: Banking system throws a " + e.getClass() +
                       " with message: \n\t" +
                       "MESSAGE: " + e.getMessage());
    System.out.println("\tAccount balance remains $" + balance + "\n");
}
}          