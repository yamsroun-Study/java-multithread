package thread.join.practice;

import java.math.BigInteger;
import java.util.List;

public class ComplexCalculation {

    public static void main(String[] args) throws InterruptedException {

        ComplexCalculation calc = new ComplexCalculation();
        BigInteger result = calc.calculateResult(BigInteger.TEN, BigInteger.TWO, BigInteger.TWO, BigInteger.TEN);
        System.out.println(">>> result=" + result);
    }

    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        List<PowerCalculatingThread> threads = List.of(
            new PowerCalculatingThread(base1, power1),
            new PowerCalculatingThread(base2, power2)
        );

        for (PowerCalculatingThread thread: threads) {
            thread.start();
            thread.join();
        }

        BigInteger result = BigInteger.ZERO;
        for (PowerCalculatingThread thread: threads) {
            result = result.add(thread.getResult());
        }
        return result;
    }

    private static class PowerCalculatingThread extends Thread {

        private final BigInteger base;
        private final BigInteger power;
        private BigInteger result = BigInteger.ONE;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            for (BigInteger bi = BigInteger.ZERO; bi.compareTo(power) < 0; bi = bi.add(BigInteger.ONE)) {
                result = result.multiply(base);
            }
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
