package com.scheduler.scheduler;

import com.scheduler.model.Process;
import com.scheduler.util.MetricsCalculator;

import java.util.*;


public class SRTFScheduler {

    public SchedulerResult run(List<Process> inputProcesses) {

        List<Process> processes = deepCopy(inputProcesses);
        processes.sort(Comparator.comparingInt(Process::getArrivalTime)
                .thenComparing(Process::getProcessId));

        int n         = processes.size();
        int completed = 0;
        int time      = 0;

        List<GanttBlock> gantt = new ArrayList<>();
        String lastId   = null;
        int    blockStart = 0;


        PriorityQueue<Process> pq = new PriorityQueue<>(
                Comparator.comparingInt(Process::getRemainingTime)
                        .thenComparingInt(Process::getArrivalTime)
                        .thenComparing(Process::getProcessId)
        );


        int nextArrival = processes.get(0).getArrivalTime();

        while (completed < n) {


            for (Process p : processes) {
                if (p.getArrivalTime() <= time && p.getRemainingTime() > 0
                        && !pq.contains(p)) {
                    pq.add(p);
                }
            }


            if (pq.isEmpty()) {

                int jumpTo = Integer.MAX_VALUE;
                for (Process p : processes) {
                    if (p.getRemainingTime() > 0 && p.getArrivalTime() > time) {
                        jumpTo = Math.min(jumpTo, p.getArrivalTime());
                    }
                }
                if (lastId != null && !"IDLE".equals(lastId)) {
                    gantt.add(new GanttBlock(lastId, blockStart, time));
                    lastId = null;
                }
                gantt.add(new GanttBlock("IDLE", time, jumpTo));
                time      = jumpTo;
                blockStart = jumpTo;
                lastId    = null;
                continue;
            }

            Process current = pq.poll();
            current.setFirstStartTime(time);


            if (!current.getProcessId().equals(lastId)) {
                if (lastId != null) {
                    gantt.add(new GanttBlock(lastId, blockStart, time));
                }
                blockStart = time;
                lastId     = current.getProcessId();
            }

            current.setRemainingTime(current.getRemainingTime() - 1);
            time++;


            for (Process p : processes) {
                if (p.getArrivalTime() == time && p.getRemainingTime() > 0
                        && !pq.contains(p)) {
                    pq.add(p);
                }
            }

            if (current.getRemainingTime() == 0) {
                current.setCompletionTime(time);
                current.calculateMetrics();
                completed++;
            } else {

                pq.add(current);
            }
        }


        if (lastId != null) {
            gantt.add(new GanttBlock(lastId, blockStart, time));
        }

        double[] avgs = MetricsCalculator.computeAverages(processes);
        return new SchedulerResult(processes, gantt, new ArrayList<>(), avgs[0], avgs[1], avgs[2]);
    }

    private List<Process> deepCopy(List<Process> src) {
        List<Process> copies = new ArrayList<>(src.size());
        for (Process p : src) copies.add(new Process(p));
        return copies;
    }
}
