package com.scheduler.scheduler;

import com.scheduler.model.Process;
import com.scheduler.util.MetricsCalculator;

import java.util.*;


public class RoundRobinScheduler {

    private final int quantum;

    public RoundRobinScheduler(int quantum) {
        if (quantum <= 0) throw new IllegalArgumentException("Quantum must be > 0");
        this.quantum = quantum;
    }


    public SchedulerResult run(List<Process> inputProcesses) {


        List<Process> processes = deepCopy(inputProcesses);


        processes.sort(Comparator.comparingInt(Process::getArrivalTime)
                .thenComparing(Process::getProcessId));

        List<GanttBlock> gantt    = new ArrayList<>();
        List<String>     queueLog = new ArrayList<>();

        Queue<Process> readyQueue = new LinkedList<>();
        int time       = 0;
        int completed  = 0;
        int n          = processes.size();
        int nextArrIdx = 0;


        while (nextArrIdx < n && processes.get(nextArrIdx).getArrivalTime() <= time) {
            readyQueue.add(processes.get(nextArrIdx++));
        }

        while (completed < n) {


            if (readyQueue.isEmpty()) {
                int nextArrival = processes.get(nextArrIdx).getArrivalTime();
                gantt.add(new GanttBlock("IDLE", time, nextArrival));
                time = nextArrival;


                while (nextArrIdx < n && processes.get(nextArrIdx).getArrivalTime() <= time) {
                    readyQueue.add(processes.get(nextArrIdx++));
                }
                continue;
            }

            Process current = readyQueue.poll();
            current.setFirstStartTime(time);

            int runFor = Math.min(quantum, current.getRemainingTime());
            int start  = time;
            time      += runFor;

            current.setRemainingTime(current.getRemainingTime() - runFor);
            gantt.add(new GanttBlock(current.getProcessId(), start, time));


            while (nextArrIdx < n && processes.get(nextArrIdx).getArrivalTime() <= time) {
                readyQueue.add(processes.get(nextArrIdx++));
            }

            if (current.getRemainingTime() == 0) {
                current.setCompletionTime(time);
                current.calculateMetrics();
                completed++;
            } else {
                readyQueue.add(current);    // not finished – push to back
            }


            queueLog.add(snapshotQueue(time, readyQueue));
        }

        double[] avgs = MetricsCalculator.computeAverages(processes);
        return new SchedulerResult(processes, gantt, queueLog, avgs[0], avgs[1], avgs[2]);
    }



    private List<Process> deepCopy(List<Process> src) {
        List<Process> copies = new ArrayList<>(src.size());
        for (Process p : src) copies.add(new Process(p));
        return copies;
    }

    private String snapshotQueue(int time, Queue<Process> q) {
        if (q.isEmpty()) return "t=" + time + " → Queue: [empty]";
        StringBuilder sb = new StringBuilder("t=").append(time).append(" → Queue: [");
        for (Process p : q) sb.append(p.getProcessId()).append(" ");
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }
}
