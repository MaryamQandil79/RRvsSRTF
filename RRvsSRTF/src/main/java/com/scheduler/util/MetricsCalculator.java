package com.scheduler.util;

import com.scheduler.model.Process;
import com.scheduler.scheduler.SchedulerResult;

import java.util.List;


public class MetricsCalculator {

    private MetricsCalculator() {}


    public static double[] computeAverages(List<Process> processes) {
        double totalWT = 0, totalTAT = 0, totalRT = 0;
        for (Process p : processes) {
            totalWT  += p.getWaitingTime();
            totalTAT += p.getTurnaroundTime();
            totalRT  += p.getResponseTime();
        }
        int n = processes.size();
        return new double[]{ totalWT / n, totalTAT / n, totalRT / n };
    }


    public static String generateComparison(SchedulerResult rrResult,
                                            SchedulerResult srtfResult,
                                            int quantum) {

        double rrWT   = rrResult.getAvgWaitingTime();
        double rrTAT  = rrResult.getAvgTurnaroundTime();
        double rrRT   = rrResult.getAvgResponseTime();

        double srWT   = srtfResult.getAvgWaitingTime();
        double srTAT  = srtfResult.getAvgTurnaroundTime();
        double srRT   = srtfResult.getAvgResponseTime();

        StringBuilder sb = new StringBuilder();

        sb.append("══════════════════════════════════════════════════════════\n");
        sb.append("         SCHEDULING ALGORITHM COMPARISON – ANALYSIS\n");
        sb.append("══════════════════════════════════════════════════════════\n\n");


        sb.append("▸ Average Waiting Time\n");
        if (Math.abs(rrWT - srWT) < 0.001) {
            sb.append("  Both algorithms produce the same average waiting time (")
                    .append(String.format("%.2f", rrWT)).append(" units).\n\n");
        } else if (srWT < rrWT) {
            sb.append(String.format(
                    "  SRTF wins: %.2f vs %.2f units (%.1f%% lower).\n"
                            + "  SRTF always picks the shortest job next, so processes\n"
                            + "  that are almost done finish quickly, dragging the average down.\n\n",
                    srWT, rrWT, pctBetter(rrWT, srWT)));
        } else {
            sb.append(String.format(
                    "  Round Robin wins: %.2f vs %.2f units.\n"
                            + "  With this particular workload and quantum=%d, RR's\n"
                            + "  time-sharing distributes CPU more evenly than SRTF.\n\n",
                    rrWT, srWT, quantum));
        }


        sb.append("▸ Average Turnaround Time\n");
        if (srTAT <= rrTAT) {
            sb.append(String.format(
                    "  SRTF: %.2f units  |  Round Robin: %.2f units\n"
                            + "  SRTF finishes jobs faster in terms of total time in system.\n\n",
                    srTAT, rrTAT));
        } else {
            sb.append(String.format(
                    "  Round Robin: %.2f units  |  SRTF: %.2f units\n"
                            + "  RR edges out SRTF on turnaround here, likely because\n"
                            + "  the quantum is well-matched to average burst length.\n\n",
                    rrTAT, srTAT));
        }


        sb.append("▸ Average Response Time\n");
        if (rrRT <= srRT) {
            sb.append(String.format(
                    "  Round Robin: %.2f units  |  SRTF: %.2f units\n"
                            + "  RR gives every process CPU time within one quantum,\n"
                            + "  making it far more responsive for interactive workloads.\n\n",
                    rrRT, srRT));
        } else {
            sb.append(String.format(
                    "  SRTF: %.2f units  |  Round Robin: %.2f units\n"
                            + "  SRTF responds faster here because short jobs jump the queue.\n\n",
                    srRT, rrRT));
        }


        sb.append("▸ Fairness\n");
        sb.append("  Round Robin is inherently fair – every process receives\n");
        sb.append("  equal CPU slices regardless of burst length.\n");
        sb.append("  SRTF can starve long processes if short jobs keep arriving,\n");
        sb.append("  making it unsuitable for fairness-critical environments.\n\n");


        sb.append("▸ Quantum Effect (RR quantum = ").append(quantum).append(")\n");
        if (quantum <= 2) {
            sb.append("  Very small quantum → heavy context-switch overhead,\n");
            sb.append("  near-concurrent execution but poor throughput.\n\n");
        } else if (quantum >= 20) {
            sb.append("  Large quantum → RR degrades towards FCFS behaviour;\n");
            sb.append("  long processes hold CPU for extended periods.\n\n");
        } else {
            sb.append("  Moderate quantum → good balance between responsiveness\n");
            sb.append("  and throughput. Ideal for general-purpose time-sharing.\n\n");
        }


        sb.append("══════════════════════════════════════════════════════════\n");
        sb.append("RECOMMENDATION\n");
        sb.append("══════════════════════════════════════════════════════════\n");

        boolean srtfOverall = (srWT + srTAT) < (rrWT + rrTAT);
        if (srtfOverall) {
            sb.append("  Use SRTF for batch systems where minimising average\n");
            sb.append("  waiting and turnaround time is the top priority.\n\n");
            sb.append("  Use Round Robin for interactive / real-time systems\n");
            sb.append("  where response time and fairness matter more than\n");
            sb.append("  raw throughput. Tune the quantum to your workload.\n");
        } else {
            sb.append("  For this workload, Round Robin with quantum=").append(quantum)
                    .append(" performs\n");
            sb.append("  competitively with SRTF. It offers the additional\n");
            sb.append("  benefit of starvation-freedom and predictable response.\n\n");
            sb.append("  SRTF remains theoretically optimal for minimising\n");
            sb.append("  average wait time but requires knowledge of future\n");
            sb.append("  burst times – impractical in real OS kernels.\n");
        }

        return sb.toString();
    }

    private static double pctBetter(double worse, double better) {
        if (worse == 0) return 0;
        return ((worse - better) / worse) * 100.0;
    }
}
