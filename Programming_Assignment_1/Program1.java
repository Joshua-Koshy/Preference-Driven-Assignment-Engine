/*
 * Name: <Joshua Koshy>
 * EID: <jik377
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
//
/**
 * Your solution goes in this class.
 *
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 *
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution. However, do not add extra import statements to this file.
 */
public class Program1 extends AbstractProgram1 {

    /**
     * Determines whether a candidate Matching represents a solution to the stable matching problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    
    @Override
    public boolean isStableMatching(Matching problem) {

      int m = problem.getUniversityCount();
    int n = problem.getStudentCount();
    
    ArrayList<ArrayList<Integer>> universityPreferences = problem.getUniversityPreference();
    ArrayList<ArrayList<Integer>> studentPreferences = problem.getStudentPreference();
    ArrayList<Integer> studentMatching = problem.getStudentMatching();
    
    // Brute force: Check all student-university pairs for blocking pairs
    for (int student = 0; student < n; student++) {
        int currentUniversity = studentMatching.get(student);

        for (int university = 0; university < m; university++) {
            if (currentUniversity == university) {
                continue; // Skip if the student is already matched to this university
            }

            // Check if the student prefers this university over their current match
            if (studentPreferences.get(student).indexOf(university) < studentPreferences.get(student).indexOf(currentUniversity)) {
                // Find the worst student currently matched to this university
                int worstStudent = -1;
                for (int otherStudent = 0; otherStudent < n; otherStudent++) {
                    if (studentMatching.get(otherStudent) == university) {
                        if (worstStudent == -1 || 
                            universityPreferences.get(university).indexOf(otherStudent) > universityPreferences.get(university).indexOf(worstStudent)) {
                            worstStudent = otherStudent;
                        }
                    }
                }

                // If the university prefers this student over its worst match (or has room), it's a blocking pair
                if (worstStudent == -1 || universityPreferences.get(university).indexOf(student) < universityPreferences.get(university).indexOf(worstStudent)) {
                    return false; // Found an instability
                }
            }
        }
    }
    
    return true; 
    }

    /**
     * Determines a solution to the stable matching problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
 public Matching stableMatchingGaleShapley_universityoptimal(Matching problem) {
    int m = problem.getUniversityCount(); // Number of universities
    int n = problem.getStudentCount(); // Number of students

    // Get university positions and preference lists
    ArrayList<Integer> universityPositions = problem.getUniversityPositions();
    ArrayList<ArrayList<Integer>> universityPreferences = problem.getUniversityPreference();
    ArrayList<ArrayList<Integer>> studentPreferences = problem.getStudentPreference();


    int[] availableSlots = new int[m]; // Tracks free slots per university
    int[] studentMatch = new int[n]; // Tracks assigned university per student

    for (int i = 0; i < n; i++) { // Initialize all students as unmatched
    studentMatch[i] = -1;
} 

    LinkedList<Integer> freeUniversities = new LinkedList<>(); // Queue of free universities
    int[] nextProposalIndex = new int[m]; // Tracks next student a university should propose to

    // Initialize available slots and add universities with open slots to the queue
    for (int i = 0; i < m; i++) {
        if (universityPositions.get(i) != null) {
            availableSlots[i] = universityPositions.get(i);
            if (availableSlots[i] > 0) {
                freeUniversities.add(i);
            }
        }
    }

    // Gale-Shapley algorithm: While there is at least one free university with slots
    while (!freeUniversities.isEmpty()) {
        int university = freeUniversities.removeFirst(); // Get next free university
        ArrayList<Integer> universityPref = universityPreferences.get(university);

        if (universityPref == null) continue; // Skip if university has no preference list

        // University makes offers to students until all slots are filled
        while (availableSlots[university] > 0 && nextProposalIndex[university] < universityPref.size()) {
            int student = universityPref.get(nextProposalIndex[university]++); // Get next preferred student
            
            if (studentMatch[student] == -1) { // If student is free, match immediately
                studentMatch[student] = university;
                availableSlots[university]--;
            } else { // Student is already matched, check if they prefer new university
                int currentUniversity = studentMatch[student];
                ArrayList<Integer> studentPref = studentPreferences.get(student);
                
                // If student prefers new university over current match, switch
                if (studentPref != null && studentPref.indexOf(university) < studentPref.indexOf(currentUniversity)) {
                    studentMatch[student] = university;
                    availableSlots[university]--;
                    availableSlots[currentUniversity]++;
                    
                    // If the replaced university now has an open slot, add back to queue
                    if (availableSlots[currentUniversity] > 0) {
                        freeUniversities.add(currentUniversity);
                    }
                }
            }
        }

        // If the university still has open slots, re-add it to the queue
        if (availableSlots[university] > 0) {
            freeUniversities.add(university);
        }
    }

    // Convert int[] to Integer[] 
    Integer[] studentMatchArray = new Integer[n];
    for (int i = 0; i < n; i++) {
        studentMatchArray[i] = studentMatch[i]; 
    }
    problem.setStudentMatching(new ArrayList<>(Arrays.asList(studentMatchArray)));

    return problem;
}


    /**
     * Determines a solution to the stable matching problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
@Override
public Matching stableMatchingGaleShapley_studentoptimal(Matching problem) {
        int m = problem.getUniversityCount();
        int n = problem.getStudentCount();
        
        ArrayList<ArrayList<Integer>> universityPreferences = problem.getUniversityPreference();
        ArrayList<ArrayList<Integer>> studentPreferences = problem.getStudentPreference();
        ArrayList<Integer> universityPositions = problem.getUniversityPositions();
        
        int[] studentMatch = new int[n];
        Arrays.fill(studentMatch, -1);
        
        int[] availableSlots = new int[m];
        for (int i = 0; i < m; i++) {
            availableSlots[i] = universityPositions.get(i);
        }
        
        int[][] studentRank = new int[n][m];
        for (int student = 0; student < n; student++) {
            ArrayList<Integer> prefs = studentPreferences.get(student);
            for (int rank = 0; rank < prefs.size(); rank++) {
                studentRank[student][prefs.get(rank)] = rank;
            }
        }
        
        LinkedList<Integer> freeStudents = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            freeStudents.add(i);
        }
        
        int[] nextProposalIndex = new int[n];
        
        ArrayList<ArrayList<Integer>> universityMatches = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            universityMatches.add(new ArrayList<>());
        }
        
        while (!freeStudents.isEmpty()) {
            int student = freeStudents.poll();
            
            ArrayList<Integer> studentPrefList = studentPreferences.get(student);
            while (nextProposalIndex[student] < studentPrefList.size()) {
                int university = studentPrefList.get(nextProposalIndex[student]++);
                
                if (availableSlots[university] > 0) {
                    studentMatch[student] = university;
                    universityMatches.get(university).add(student);
                    availableSlots[university]--;
                    break;
                } else {
                    ArrayList<Integer> universityPrefList = universityPreferences.get(university);
                    ArrayList<Integer> currentMatches = universityMatches.get(university);
                    
                    int worstStudent = currentMatches.get(0);
                    for (int matchedStudent : currentMatches) {
                        if (universityPrefList.indexOf(matchedStudent) > universityPrefList.indexOf(worstStudent)) {
                            worstStudent = matchedStudent;
                        }
                    }
                    
                    if (universityPrefList.indexOf(student) < universityPrefList.indexOf(worstStudent)) {
                        universityMatches.get(university).remove((Integer) worstStudent);
                        universityMatches.get(university).add(student);
                        studentMatch[worstStudent] = -1;
                        studentMatch[student] = university;
                        freeStudents.add(worstStudent);
                        break;
                    }
                }
            }
        }
        
        ArrayList<Integer> studentMatchingList = new ArrayList<>();
        for (int match : studentMatch) {
            studentMatchingList.add(match);
        }
        problem.setStudentMatching(studentMatchingList);
        return problem;
    }
}
