package com.scheduler.scheduler;

import com.scheduler.model.Process;
import java.util.List;


public class SchedulerResult {

    private final List<Process>    processes;
    private final List<GanttBlock> ganttBlocks;
    private final List<String>     readyQueueLog;

    private final double avgWaitingTime;
    private final double avgTurnaroundTime;
    private final double avgResponseTime;

    public SchedulerResult(List<Process> processes,
                           List<GanttBlock> ganttBlocks,
                           List<String> readyQueueLog,
                           double avgWT, double avgTAT, double avgRT) {
        this.processes       = processes;
        this.ganttBlocks     = ganttBlocks;
        this.readyQueueLog   = readyQueueLog;
        this.avgWaitingTime  = avgWT;
        this.avgTurnaroundTime = avgTAT;
        this.avgResponseTime = avgRT;
    }

    public List<Process>    getProcesses()       { return processes; }
    public List<GanttBlock> getGanttBlocks()     { return ganttBlocks; }
    public List<String>     getReadyQueueLog()   { return readyQueueLog; }
    public double           getAvgWaitingTime()  { return avgWaitingTime; }
    public double           getAvgTurnaroundTime(){ return avgTurnaroundTime; }
    public double           getAvgResponseTime() { return avgResponseTime; }
}
