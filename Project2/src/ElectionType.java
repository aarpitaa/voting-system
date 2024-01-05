/**
 * ElectionType.java
 * 
 * This abstract class provides a structure for the two different types of elections: IR and OPL.
 * It contains common properties and methods used in the election process such
 * as ballot counting, candidate management, and the election type.
 * 
 * Author: Arpita Dev
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * The abstract class ElectionType serves as a blueprint for the two election types: IR and OPL.
 * It encapsulates shared data and operations for handling election processes.
 */

public abstract class ElectionType {

    protected int ballotCount;                       // Total number of ballots cast in the election
    protected int candidatesCount;                   // Total number of candidates participating
    protected ArrayList<Candidate> candidateList;    // List of candidates in the election
    protected Map<String, Party> partyList;          // Map of party names to Party objects
    protected String electionType;                   // Type of election, e.g., "IR" for Instant Runoff or "OPL" for Open Party Listing


    /**
     * Default constructor initializing an election with no ballots or candidates.
     */

    public ElectionType() {
        this.ballotCount = 0;
        this.candidatesCount = 0;
        this.candidateList = new ArrayList<>();
        this.partyList = new HashMap<>();
        this.electionType = "";
    }


    /**
     * Constructor with initial ballot and candidate counts.
     * 
     * @param ballotCount     Initial number of ballots.
     * @param candidatesCount Initial number of candidates.
     */

    public ElectionType(int ballotCount, int candidatesCount) {
        this();
        this.ballotCount = ballotCount;
        this.candidatesCount = candidatesCount;
    }


    /**
     * Creates a list of Candidate objects based on a string of candidate entries.
     * 
     * @param candidateEntries A string representation of candidates and their parties.
     * @return A list of Candidate objects.
     * @throws IllegalArgumentException If candidate entries are null or improperly formatted.
     */

    protected ArrayList<Candidate> createCandidateList(String candidateEntries) {
        ArrayList<Candidate> list = new ArrayList<>();
        if (candidateEntries == null || candidateEntries.trim().isEmpty()) {
            throw new IllegalArgumentException("Candidate entries cannot be null or empty.");
        }
        String[] entries = candidateEntries.split(",");
    
        for (String entry : entries) {
            String[] parts = entry.trim().split("\\s+");
            String name = parts[0];
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Candidate name cannot be empty.");
            }
            String partyName = parts.length > 1 ? parts[1] : "Independent";
    
            Party party = partyList.computeIfAbsent(partyName, k -> new Party(k));
    
            Candidate candidate = new Candidate(name);
            candidate.assignCandidateToParty(party);
            party.addCandidateToList(candidate);
            list.add(candidate);
        }
        return list;
    }


    /**
     * Selects a candidate randomly from a list to break a tie.
     * 
     * @param objects List of candidates among whom the tie is to be broken.
     * @return The selected candidate object.
     * @throws IllegalArgumentException If the list is null or empty.
     */

    protected Object tieBreaker(ArrayList<?> objects) {
        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException("Objects list cannot be null or empty for a tiebreaker.");
        }
        if (objects.size() == 1) {
            return objects.get(0);
        }
        Random random = new Random();
        int randomIndex = random.nextInt(objects.size());
        return objects.get(randomIndex);
    }


    /**
     * Abstract method to run the election process.
     */

    public abstract void runElection();
    

    /**
     * Gets the total number of ballots cast in the election.
     *
     * @return The total number of ballots.
     */

    public int getBallotCount() {
        return ballotCount;
    }


    /**
     * Sets the total number of ballots cast in the election.
     *
     * @param ballotCount The number of ballots to be set.
     */

    public void setBallotCount(int ballotCount) {
        this.ballotCount = ballotCount;
    }


    /**
     * Gets the total number of candidates participating in the election.
     *
     * @return The total number of candidates.
     */

    public int getCandidatesCount() {
        return candidatesCount;
    }


    /**
     * Sets the total number of candidates participating in the election.
     *
     * @param candidatesCount The number of candidates to be set.
     */

    public void setCandidatesCount(int candidatesCount) {
        this.candidatesCount = candidatesCount;
    }


    /**
     * Gets the list of candidates in the election.
     *
     * @return A list of Candidate objects.
     */

    public ArrayList<Candidate> getCandidateList() {
        return candidateList;
    }


    /**
     * Sets the list of candidates in the election.
     *
     * @param candidateList The list of Candidate objects to be set.
     */

    public void setCandidateList(ArrayList<Candidate> candidateList) {
        this.candidateList = candidateList;
    }


    /**
     * Gets the map of party names to Party objects.
     *
     * @return A map associating party names with Party objects.
     */

    public Map<String, Party> getPartyList() {
        return partyList;
    }


    /**
     * Sets the map of party names to Party objects.
     *
     * @param partyList The map associating party names with Party objects to be set.
     */

    public void setPartyList(Map<String, Party> partyList) {
        this.partyList = partyList;
    }


     /**
     * Gets the type of the election.
     *
     * @return The election type as a string.
     */

    public String getElectionType() {
        return electionType;
    }


    /**
     * Sets the type of the election.
     *
     * @param electionType The type of the election to be set.
     */
    
    public void setElectionType(String electionType) {
        this.electionType = electionType;
    }
}
