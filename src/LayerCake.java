// Accepted

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LayerCake {
    private List<Rect> layers;

    private class Rect {
        public final int height;  // height <= width
        public final int width;

        public Rect(int x, int y) {
            height = Math.min(x, y);
            width = Math.max(y, x);
        }
    }

    private class WidthSet {
        private final int[] widths;
        private int size = 0;
        public int bestWidth;
        public int bestCount;

        public WidthSet(int capacity) {
            this.widths = new int[capacity];
        }

        public void add(int value) {
            int pos = Arrays.binarySearch(widths, 0, size, value);
            if (pos < 0) {
                pos = -pos - 1;
            }
            size += 1;
            for (int i = size - 1; i > pos; i--) {
                widths[i] = widths[i - 1];
            }
            widths[pos] = value;
        }

        public void calcBest() {
            long bestS = 0;
            for (int i = 0; i < size; i++) {
                int w = widths[i];
                long s = ((long) size - i) * w;
                if (s > bestS) {
                    bestWidth = w;
                    bestCount = size - i;
                    bestS = s;
                }
            }
        }
    }

    private void solve() {
        int n = layers.size();
        layers.sort((a, b) -> Integer.compare(a.height, b.height));

        int bestH = 0;
        int bestW = 0;
        long bestV = 0;

        WidthSet ws = new WidthSet(n);
        int h = layers.get(n - 1).height;
        for (int i = n - 1; i >= 0; i--) {
            Rect rect = layers.get(i);
            if (rect.height != h) {
                ws.calcBest();
                int w = ws.bestWidth;
                long v = ((long) h) * w * ws.bestCount;
                if (v > bestV) {
                    bestH = h;
                    bestW = w;
                    bestV = v;
                }
                h = rect.height;
            }
            ws.add(rect.width);
        }

        System.out.printf("%d\n%d %d", bestV, bestH, bestW);
    }

    private void input(InputStream source) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(source));
        int n = Integer.parseInt(reader.readLine());
        layers = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            String[] pair = reader.readLine().trim().split(" ", 2);
            int x = Integer.parseInt(pair[0]);
            int y = Integer.parseInt(pair[1]);
            layers.add(new Rect(x, y));
        }
        layers.add(new Rect(0, 0));  // Sentinel
    }

    private void run() {
        try {
            input(System.in);
            solve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LayerCake().run();
    }
}
