package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        for (int i = 1000; i < 128001; i = i * 2) {
            Ns.addLast(i);
            SLList<Integer> test = new SLList<>();
            for (int j = 0; j < i; j += 1) {
                test.addLast(j);
            }
            Stopwatch sw = new Stopwatch();
            for (int k = 0; k < 1000; k += 1) {
                test.getLast();
            }
            times.addLast(sw.elapsedTime());
            opCounts.addLast(1000);
            }
        System.out.println("Timing table for getLast");
        printTimingTable(Ns, times, opCounts);
        }
}
