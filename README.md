<<<<<<< HEAD
# Matching Engine: Stable Assignment Solver (Java)

This project implements a complete engine for solving a multi-capacity stable assignment problem.  
It supports validating an existing assignment and generating new matchings using two variants of the
Gale–Shapley algorithm.

The system takes any two groups (e.g., applicants and slots, workers and tasks, clients and providers)
and produces a stable assignment according to their ranked preferences.

---

## Core Capabilities

### 🔹 Stability Verification
A checker that analyzes an existing assignment and determines whether it violates:
- Blocking pairs  
- Preference-based priority conflicts  
- Capacity constraints on the “provider” side  

This enables sanity-checking of externally generated matchings.

### 🔹 Applicant-Optimal Matching
Implements the classical version of Gale–Shapley where *applicants propose* and providers accept
according to capacity and preference. The resulting assignment is:
- Optimal for applicants  
- Stable for all participants  

### 🔹 Provider-Optimal Matching
Implements the reversed algorithm where *providers propose*. This produces a stable assignment that:
- Optimizes outcomes for providers  
- Preserves all standard stability guarantees  

### 🔹 Multi-Position Support
Providers may have **multiple available slots**, and the algorithm manages these as flexible,
priority-based queues.

---

## File Structure

- **Program1.java**  
  Implements all matching logic, including:
  - Stability checking  
  - Applicant-optimal matching  
  - Provider-optimal matching  

- **Matching.java**  
  Defines input format and ranking/matching representations.

- **Driver.java**  
  CLI wrapper for running the solver on any input file.

- **Input Files (.in)**  
  Text-based configuration describing:
  - Number of providers  
  - Number of applicants  
  - Provider capacities  
  - Preference lists  

---

## Usage

Run via the command line:

### Applicant-optimal matching:
```bash
java Driver -s input.in
=======
# Preference-Driven-Assignment-Engine
>>>>>>> fb0f3f0a611441ac83e8b4ad22d29b125525338b
