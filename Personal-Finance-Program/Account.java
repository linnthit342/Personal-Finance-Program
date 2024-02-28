/*

 * Date: 01-12-2023
 * Description: 
 */

import java.text.DecimalFormat;

public class Account {
    private double rate; // Interest rate for the account
    private int numberOfWeeks; // Number of weeks for the investment
    private double amount; // Amount invested per week
    private double investment = 0; // Current investment balance

    // Default constructor
    public Account() {
        this.rate = 0.0;
        this.numberOfWeeks = 0;
        this.amount = 0.0;
    }

    // Constructor with parameters
    public Account(double rate, int numberOfWeeks, double amount) {
        this.rate = rate;
        this.numberOfWeeks = numberOfWeeks;
        this.amount = amount;
    }

    // Accessor methods
    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getNumberOfWeeks() {
        return numberOfWeeks;
    }

    public void setNumberOfWeeks(int numberOfWeeks) {
        this.numberOfWeeks = numberOfWeeks;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Method to calculate remaining amount available to invest
    public double calcRemainingAmount(double netSalary, double expenses) {
        return netSalary - expenses - investment;
    }

    // Method to calculate investment growth over a specified number of weeks
    public String calcInvestment() {
        StringBuilder result = new StringBuilder();

        double weeklyRate = rate / 100 / 52; // Weekly interest rate
        double monthlyRate = 1 + weeklyRate * 4; // Monthly growth factor

        // Header for the investment details
        result.append(String.format("\n%-12s%-12s%n", "Weeks", "Balance"));
        result.append("\n--------------------\n");

        // Loop to calculate investment balance for each 4-week period
        for (int i = 4; i <= numberOfWeeks; i += 4) {
            investment = (investment + amount * 4) * monthlyRate;
            result.append(String.format("%-12d$%-12.2f%n", i, investment));
        }

        // Calculate and append the remaining weeks if the total weeks are not a
        // multiple of 4
        if (numberOfWeeks % 4 != 0) {
            investment = investment + amount * (numberOfWeeks % 4);
            result.append(String.format("%-12d$%-12.2f%n", numberOfWeeks, investment));
        }

        return result.toString();
    }
}