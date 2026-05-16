package com.scheduler.scheduler;


public class GanttBlock {

    private final String processId;
    private final int    startTime;
    private final int    endTime;

    public GanttBlock(String processId, int startTime, int endTime) {
        this.processId = processId;
        this.startTime = startTime;
        this.endTime   = endTime;
    }

    public String getProcessId() { return processId; }
    public int    getStartTime() { return startTime; }
    public int    getEndTime()   { return endTime; }

    public boolean isIdle() { return "IDLE".equals(processId); }

    @Override
    public String toString() {
        return String.format("[%s: %d→%d]", processId, startTime, endTime);
    }
}
