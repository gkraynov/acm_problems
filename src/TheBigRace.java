// Accepted

import java.io.*;
import java.math.BigInteger;

public class TheBigRace {
    long maxRun;
    long stepA, stepB;
    long result;


    static long gcd(long x, long y) {
        if (y == 0) {
            return x;
        } else {
            return gcd(y, x % y);
        }
    }

    static BigInteger lcm(long x, long y) {
        BigInteger bigX = BigInteger.valueOf(x);
        BigInteger bigY = BigInteger.valueOf(y);
        return bigX.multiply(bigY).divide(BigInteger.valueOf(gcd(x, y)));
    }

    void solve() {
        BigInteger period = lcm(stepA, stepB);
        long cycles;
        long remain;
        if (period.compareTo(BigInteger.valueOf(maxRun)) != 1) {
            cycles = maxRun / period.longValueExact();
            remain = maxRun - period.longValueExact() * cycles;
        } else {
            cycles = 0;
            remain = maxRun;
        }

        long offset = Math.min(stepA, stepB) - 1;
        result = (offset + 1) * cycles + Math.min(offset, remain);
    }

    void input(InputStream source) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(source));
        String[] vals = reader.readLine().split(" ");
        maxRun = Long.parseLong(vals[0]);
        stepA = Long.parseLong(vals[1]);
        stepB = Long.parseLong(vals[2]);
    }

    void output() {
        long g = gcd(result, maxRun);
        long a = result / g;
        long b = maxRun / g;
        System.out.printf("%d/%d", a, b);
        System.out.println();
    }

    void run() {
        try {
            input(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        solve();
        output();
    }

    public static void main(String[] args) {
        new TheBigRace().run();
    }
}
