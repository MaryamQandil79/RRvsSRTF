package com.scheduler.model;


public class Process {


    private final String processId;
    private final int    arrivalTime;
    private final int    burstTime;


    private int remainingTime;


    private int completionTime;
    private int turnaroundTime;
    private int waitingTime;
    private int responseTime;
    private int firstStartTime = -1;

    public Process(String processId, int arrivalTime, int burstTime) {
        this.processId     = processId;
        this.arrivalTime   = arrivalTime;
        this.burstTime     = burstTime;
        this.remainingTime = burstTime;
    }


    public Process(Process other) {
        this.processId      = other.processId;
        this.arrivalTime    = other.arrivalTime;
        this.burstTime      = other.burstTime;
        this.remainingTime  = other.burstTime;   // always reset
        this.completionTime = 0;
        this.turnaroundTime = 0;
        this.waitingTime    = 0;
        this.responseTime   = 0;
        this.firstStartTime = -1;
    }



    public String getProcessId()      { return processId; }
    public int    getArrivalTime()    { return arrivalTime; }
    public int    getBurstTime()      { return burstTime; }
    public int    getRemainingTime()  { return remainingTime; }
    public int    getCompletionTime() { return completionTime; }
    public int    getTurnaroundTime() { return turnaroundTime; }
    public int    getWaitingTime()    { return waitingTime; }
    public int    getResponseTime()   { return responseTime; }
    public int    getFirstStartTime() { return firstStartTime; }



    public void setRemainingTime(int t)  { this.remainingTime  = t; }
    public void setCompletionTime(int t) { this.completionTime = t; }
    public void setFirstStartTime(int t) {
        if (firstStartTime == -1) firstStartTime = t;
    }


    public void calculateMetrics() {
        turnaroundTime = completionTime - arrivalTime;
        waitingTime    = turnaroundTime - burstTime;
        responseTime   = firstStartTime - arrivalTime;
    }

    @Override
    public String toString() {
        return String.format("Process{id=%s, AT=%d, BT=%d}", processId, arrivalTime, burstTime);
    }
}
