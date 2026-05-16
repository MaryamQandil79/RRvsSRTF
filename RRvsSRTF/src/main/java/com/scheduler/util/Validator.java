package com.scheduler.util;

import com.scheduler.model.Process;
import java.util.List;


public class Validator {

    private Validator() { }


    public static void validateNewProcess(String id, String arrivalStr,
                                          String burstStr, List<Process> existing) {


        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Process ID cannot be empty.");
        if (arrivalStr == null || arrivalStr.trim().isEmpty())
            throw new IllegalArgumentException("Arrival Time cannot be empty.");
        if (burstStr == null || burstStr.trim().isEmpty())
            throw new IllegalArgumentException("Burst Time cannot be empty.");


        int arrival, burst;
        try {
            arrival = Integer.parseInt(arrivalStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Arrival Time must be a whole number.");
        }
        try {
            burst = Integer.parseInt(burstStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Burst Time must be a whole number.");
        }


        if (arrival < 0)
            throw new IllegalArgumentException("Arrival Time cannot be negative.");
        if (burst <= 0)
            throw new IllegalArgumentException("Burst Time must be greater than zero.");
        if (burst > 1000)
            throw new IllegalArgumentException("Burst Time seems unrealistically large (> 1000). Check your input.");


        String trimmedId = id.trim();
        for (Process p : existing) {
            if (p.getProcessId().equalsIgnoreCase(trimmedId))
                throw new IllegalArgumentException(
                        "A process with ID '" + trimmedId + "' already exists.");
        }
    }


    public static int validateQuantum(String quantumStr) {
        if (quantumStr == null || quantumStr.trim().isEmpty())
            throw new IllegalArgumentException("Time Quantum cannot be empty.");
        int q;
        try {
            q = Integer.parseInt(quantumStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Time Quantum must be a whole number.");
        }
        if (q <= 0)
            throw new IllegalArgumentException("Time Quantum must be greater than zero.");
        if (q > 500)
            throw new IllegalArgumentException("Time Quantum > 500 is unusual. Please verify.");
        return q;
    }


    public static void validateProcessList(List<Process> list) {
        if (list == null || list.isEmpty())
            throw new IllegalArgumentException(
                    "Please add at least one process before running the simulation.");
    }
}

