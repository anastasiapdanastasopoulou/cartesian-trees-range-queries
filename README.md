# Cartesian Trees and Range Minimum Queries

Diploma Thesis | Computer Science and Engineering
University of Ioannina

## Overview

This project presents a Java implementation of multiple algorithms and data structures for solving Range Minimum Query (RMQ), Lowest Common Ancestor (LCA), and Bottleneck Edge Query (BEQ) problems.

The thesis explores reductions between these problems and compares different online and offline algorithmic approaches.

## Algorithms and Data Structures

- Cartesian Trees
- Sparse Table
- Euler Tour
- Tarjan's Offline LCA Algorithm
- Union-Find (Disjoint Set Union)
- Cartesian Tree of Tree (CToT)

## Objectives

- Implement different approaches for solving RMQ, LCA, and BEQ problems.
- Explore relationships and reductions between the problems.
- Compare online and offline approaches.
- Validate implementation consistency using common datasets.

## Technologies

- Java

## Academic Context

Diploma Thesis
Department of Computer Science and Engineering
University of Ioannina, Greece

## Project Structure

```text
cartesian-trees-range-queries/
├── src/          # Java source code
├── test-data/    # Input arrays, trees, and queries
├── docs/         # Diploma thesis report
├── README.md
└── .gitignore
```

## How to Run

### Requirements
- Java Development Kit (JDK)
- Command Prompt or Terminal

### Compilation

Open a terminal in the root directory of the repository and run:

```cmd
javac -d . src\*.java
```

### Execution

```cmd
java Main
```

The program reads input datasets from `test-data/` and compares results from different RMQ, LCA, and BEQ implementations.

## Diploma Thesis Report

[Read the full diploma thesis (PDF)](docs/Diploma_Thesis_Cartesian_Trees_and_RMQ.pdf)