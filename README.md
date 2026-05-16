# ⚙️ RR vs SRTF — CPU Scheduling Simulator

A JavaFX desktop application that simulates and visually compares two CPU scheduling algorithms:
**Round Robin (RR)** and **Shortest Remaining Time First (SRTF)**.

---

## 📸 Overview

This project was built to demonstrate the differences in performance between two popular CPU scheduling algorithms, with a side-by-side visual comparison using Gantt charts and detailed metrics.

---

## ✨ Features

- 🖥️ Interactive **Gantt chart** visualization for both algorithms
- 📊 Side-by-side **performance metrics** comparison:
  - Average Waiting Time
  - Average Turnaround Time
  - Average Response Time
- ⚙️ Configurable **Round Robin quantum**
- 📋 Detailed **analysis & recommendation** report
- ✅ Input validation

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| Java 21+ | Core language |
| JavaFX | UI (FXML + CSS) |
| Maven | Build tool |

---

## 🚀 How to Run

### Prerequisites
- Java 21 or higher
- Maven

### Steps
```bash
git clone https://github.com/MaryamQandil79/RRvsSRTF.git
cd RRvsSRTF
mvn clean javafx:run
```

---

## 📁 Project Structure

```
src/
└── main/
    └── java/com/scheduler/
        ├── Main.java
        ├── controller/
        │   └── MainController.java
        ├── model/
        │   └── Process.java
        ├── scheduler/
        │   ├── RoundRobinScheduler.java
        │   ├── SRTFScheduler.java
        │   ├── SchedulerResult.java
        │   └── GanttBlock.java
        └── util/
            ├── MetricsCalculator.java
            └── Validator.java
```

---

## 📖 Algorithms

### 🔁 Round Robin (RR)
A preemptive algorithm that gives each process a fixed time slice (quantum). Fair and widely used in time-sharing systems.

### ⚡ SRTF — Shortest Remaining Time First
A preemptive version of SJF. Always picks the process with the least remaining burst time. Optimal for minimizing average waiting time but can cause starvation.

