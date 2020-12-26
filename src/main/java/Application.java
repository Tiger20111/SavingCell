import java.util.concurrent.ExecutionException;

public class Application {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int n = 10;
        int amount = 100;
        TransferTest.test(n, amount);
    }
}
