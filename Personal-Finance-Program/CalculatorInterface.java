/*

 * Date: 01-12-2023
 * Description: 
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*This CalculatorInterface class provides a Terminal IO for managing client financial details.*/
public class CalculatorInterface {
    private Client[] clients; // Array to store Client objects
    private Scanner scanner; // Scanner for user input
    private int numberOfClients; // Counter to track the number of clients

    /* Constructor for CalculatorInterface initializes class variables. */
    public CalculatorInterface() {
        this.clients = new Client[7]; // Initialize the array to store up to 7 clients
        this.scanner = new Scanner(System.in);
        this.numberOfClients = 0; // Initialize the number of clients to 0
    }

    /* Adds a new client to the program */
    private void addClient() {
        if (numberOfClients >= 7) {
            System.out.println("Cannot add more clients. Maximum limit reached.");
            return;
        }

        String name;
        double grossSalary;
        boolean isResident;
        double weeklyExpenses;

        // Validation for client details input
        while (true) {
            System.out.print("Enter client's full name (first name last name): ");
            name = scanner.nextLine();

            // Validate name format
            if (name.split(" ").length < 2) {
                System.out.println("Invalid name format. Please provide at least two names.");
                continue;
            }

            // Check if a client with the same name already exists
            boolean nameExists = false;
            for (int i = 0; i < numberOfClients; i++) {
                if (clients[i].getName().equalsIgnoreCase(name)) {
                    nameExists = true;
                    break;
                }
            }

            // if client already exists, asks again
            if (nameExists) {
                System.out.println("Client with this name already exists. Please choose a different name.");
                continue;
            }

            // validation for income
            while (true) {
                System.out.print("Enter client's annual income/salary: $");
                while (true) {
                    // Check if valid data type
                    if (!scanner.hasNextDouble()) {
                        System.out.println("Invalid input. Please enter a valid salary amount.");
                        scanner.next();
                        System.out.print("Enter client's annual income/salary: $");
                    } else {
                        break;
                    }
                }
                grossSalary = scanner.nextDouble();
                scanner.nextLine();

                // validate positive amount
                if (grossSalary <= 0) {
                    System.out.println("Invalid salary. Please enter a positive salary amount.");
                    continue;
                }

                // check resident or not
                while (true) {
                    System.out.print("Is the client a resident? (Yes/No): ");
                    String residentChoice = scanner.nextLine().trim().toLowerCase();
                    if (residentChoice.equals("yes") || residentChoice.equals("no")
                            || residentChoice.equals("y") || residentChoice.equals("n")) {
                        isResident = residentChoice.equals("yes") || residentChoice.equals("y");
                        break;
                    } else {
                        System.out.println("Invalid input. Please answer Yes or No.");
                    }
                }

                // validate weekly expenses
                while (true) {
                    System.out.print("Enter client's weekly living expenses: $");

                    while (true) {
                        if (!scanner.hasNextDouble()) {
                            System.out.println("Invalid input. Please enter a valid living expenses amount.");
                            scanner.next(); // Clear the invalid input
                            System.out.print("Enter client's weekly living expenses: $");
                        } else {
                            break;
                        }
                    }
                    weeklyExpenses = scanner.nextDouble();
                    scanner.nextLine();
                    // check if valid expenses or not
                    if (weeklyExpenses <= 0 || weeklyExpenses >= grossSalary) {
                        System.out.println(
                                "Invalid living expenses amount. Please enter a positive amount less than the net salary.");
                        continue;
                    }

                    // Create a new client
                    Client newClient = new Client(name, null, grossSalary, isResident, weeklyExpenses);

                    // Calculate tax and Medicare
                    newClient.calcTax();
                    newClient.calcMedicare();

                    // Add the new client to the array
                    clients[numberOfClients] = newClient;
                    numberOfClients++;

                    System.out.println("Client added successfully.");
                    break;
                }
                break;
            }
            break;
        }
    }

