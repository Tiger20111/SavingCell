package bank;

import account.Account;

import java.util.ArrayList;
import java.util.Random;

public class Bank {
    ArrayList<Account> accounts;
    public int baseAmount;
    private int numAccounts;

    public Bank(int numAcc, int baseAmount) {
        accounts = new ArrayList<>();
        final Random random = new Random();
        for (int i = 0; i < numAcc; i++) {
            accounts.add(new Account(3 * Math.abs(random.nextInt() % (baseAmount))));
        }
        this.baseAmount = baseAmount;
        this.numAccounts = numAcc;
    }

    public Account getAccount(int id) {
        return this.accounts.get(id);
    }
    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }
    public int getNumAccounts() {
        return numAccounts;
    }
}
