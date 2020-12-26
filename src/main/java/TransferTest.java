import account.Account;
import account.Boss;
import account.People;
import bank.Bank;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TransferTest {
    public static void test(int n, int amount) throws ExecutionException, InterruptedException {
        Bank bank = new Bank(n, amount);
        Boss boss = new Boss(bank, n * amount);
        ArrayList<People> people = new ArrayList<>();
        ExecutorService tasksPool = Executors.newFixedThreadPool(n + 1);
        ArrayList<Future<?>> futures = new ArrayList<>();
        printStartBalance(bank);
        experement(boss, bank, people, tasksPool, futures);
    }

    public static void experement(Boss boss, Bank bank, ArrayList<People> people,
                                  ExecutorService tasksPool, ArrayList<Future<?>> futures) throws ExecutionException, InterruptedException {
        for (int i = 0; i < bank.getAccounts().size(); i++) {
            people.add(new People(bank, bank.getAccount(i), i));
        }
        for (People human : people) {
            Future<?> f = tasksPool.submit(human);
            futures.add(f);
        }
        Future<?> f = tasksPool.submit(boss);
        futures.add(f);

        for (Future<?> future : futures) {
            future.get();
        }
        tasksPool.shutdown();
    }

    public static void printStartBalance(Bank bank) {
        ArrayList<Account> accounts = bank.getAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println("Account with id " + i + " has balance: " + accounts.get(i).getBalance());
        }
    }
}
