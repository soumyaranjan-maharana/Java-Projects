package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Bank {

    private final int account;
    private final String accountHolder;
    private int balance;

    private final Map<String, List<Integer>> statement = new HashMap<>();

    public Bank(int account, String accountHolder) {
        this.account = account;
        this.accountHolder = accountHolder;
        this.balance = 1000;
    }

    public int getAccount() {
        return account;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public int getBalance() {
        return balance;
    }

    private void addTransaction(String type, int amount) {
        statement.computeIfAbsent(type, k -> new ArrayList<>()).add(amount);
    }

    public void deposit(int amount) {

        if (amount <= 0) {
            System.out.println("Invalid deposit amount.\n");
            return;
        }

        balance += amount;
        addTransaction("Deposit", amount);

        System.out.println(
            "You deposited " + amount +
            ". Current balance: " + balance + "\n"
        );
    }

    public void withdraw(int amount) {

        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.\n");
            return;
        }

        if (amount > balance) {
            System.out.println("Insufficient balance.\n");
            return;
        }

        balance -= amount;
        addTransaction("Withdraw", amount);

        System.out.println(
            "You withdrew " + amount +
            ". Current balance: " + balance + "\n"
        );
    }

    public void transfer(Bank receiver, int amount) {

        if (receiver == this) {
            System.out.println("You cannot transfer money to your own account.\n");
            return;
        }

        if (amount <= 0) {
            System.out.println("Invalid transfer amount.\n");
            return;
        }

        if (amount > balance) {
            System.out.println("Insufficient balance.\n");
            return;
        }

        balance -= amount;
        receiver.balance += amount;

        addTransaction(
            "Transfer sent to " + receiver.account,
            amount
        );

        receiver.addTransaction(
            "Transfer received from " + account,
            amount
        );

        System.out.println(
            "You transferred " + amount +
            " to " + receiver.accountHolder +
            ". Current balance: " + balance + "\n"
        );
    }

    public void showStatement() {

        System.out.println("\n----- ACCOUNT STATEMENT -----");

        if (statement.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Map.Entry<String, List<Integer>> entry : statement.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }

        System.out.println("-----------------------------\n");
    }

    public String details() {
        return "Account Number: " + account +
               "\nAccount Holder: " + accountHolder +
               "\nBalance: " + balance + "\n";
    }

    @Override
    public String toString() {
        return "Account Number: " + account +
               "\nAccount Holder: " + accountHolder + "\n";
    }
}

class Who {

    public Bank find(List<Bank> accounts, Scanner sc) {

        if (accounts.isEmpty()) {
            System.out.println("No accounts available.\n");
            return null;
        }

        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + accounts.get(i));
        }

        System.out.print("Enter account index: ");

        if (!sc.hasNextInt()) { 
            System.out.println("Please enter a valid number.\n");
            sc.next();
            return null;
        }

        int whichAcc = sc.nextInt();

        if (whichAcc < 1 || whichAcc > accounts.size()) {
            System.out.println("Invalid account selection.\n");
            return null;
        }

        return accounts.get(whichAcc - 1);
    }
}

public class Bank_Account {

    public static void main(String[] args) {

        List<Bank> accounts = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        Who w = new Who();

        int account = 100;

        while (true) {

            System.out.println("1. Create a new account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer money");
            System.out.println("5. Account statement");
            System.out.println("6. Account details");
            System.out.println("7. Exit");

            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.\n");
                sc.next();
                continue;
            }

            int choose = sc.nextInt();

            if (choose == 1) {

                System.out.print("Enter account holder name: ");
                sc.nextLine();
                String name = sc.nextLine().trim();

                if (name.isEmpty()) {
                    System.out.println("Name cannot be empty.\n");
                    continue;
                }

                Bank acc = new Bank(account, name);
                accounts.add(acc);

                System.out.println("Account created successfully!");
                System.out.println(acc.details());

                account++;

            } else if (choose == 2) {

                Bank acc = w.find(accounts, sc);

                if (acc == null) continue;

                System.out.print("How much do you want to deposit? ");
                if (!sc.hasNextInt()) {
                    System.out.println("Invalid amount.\n");
                    sc.next();
                    continue;
                }

                acc.deposit(sc.nextInt());

            } else if (choose == 3) {

                Bank acc = w.find(accounts, sc);

                if (acc == null) continue;

                System.out.print("How much do you want to withdraw? ");
                if (!sc.hasNextInt()) {
                    System.out.println("Invalid amount.\n");
                    sc.next();
                    continue;
                }

                acc.withdraw(sc.nextInt());

            } else if (choose == 4) {

                Bank sender = w.find(accounts, sc);

                if (sender == null) continue;

                System.out.println("\nSelect receiver account:");

                Bank receiver = w.find(accounts, sc);

                if (receiver == null) continue;

                System.out.print("How much do you want to transfer? ");

                if (!sc.hasNextInt()) {
                    System.out.println("Invalid amount.\n");
                    sc.next();
                    continue;
                }

                sender.transfer(receiver, sc.nextInt());

            } else if (choose == 5) {

                Bank acc = w.find(accounts, sc);

                if (acc == null) continue;

                acc.showStatement();

            } else if (choose == 6) {

                Bank acc = w.find(accounts, sc);

                if (acc == null) continue;

                System.out.println(acc.details());

            } else if (choose == 7) {

                System.out.println("Thank you for using our banking system.");
                break;

            } else {

                System.out.println("Invalid choice.\n");
            }
        }

        sc.close();
    }
}
