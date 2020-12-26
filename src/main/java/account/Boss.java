package account;

import bank.Bank;

public class Boss implements Runnable{
    Bank bank;
    int prize;
    public Boss(Bank bank, int prize) {
        this.bank = bank;
        this.prize = prize;
    }

    public void run() {
        System.out.println("Boss start given prize to all");
        for (Account account : bank.getAccounts()) {
            account.deposit(prize);
        }
    }
}
