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

    public void withdrawal(int k) throws InterruptedException {
        lock.lock();
        while (true) {
            if (balance < k) {
                    condition.await();
            }
            preferredLock.lock();
            if (balance < k || preferred > 0) {
                preferredLock.unlock();
                condition.await();
            } else {
                preferredLock.unlock();
                break;
            }
        }
        balance -= k;
        lock.unlock();
    }

    public int getBalance() {
        // Для изначального вывода, чтобы понимать, что происходит
        return this.balance;
    }

    public void withdrawalPreferred(int k) throws InterruptedException {
        preferredLock.lock();
        preferred++;
        preferredLock.unlock();

        lock.lock();
        while (balance < k) {
                conditionPreferred.await();
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
        try {
            if (exit == 0) {
                    withdrawal(k);

            } else {
                withdrawalPreferred(k);
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        anotherAccount.deposit(k);
    }

}
