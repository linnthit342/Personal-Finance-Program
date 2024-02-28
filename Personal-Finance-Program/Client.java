/*

 * Date: 01-12-2023
 * Description: 
 */

import java.util.Scanner;

public class Client {
    private String name;
    private Account[] accounts;
    private double grossSalary;
    private boolean resident;
    private double tax;
    private double medicare;
    private double weeklyExpenses;
    private int numberOfAccounts;

    // Constructor to initialize a client with basic details and an optional saving
    // account
    public Client(String name, Account savingAccount, double grossSalary, boolean resident, double weeklyExpenses) {
        this.name = name;
        this.accounts = new Account[3]; // Maximum of 3 accounts for a client
        this.grossSalary = grossSalary;
        this.resident = resident;
        this.weeklyExpenses = weeklyExpenses;
        this.numberOfAccounts = 0;

        if (savingAccount != null) {
            addAccount(savingAccount); // Add the provided saving account during initialization
        }
    }

    // Method to calculate income tax based on the provided tax brackets
    public void calcTax() {
        double taxableIncome = grossSalary;
        double annualTax = 0.0;

        if (resident) {
            // Calculate tax based on residency and income brackets
            if (taxableIncome <= 18200) {
                annualTax = 0.0;
            } else if (taxableIncome <= 45000) {
                annualTax = (taxableIncome - 18200) * 0.19;
            } else if (taxableIncome <= 120000) {
                annualTax = (45000 - 18200) * 0.19 + (taxableIncome - 45000) * 0.325;
            } else if (taxableIncome <= 180000) {
                annualTax = (45000 - 18200) * 0.19 + (120000 - 45000) * 0.325 + (taxableIncome - 120000) * 0.37;
            } else {
                annualTax = (45000 - 18200) * 0.19 + (120000 - 45000) * 0.325 + (180000 - 120000) * 0.37
                        + (taxableIncome - 180000) * 0.45;
            }
        } else {
            // Calculate tax for non-residents based on income brackets
            if (taxableIncome <= 120000) {
                annualTax = taxableIncome * 0.325;
            } else if (taxableIncome <= 180000) {
                annualTax = 120000 * 0.325 + (taxableIncome - 120000) * 0.37;
            } else {
                annualTax = 120000 * 0.325 + 180000 * 0.37 + (taxableIncome - 180000) * 0.45;
            }
        }

        tax = annualTax;
    }

    // Method to calculate Medicare levy based on residency and income
    public void calcMedicare() {
        if (resident && grossSalary > 29032) {
            medicare = grossSalary * 0.02;
        } else {
            medicare = 0.0;
        }
    }

    // Getter methods for various financial details
    public double getNetSalary() {
        return grossSalary - tax - medicare;
    }

    public double getNetSalaryPerYear() {
        return getNetSalary();
    }

    public double getNetSalaryPerWeek() {
        return getNetSalary() / 52;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public double getGrossSalaryPerWeek() {
        return getGrossSalary() / 52;
    }

    public double getTax() {
        return tax;
    }

    public boolean isResident() {
        return resident;
    }

    public double getMedicare() {
        return medicare;
    }

    public double getMedicarePerWeek() {
        return medicare / 52;
    }

    public String getName() {
        return name;
    }

    public double getweeklyExpenses() {
        return weeklyExpenses;
    }

    public int getNumberOfAccounts() {
        return numberOfAccounts;
    }

    // Method to get a specific account by index
    public Account getAccount(int index) {
        if (index >= 0 && index < numberOfAccounts) {
            return accounts[index];
        }
        return null;
    }

    // Method to delete an account by shifting array elements
    public void deleteAccount(int accountNumber) {
        if (accountNumber >= 0 && accountNumber < numberOfAccounts) {
            System.arraycopy(accounts, accountNumber + 1, accounts, accountNumber,
                    numberOfAccounts - accountNumber - 1);
            accounts[numberOfAccounts - 1] = null;
            numberOfAccounts--;
            System.out.println("Account " + (accountNumber + 1) + " was deleted.");
        } else {
            System.out.println("Invalid account number. Please enter a valid account number.");
        }
    }

    // Method to calculate the remaining amount available to invest after
    // considering existing accounts
    public double getRemainingAmountToInvest() {
        double totalInvestment = 0.0;

        for (int i = 0; i < numberOfAccounts; i++) {
            totalInvestment += accounts[i].getAmount();
        }

        return getNetSalaryPerWeek() - weeklyExpenses - totalInvestment;
    }

    // Method to add a new account if the maximum limit is not reached
    public void addAccount(Account account) {
        if (numberOfAccounts < 3) {
            accounts[numberOfAccounts] = account;
            numberOfAccounts++;
        }
    }

}