    // Deletes an existing client from program
    private void deleteClient() {
        System.out.print("Enter the client name to delete: ");
        String nameToDelete = scanner.nextLine().trim();

        int indexToDelete = -1;

        // Search for Client to Delete
        for (int i = 0; i < numberOfClients; i++) {
            // Check if the name of the current client matches the name entered by the user.
            if (clients[i].getName().equalsIgnoreCase(nameToDelete)) {
                indexToDelete = i; // If a match is found, record the index of the client to delete (i)
                break;
            }
        }

        if (indexToDelete != -1) {
            // Client found, delete and shift array elements
            System.arraycopy(clients, indexToDelete + 1, clients, indexToDelete, numberOfClients - indexToDelete - 1);
            clients[numberOfClients - 1] = null;
            numberOfClients--; // decrease the count of total clients since one client is deleted.
            System.out.println("The client was deleted.");
        } else {
            System.out.println("The client does not exist.");
        }
    }

    // method to display client
    private void displayClient() {
        System.out.print("Enter the client name to display: ");
        String nameToDisplay = scanner.nextLine().trim();

        // Iterate through the clients array to find the specified client
        for (int i = 0; i < numberOfClients; i++) {
            if (clients[i].getName().equalsIgnoreCase(nameToDisplay)) {
                displayClientDetails(clients[i]); // If the client is found, display detailed information

                // After displaying client details, offer additional options to the user
                while (true) {
                    System.out.println("\nOptions:");
                    System.out.println("1. Back to Main Menu");
                    System.out.println("2. Delete Client");
                    System.out.println("3. Add an Account");
                    System.out.println("4. Display Account");
                    System.out.println("5. Delete Account");
                    System.out.print("Enter your choice (1-5): ");

                    int choice;
                    while (true) {
                        try {
                            choice = Integer.parseInt(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            System.out.print("Enter your choice (1-5): ");
                        }
                    }

                    switch (choice) {
                        case 1:
                            return; // Return to the main menu
                        case 2:
                            deleteClient();
                            return; // Return to the main menu after deleting the client
                        case 3:
                            addAccount();
                            break;
                        case 4:
                            displayAccount();
                            break;
                        case 5:
                            deleteAccount();
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                    }
                }
            }
        }

        System.out.println("The client does not exist."); // If the specified client is not found, display a message
    }

    // display all clients in the program
    private void displayAllClients() {
        if (numberOfClients == 0) {
            System.out.println("No clients."); // if there is no client, return to Options
            return;
        }

        // to display each client in the array
        for (int i = 0; i < numberOfClients; i++) {
            System.out.println("Client " + (i + 1) + ":");

            // Display common client details
            System.out.println("  - Name: " + clients[i].getName());
            System.out.println("  - Resident: " + (clients[i].isResident() ? "Yes" : "No"));
            System.out.println(
                    "  - Gross salary (per week): $" + String.format("%.2f", clients[i].getGrossSalaryPerWeek()));
            System.out
                    .println("  - Net salary (per week): $" + String.format("%.2f", clients[i].getNetSalaryPerWeek()));
            System.out.println("  - Tax (per week): $" + String.format("%.2f", clients[i].getTax() / 52));
            System.out.println("  - Medicare (per week): $" + String.format("%.2f", clients[i].getMedicarePerWeek()));
            System.out.println("  - Expenses (per week): $" + String.format("%.2f", clients[i].getweeklyExpenses()));

            // Display accounts information
            int numberOfAccounts = clients[i].getNumberOfAccounts();
            if (numberOfAccounts == 0) {
                System.out.println("  - No accounts");
            } else {
                System.out.println("  - Number of Accounts: " + numberOfAccounts);

            }

            System.out.println(); // Add a line break between clients
        }
    }

    // method to display detailed information for a specific client
    private void displayClientDetails(Client client) {
        System.out.println("Name: " + client.getName());

        // display gross salary
        System.out.println("Gross salary (per year): $" + String.format("%.2f", client.getGrossSalary()));
        System.out.println("Gross salary (per week): $" + String.format("%.2f", client.getGrossSalaryPerWeek()));

        // display resident
        System.out.println("Resident: " + (client.isResident() ? "Yes" : "No"));

        // display tax
        System.out.println("Tax (per year): $" + String.format("%.2f", client.getTax()));
        System.out.println("Tax (per week): $" + String.format("%.2f", client.getTax() / 52));

        // display net salary
        System.out.println("Net salary (per year): $" + String.format("%.2f", client.getNetSalaryPerYear()));
        System.out.println("Net salary (per week): $" + String.format("%.2f", client.getNetSalaryPerWeek()));

        // display medicare
        System.out.println("Medicare (per year): $" + String.format("%.2f", client.getMedicare()));
        System.out.println("Medicare (per week): $" + String.format("%.2f", client.getMedicarePerWeek()));

        // display expenses per week (assuming expenses are the same for each week)
        System.out.println("Expenses (per week): $" + String.format("%.2f", client.getweeklyExpenses()));

        // display remaining amount to invest per week
        double remainingAmountToInvest = client.getRemainingAmountToInvest(); // get ramaining amount from client class
        System.out.println(
                "Remaining Amount Available to Invest Per Week: $" + String.format("%.2f", remainingAmountToInvest));

        // display account information
        int numberOfAccounts = client.getNumberOfAccounts();
        if (numberOfAccounts == 0) {
            System.out.println("No accounts"); // if no account,show this message
        } else {
            System.out.println("Number of Accounts: " + numberOfAccounts); // if there is any, display the number of
                                                                           // accounts
            // display each account of a client
            for (int i = 0; i < numberOfAccounts; i++) {
                Account account = client.getAccount(i); // get the i-th account of the client

                // Display details for the current account
                System.out.println("Account " + (i + 1) + " Details:");
                System.out.println("  - Amount invested per week: $" + String.format("%.2f", account.getAmount()));
                System.out.println("  - Interest Rate: " + String.format("%.2f", account.getRate()) + "%");
                System.out.println("  - Number of weeks: " + account.getNumberOfWeeks());
                System.out.println("  - Total Amount at the End: $" + account.calcInvestment());
            }
        }
    }

    // Method to add an account to an existing client
    private void addAccount() {
        // Prompt the user to enter the name of the client to add an account
        System.out.print("Enter the client name to add an account: ");
        String nameToAddAccount = scanner.nextLine().trim();

        int indexToAddAccount = -1;

        // Find the index of the client with the specified name
        for (int i = 0; i < numberOfClients; i++) {
            if (clients[i].getName().equalsIgnoreCase(nameToAddAccount)) {
                indexToAddAccount = i;
                break;
            }
        }

        // Check if the client with the given name exists
        if (indexToAddAccount != -1) {
            Client client = clients[indexToAddAccount];

            // Check if the client has reached the maximum limit of accounts
            if (client.getNumberOfAccounts() >= 3) {
                System.out.println("Cannot add more accounts. Maximum limit reached for this client.");
            } else {
                double remainingAmountToInvest = client.getRemainingAmountToInvest();

                // Check if there is remaining amount available for investment
                if (remainingAmountToInvest > 0) {
                    // Prompt the user to enter investment details
                    System.out.print(
                            "Enter investment value per week (max $" + String.format("%.2f", remainingAmountToInvest)
                                    + "): $");

                    double investmentValue;
                    // Validate and get the investment value from the user
                    while (true) {
                        try {
                            investmentValue = Double.parseDouble(scanner.nextLine());
                            if (investmentValue <= 0 || investmentValue > remainingAmountToInvest) {
                                throw new NumberFormatException();
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid investment value.");
                            System.out.print("Enter investment value (max $"
                                    + String.format("%.2f", remainingAmountToInvest) + "): $");
                        }
                    }

                    // Prompt the user to enter interest rate
                    System.out.print("Enter interest rate (between 5% and 10%): ");
                    double interestRate;
                    // Validate and get the interest rate from the user
                    while (true) {
                        try {
                            interestRate = Double.parseDouble(scanner.nextLine());
                            if (interestRate < 5 || interestRate > 10) {
                                throw new NumberFormatException();
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid interest rate between 5% and 10%.");
                            System.out.print("Enter interest rate (between 5% and 10%): ");
                        }
                    }

                    // Prompt the user to enter investment length
                    System.out.print("Enter investment length (positive and non-zero): ");
                    int investmentLength;
                    // Validate and get the investment length from the user
                    while (true) {
                        try {
                            investmentLength = Integer.parseInt(scanner.nextLine());
                            if (investmentLength <= 0) {
                                throw new NumberFormatException();
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid investment length.");
                            System.out.print("Enter investment length (positive and non-zero): ");
                        }
                    }

                    // Create a new account and add it to the client
                    Account account = new Account(interestRate, investmentLength, investmentValue);
                    client.addAccount(account);

                    System.out.println("Account added successfully.");
                } else {
                    System.out.println("Cannot add an account. No remaining amount available to invest.");
                }
            }
        } else {
            System.out.println("The client does not exist.");
        }
    }

    // Method to prompt the user to enter a client name and display their account
    // details
    private void displayAccount() {
        // ask the user to enter the client name to display account
        System.out.print("Enter the client name to display account: ");
        String nameToDisplay = scanner.nextLine().trim();

        // Iterate through the clients to find the matching client by name
        for (int i = 0; i < numberOfClients; i++) {
            if (clients[i].getName().equalsIgnoreCase(nameToDisplay)) {
                // Call the displayAccountNumber method to show account details for the found
                // client
                displayAccountNumber(clients[i]);
                return;
            }
        }

        // Display an error message if the client with the entered name does not exist
        System.out.println("The client does not exist.");
    }

    // Method to display account details for a specific account number of a given
    // client
    private void displayAccountNumber(Client client) {
        // Check if the client has any accounts
        if (client.getNumberOfAccounts() == 0) {
            System.out.println("The client has no accounts."); // if no account, go back to Options
            return;
        }

        // Prompt the user to enter the account number to display
        System.out.println("Enter the account number to display (1, 2, or 3): ");
        int accountNumber;
        while (true) {
            try {
                // Validate and get the account number from the user
                accountNumber = Integer.parseInt(scanner.nextLine());
                if (accountNumber < 1 || accountNumber > client.getNumberOfAccounts()) {
                    throw new NumberFormatException();
                }
                break;
            } catch (NumberFormatException e) {
                // Handle invalid input and prompt the user again
                System.out.println("Invalid input. Please enter a valid account number.");
                System.out.print("Enter the account number to display (1, 2, or 3): ");
            }
        }

        // Display the client's name and call the displayAccountDetails method to show
        // account details
        System.out.println("Client: " + client.getName());
        Account account = client.getAccount(accountNumber - 1);
        System.out.println("Account " + accountNumber + " Details:");

        // Call the displayAccountDetails method here
        displayAccountDetails(account);
    }

    // Method to display details of a specific account
    private void displayAccountDetails(Account account) {
        // Display details of the provided account
        System.out.println("  - Amount invested per week: $" + String.format("%.2f", account.getAmount()));
        System.out.println("  - Interest rate: " + String.format("%.2f", account.getRate()) + "%");
        System.out.println("  - Number of weeks: " + account.getNumberOfWeeks());
        System.out.println("  - Total amount at the end of the period: $" + account.calcInvestment());
    }

    // Method to delete an account for a specific client
    private void deleteAccount() {
        // Prompt the user to enter the client name to delete an account
        System.out.print("Enter the client name to delete account: ");
        String nameToDelete = scanner.nextLine().trim();

        int indexToDelete = -1;

        // Iterate through the clients to find the matching client by name
        for (int i = 0; i < numberOfClients; i++) {
            if (clients[i].getName().equalsIgnoreCase(nameToDelete)) {
                indexToDelete = i;
                break;
            }
        }

        // Check if the client exists
        if (indexToDelete != -1) {
            Client client = clients[indexToDelete];

            // Check if the client has any accounts
            if (client.getNumberOfAccounts() == 0) {
                System.out.println("There is no account for this client.");
                return; // Return to main options
            }

            // Prompt the user to enter the account number to delete
            System.out.print("Enter account number to delete (1, 2, or 3): ");
            int accountNumber;
            while (true) {
                try {
                    // Validate and get the account number from the user
                    accountNumber = Integer.parseInt(scanner.nextLine());
                    if (accountNumber < 1 || accountNumber > client.getNumberOfAccounts()) {
                        throw new NumberFormatException();
                    }
                    break;
                } catch (NumberFormatException e) {
                    // Handle invalid input and prompt the user again
                    System.out.println("Invalid input. Please enter a valid account number.");
                    System.out.print("Enter account number to delete (1, 2, or 3): ");
                }
            }

            // Call the deleteAccount method of the client to remove the specified account
            client.deleteAccount(accountNumber - 1);
        } else {
            // Display an error message if the client with the entered name does not exist
            System.out.println("The client does not exist.");
        }
    }

    // Method to save content to a file
    private void saveToFile(String fileName, String content) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            // Write the content to the specified file
            fileWriter.write(content);
            System.out.println("Data successfully saved to " + fileName);
        } catch (IOException e) {
            // Handle exceptions in case of an error during file write
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    // Method to convert client details to a formatted string
    private String getClientDetailsAsString(Client client) {
        // Use a StringBuilder to efficiently build the client details string
        StringBuilder clientDetails = new StringBuilder();

        // Append client details to the StringBuilder
        clientDetails.append("Client Name: ").append(client.getName()).append("\n");
        clientDetails.append("Gross Salary (per year): $").append(String.format("%.2f", client.getGrossSalary()))
                .append("\n");
        clientDetails.append("Net Salary (per year): $").append(String.format("%.2f", client.getNetSalary()))
                .append("\n");
        clientDetails.append("Tax (per year): $").append(String.format("%.2f", client.getTax())).append("\n");
        clientDetails.append("Medicare (per year): $").append(String.format("%.2f", client.getMedicare())).append("\n");
        clientDetails.append("Expenses (per week): $").append(String.format("%.2f", client.getweeklyExpenses()))
                .append("\n");

        // Append account details for each client account
        int numberOfAccounts = client.getNumberOfAccounts();
        clientDetails.append("Number of Accounts: ").append(numberOfAccounts).append("\n");
        for (int i = 0; i < numberOfAccounts; i++) {
            Account account = client.getAccount(i);
            clientDetails.append("Account ").append(i + 1).append(" Details:\n");
            clientDetails.append("  - Amount invested per week: $").append(String.format("%.2f", account.getAmount()))
                    .append("\n");
            clientDetails.append("  - Interest Rate: ").append(String.format("%.2f", account.getRate())).append("%\n");
            clientDetails.append("  - Number of weeks: ").append(account.getNumberOfWeeks()).append("\n");
            clientDetails.append("  - Total Amount at the End: $").append(account.calcInvestment()).append("\n");
        }

        // Return the formatted client details as a string
        return clientDetails.toString();
    }

    // Method to save details of all clients to a file
    private void saveAllClientsToFile() {
        // Check if there are no clients
        if (numberOfClients == 0) {
            // Save a message indicating no clients to the specified file
            saveToFile("all_clients_details.txt", "No clients");
        } else {
            // Use StringBuilder to efficiently concatenate details of all clients
            StringBuilder allClientsDetails = new StringBuilder();
            for (int i = 0; i < numberOfClients; i++) {
                // Append details of each client to the StringBuilder
                allClientsDetails.append(getClientDetailsAsString(clients[i])).append("\n");
            }
            // Save the concatenated details of all clients to the specified file
            saveToFile("all_clients_details.txt", allClientsDetails.toString());
        }
    }

    // Main program loop for user interaction
    private void run() {
        while (true) {
            // Display user options
            System.out.println("Choose an option:");
            System.out.println("1. Add Client");
            System.out.println("2. Delete Client");
            System.out.println("3. Display a Client");
            System.out.println("4. Display all Clients");
            System.out.println("5. Add an Account");
            System.out.println("6. Display Account");
            System.out.println("7. Delete Account");
            System.out.println("8. Save to a text File");
            System.out.println("9. Exit");
            System.out.print("Enter your choice (1-9): ");

            int choice;
            while (true) {
                try {
                    // Get user choice, handling non-integer inputs
                    choice = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    System.out.print("Enter your choice (1-8): ");
                }
            }

            // Perform actions based on user choice
            switch (choice) {
                case 1:
                    addClient();
                    break;
                case 2:
                    deleteClient();
                    break;
                case 3:
                    displayClient();
                    break;
                case 4:
                    displayAllClients();
                    break;
                case 5:
                    addAccount();
                    break;
                case 6:
                    displayAccount();
                    break;
                case 7:
                    deleteAccount();
                    break;
                case 8:
                    saveAllClientsToFile();
                    break;
                case 9:
                    System.out.println("Thank you! Have a great day.");
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 8.");
            }
        }
    }

    public static void main(String[] args) {
        CalculatorInterface calculatorInterface = new CalculatorInterface();
        calculatorInterface.run();
    }
}