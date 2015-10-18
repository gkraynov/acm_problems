import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PolycarpMasterpiece {
    String base;
    int gens;
    long[] genLength;
    int[] genRotate;
    List<Range> ranges;

    class Range {
        final char ch;
        final long begin;  // Inclusive
        final long end;    // Inclusive
        final long total;

        Range(char ch, long begin, long end, long total) {
            this.ch = ch;
            this.begin = begin;
            this.end = end;
            this.total = total;
        }

        Range left() {
            long mid = total / 2;
            long newBegin;
            long newEnd;
            if (begin <= end) {
                // Non-circular
                if (begin < mid) {
                    newBegin = begin;
                    newEnd = Math.min(end, mid - 1);
                } else {
                    return null;
                }
            } else {
                // Circular
                if (begin < mid) {
                    newBegin = begin;
                    newEnd = end;
                } else {
                    newBegin = 0;
                    newEnd = Math.min(end, mid - 1);
                }
            }
            return new Range(ch, newBegin, newEnd, mid);
        }

        Range right() {
            long mid = total / 2;
            long newBegin;
            long newEnd;
            if (begin <= end) {
                // Non-circular
                if (end >= mid) {
                    newBegin = Math.max(begin, mid);
                    newEnd = end;
                } else {
                    return null;
                }
            } else {
                // Circular
                if (end >= mid) {
                    newBegin = begin;
                    newEnd = end;
                } else {
                    newBegin = Math.max(begin, mid);
                    newEnd = total - 1;
                }
            }
            return new Range(ch, newBegin - mid, newEnd - mid, mid);
        }

        Range rotate(int leftShift) {
            long sh = leftShift;
            if (sh > total) {
                sh = sh % total;
            }
            long newBegin = begin - sh + total;
            long newEnd = end - sh + total;
            if (newBegin > newEnd) {
                newEnd += total;
            }
            return new Range(ch, newBegin % total, newEnd % total, total);
        }

        String mask(String str) {
            StringBuilder s = new StringBuilder(str);
            if (begin <= end) {
                for (int i = 0; i < begin; i++) {
                    s.setCharAt(i, ' ');
                }
                for (int i = (int) end + 1; i < total; i++) {
                    s.setCharAt(i, ' ');
                }
            } else {
                for (int i = (int) end + 1; i < begin; i++) {
                    s.setCharAt(i, ' ');
                }
            }
            return s.toString();
        }
    }

    long genCalc(int gen, Range range) {
        if (gen == 0) {
            String m = range.mask(base);
            return m.length() - m.replace("" + range.ch, "").length();
        }

        long result = 0;
        Range left = range.left();
        Range right = range.right();
        if (left != null) {
            result += genCalc(gen - 1, range.left());
        }
        if (right != null) {
            result += genCalc(gen - 1, range.right().rotate(genRotate[gen]));
        }
        return result;
    }


    String strRotRight(String str, int shift) {
        int off = str.length() - shift;
        String a = new StringBuilder(str.substring(0, off)).reverse().toString();
        String b = new StringBuilder(str.substring(off)).reverse().toString();
        return new StringBuilder(a + b).reverse().toString();
    }

//    void naive() {
//        String current = base;
//        for (int i = 1; i < gens; i++) {
//            String append = strRotRight(current, genRotate[i]);
//            current += append;
//        }
//        //System.out.println(current);
//
//        //Range x = new Range('e', 8, 13, 22);
//        //System.out.println(x.mask(current));
//        //System.out.println(genCalc(1, x));
//    }

    void solve() {
        ranges.forEach((range) -> {
            try {
                System.out.println(genCalc(gens - 1, range));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    void input(InputStream source) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(source));
        base = reader.readLine().trim();
        String[] pair = reader.readLine().trim().split(" ", 2);
        gens = Integer.parseInt(pair[0]) + 1;
        int tasks = Integer.parseInt(pair[1]);

        String[] rots = reader.readLine().trim().split(" ");
        genRotate = new int[gens];
        genRotate[0] = 0;
        for (int i = 1; i < gens; i++) {
            genRotate[i] = Integer.parseInt(rots[i - 1]);
        }

        genLength = new long[gens];
        genLength[0] = base.length();
        for (int i = 1; i < gens; i++) {
            genLength[i] = genLength[i - 1] * 2;
        }

        ranges = new ArrayList<>(tasks);
        for (int i = 0; i < tasks; i++) {
            String[] range = reader.readLine().trim().split(" ", 3);
            long left = Long.parseLong(range[0]) - 1;
            long right = Long.parseLong(range[1]) - 1;
            char ch = range[2].charAt(0);
            ranges.add(new Range(ch, left, right, genLength[gens - 1]));
        }
    }

    void run() {
        try {
            //input(new FileInputStream("tests/PolycarpMasterpiece/03.in"));
            input(System.in);
            solve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PolycarpMasterpiece().run();
    }
}
