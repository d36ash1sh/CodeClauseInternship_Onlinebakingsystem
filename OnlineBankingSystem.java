import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

// User class to hold user information
class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

// Transaction class to hold transaction details
class Transaction {
    private String accountNumber;
    private double amount;
    private LocalDateTime timestamp;
    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER

    public Transaction(String accountNumber, double amount, String type) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return timestamp + ": " + type + " of $" + amount + " on account " + accountNumber;
    }
}

// Account class to manage account information
class Account {
    private String accountNumber;
    private double balance;
    private List<Transaction> transactionHistory;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(new Transaction(accountNumber, amount, "DEPOSIT"));
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        transactionHistory.add(new Transaction(accountNumber, amount, "WITHDRAWAL"));
        return true;
    }

    public boolean transfer(Account toAccount, double amount) {
        if (this.withdraw(amount)) { 
            toAccount.deposit(amount);
            transactionHistory.add(new Transaction(toAccount.getAccountNumber(), amount, "TRANSFER"));
            return true;
        }
        return false;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}

// BankingSystem class to manage users and accounts
class BankingSystem {
    private HashMap<String, User> users = new HashMap<>();
    private HashMap<String, Account> accounts = new HashMap<>();

    public void register(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password));
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Username already exists!");
        }
    }

    public void createAccount(String username) {
        String accountNumber = "ACC" + (accounts.size() + 1);
        accounts.put(accountNumber, new Account(accountNumber));
        System.out.println("Account created: " + accountNumber);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }
}

// Main class for user interaction
public class OnlineBankingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankingSystem bankingSystem = new BankingSystem();

        System.out.println("Welcome to the Online Banking System!");

        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                bankingSystem.register(username, password);
            } else if (choice == 2) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                if (bankingSystem.authenticate(username, password)) {
                    System.out.println("Login successful!");
                    String accountNumber = null;

                    while (true) {
                        System.out.println("\n1. Create Account\n2. Deposit\n3. Withdraw\n4. Transfer\n5. View Balance\n6. View Transaction History\n7. Logout");
                        int action = scanner.nextInt();

                        if (action == 1) {
                            bankingSystem.createAccount(username);
                        } else if (action == 2) {
                            System.out.print("Enter account number: ");
                            accountNumber = scanner.next();
                            Account account = bankingSystem.getAccount(accountNumber);
                            if (account != null) {
                                System.out.print("Enter amount to deposit: ");
                                double amount = scanner.nextDouble();
                                account.deposit(amount);
                                System.out.println("Deposited: $" + amount);
                            } else {
                                System.out.println("Account not found!");
                            }
                        } else if (action == 3) {
                            System.out.print("Enter account number: ");
                            accountNumber = scanner.next();
                            Account account = bankingSystem.getAccount(accountNumber);
                            if (account != null) {
                                System.out.print("Enter amount to withdraw: ");
                                double amount = scanner.nextDouble();
                                if (account.withdraw(amount)) {
                                    System.out.println("Withdrew: $" + amount);
                                } else {
                                    System.out.println("Insufficient funds!");
                                }
                            } else {
                                System.out.println("Account not found!");
                            }
                        } else if (action == 4) {
                            System.out.print("Enter source account number: ");
                            String fromAccountNumber = scanner.next();
                            Account fromAccount = bankingSystem.getAccount(fromAccountNumber);
                            if (fromAccount != null) {
                                System.out.print("Enter destination account number: ");
                                String toAccountNumber = scanner.next();
                                Account toAccount = bankingSystem.getAccount(toAccountNumber);
                                if (toAccount != null) {
                                    System.out.print("Enter amount to transfer: ");
                                    double amount = scanner.nextDouble();
                                    if (fromAccount.transfer(toAccount, amount)) {
                                        System.out.println("Transferred: $" + amount);
                                    } else {
                                        System.out.println("Transfer failed due to insufficient funds!");
                                    }
                                } else {
                                    System.out.println("Destination account not found!");
                                }
                            } else {
                                System.out.println("Source account not found!");
                            }
                        } else if (action == 5) {
                            System.out.print("Enter account number: ");
                            accountNumber = scanner.next();
                            Account account = bankingSystem.getAccount(accountNumber);
                            if (account != null) {
                                System.out.println("Balance: $" + account.getBalance());
                            } else {
                                System.out.println("Account not found!");
                            }
                        } else if (action == 6) {
                            System.out.print("Enter account number: ");
                            accountNumber = scanner.next();
                            Account account = bankingSystem.getAccount(accountNumber);
                            if (account != null) {
                                System.out.println("Transaction History:");
                                for (Transaction transaction : account.getTransactionHistory()) {
                                    System.out.println(transaction);
                                }
                            } else {
                                System.out.println("Account not found!");
                            }
                        } else if (action == 7) {
                            System.out.println("Logged out!");
                            break;
                        } else {
                            System.out.println("Invalid option!");
                        }
                    }
                } else {
                    System.out.println("Invalid username or password!");
                }
            } else if (choice == 3) {
                System.out.println("Exiting the system.");
                break;
            } else {
                System.out.println("Invalid option!");
            }
        }

        scanner.close();
    }
}


