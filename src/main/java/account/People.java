package account;

import bank.Bank;

import java.util.Random;

public class People implements Runnable{
    Bank bank;
    Account account;
    int id;
    public People(Bank bank, Account account, int id) {
        this.bank = bank;
        this.account = account;
        this.id = id;
    }

    public void run() {
        Random random = new Random();
        int anotherId;
        do {
            anotherId = Math.abs(random.nextInt() % bank.getNumAccounts());
        } while (anotherId == id);
        System.out.println("Thread id: " + Thread.currentThread().getId() + ": start transfer from " + anotherId + " to " + id);
        Account anotherAcc = bank.getAccount(anotherId);
        anotherAcc.transfer(bank.baseAmount,this.account);
        System.out.println("Thread id: " + Thread.currentThread().getId() + ": end transfer from " + anotherId + " to " + id + " "  + anotherAcc.getBalance());
    }
}
