package account;

import java.util.Random;
import java.util.concurrent.locks.*;

public class Account {
    private int balance;
    private int preferred;
    Lock preferredLock = new ReentrantLock();
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    Condition conditionPreferred = lock.newCondition();

    public Account(int startBalance) {
        this.balance = startBalance;
        this.preferred = 0;
    }

    public void deposit(int k) {
        lock.lock();
        balance += k;
        condition.signalAll();
        conditionPreferred.signalAll();
        lock.unlock();
    }

    public void withdrawal(int k) {
        lock.lock();
        while (preferred > 0 || balance < k) {
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        balance -= k;
        lock.unlock();
    }

    public int getBalance() {
        // Для изначального вывода, чтобы понимать, что происходит
        return this.balance;
    }

    public void withdrawalPreferred(int k) {
        preferredLock.lock();
        preferred++;
        preferredLock.unlock();

        lock.lock();
        while (balance < k) {
            try {
                conditionPreferred.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        balance -= k;
        preferredLock.lock();
        preferred--;
        if (preferred > 0) {
            conditionPreferred.signalAll();
        } else {
            condition.signalAll();
        }
        preferredLock.unlock();
        lock.unlock();
    }

    public void transfer(int k, Account anotherAccount) {
        Random random = new Random();
        int exit = random.nextInt() % 2;
        if (exit == 0) {
            withdrawal(k);
        } else {
            withdrawalPreferred(k);
        }
        anotherAccount.deposit(k);
    }

}
