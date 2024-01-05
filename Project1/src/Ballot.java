/**
 * Ballot.java
 *
 * This class represents a ballot in an election system. It handles the parsing of an input string
 * representing a voter's choices into a structured format that the system can process. It also manages
 * the ordering and prioritization of candidates as per the voter's preferences.
 *
 * Author: Arpita Dev
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Class representing a single voter's ballot.
 * It is responsible for interpreting the ranking of candidates as given by a voter.
 */

public class Ballot {

    private String inputtedBallot;  // The raw input string representing the voter's choices.
    private ArrayList<Candidate> formattedBallot;  // A structured list of candidates in the order of voter's preference.
    private int ballotRankingDigit;  // A digit representing the current ranking being processed.


    /**
     * Default constructor initializing an empty ballot with a ranking digit starting at 0.
     */

    public Ballot() {
        this.formattedBallot = new ArrayList<>();
        this.ballotRankingDigit = 0;
    }


    /**
     * Overloaded constructor that takes a preformatted ballot.
     *
     * @param formattedBallot An ArrayList of Candidate objects in the order of voter's preference.
     */

    public Ballot(ArrayList<Candidate> formattedBallot) {
        this.formattedBallot = formattedBallot;
    }


    /**
     * Translates a comma-separated string of rankings into an ordered list of Candidate objects.
     *
     * @param input A string representing the voter's ranked choices.
     * @param candidateCount The total number of candidates in the election.
     * @param allCandidates A list of all candidates in the election.
     * @return An ordered ArrayList of Candidate objects based on the voter's preferences.
     * @throws NumberFormatException if a non-integer ranking is found within the input string.
     */

    public ArrayList<Candidate> translateBallot(String input, int candidateCount, List<Candidate> allCandidates) {
        // Split input into an array of preferences
        String[] preferences = input.split(",", -1);
        
        // Create a mapping of rankings to Candidates
        Map<Integer, Candidate> rankingToCandidate = new HashMap<>();
        int x = 1000000;

        // Iterate through preferences and update the mapping
        for (int i = 0; i < preferences.length; ++i) {
            String preference = preferences[i].trim();
            if (preference.isEmpty()) {
                rankingToCandidate.put(x, null);
            } else {
                // Parse preference and assign corresponding ranking
                int ranking = Integer.parseInt(preference);
                rankingToCandidate.put(ranking, allCandidates.get(i));
            }
            x+=1;
        }
        
        Map<Integer, Candidate> sortedMap = new TreeMap<>(rankingToCandidate);

        // Extract values in order of increasing keys
        ArrayList<Candidate> formattedBallot = new ArrayList<>(sortedMap.values());

        return formattedBallot;
    }


    /**
     * Increments the ballot ranking digit.
     */

    public void incrementRankingDigit() {
        this.ballotRankingDigit++;
    }


    // Getters and Setters

    /**
     * Gets the raw input ballot string.
     *
     * @return The raw input string representing the voter's choices.
     */

    public String getInputtedBallot() {
        return inputtedBallot;
    }


    /**
     * Sets the raw input ballot string.
     *
     * @param inputtedBallot The raw input string representing the voter's choices.
     */

    public void setInputtedBallot(String inputtedBallot) {
        this.inputtedBallot = inputtedBallot;
    }


    /**
     * Gets the formatted list of candidates based on voter's preference.
     *
     * @return An ArrayList of Candidate objects in the order of preference.
     */

    public ArrayList<Candidate> getFormattedBallot() {
        return formattedBallot;
    }


    /**
     * Sets the formatted list of candidates based on voter's preference.
     *
     * @param formattedBallot An ArrayList of Candidate objects in the order of preference.
     */

    public void setFormattedBallot(ArrayList<Candidate> formattedBallot) {
        this.formattedBallot = formattedBallot;
    }


    /**
     * Gets the current ranking digit being processed.
     *
     * @return The ballot ranking digit.
     */

    public int getBallotRankingDigit() {
        return ballotRankingDigit;
    }


    /**
     * Sets the current ranking digit.
     *
     * @param ballotRankingDigit The digit representing the current ranking being processed.
     */
    
    public void setBallotRankingDigit(int ballotRankingDigit) {
        this.ballotRankingDigit = ballotRankingDigit;
    }


    /**
     * Retrieves the next candidate in the ranking order, if available.
     *
     * @return The next Candidate object in the order, or null if there are no more candidates or the next candidate is not in the election.
     */

    public Candidate getNextCandidate(){
        if (formattedBallot.size() > 0) {
            Candidate nextCandidate = formattedBallot.remove(0);
            if(nextCandidate.isInElection()) {
                return nextCandidate;
            }
           return null;  
        }
        else {
            return null;
        }
    }
}
