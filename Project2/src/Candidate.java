import java.util.ArrayList;

/**
 * The Candidate class represents a candidate in an election, storing information
 * such as name, ballot count, associated party, and a list of received ballots.
 */
public class Candidate {
    private String name;
    private int ballotCount;
    private Party party;
    private ArrayList<Ballot> ballots;
    private boolean inElection;
    
    /**
     * Constructs a Candidate object with the specified name.
     *
     * @param name The name of the candidate.
     */
    public Candidate(String name) {
        this.name = name;
        this.ballotCount = 0;
        this.party = null;
        this.ballots = new ArrayList<>();
        this.inElection = true;
    }
    
    /**
     * Assigns the candidate to a specified party.
     *
     * @param party The party to which the candidate is assigned.
     */
    public void assignCandidateToParty(Party party) {
        this.party = party;
    }
    
    /**
     * Gets the name of the candidate.
     *
     * @return The name of the candidate.
     */
    public String getName() {
        return name;
    }
 
    /**
     * Gets the number of received ballots by the candidate.
     *
     * @return The number of received ballots.
     */
    public int getBallotCount() {
        return ballotCount;
    }
    
    /**
     * Increments the number of received ballots by the candidate.
     */
    public void incrementBallotCount() {
        ballotCount++;
    }

    /**
     * Increments the number of received ballots by the candidate.
     */
    public void decrementBallotCount() {
        ballotCount++;
    }

    
    /**
     * Gets the associated party of the candidate.
     *
     * @return The associated party.
     */
    public Party getCandidateParty() { 
        return party;
    }
    
    /**
     * Removes the candidate from the election.
     */
    public void removeCandidateFromElection() {
        inElection = false;
    }
    
    /**
     * Checks if the candidate is still in the election.
     *
     * @return True if the candidate is in the election, false otherwise.
     */
    public boolean isInElection() {
        return inElection;
    }

    /**
     * Removes and returns the first received ballot. If no ballots are present, returns null.
     *
     * @return The first received ballot or null if no ballots are present.
     */
    public Ballot removeBallot() {
        if (ballotCount != 0) {
            Ballot ballot =  ballots.remove(0);
            ballotCount--;
            return ballot;
        }
        else
            return null;
    }
    
    /**
     * Adds a new ballot to the list of received ballots and increments the ballot count.
     *
     * @param b The ballot to be added.
     */
    public void addToBallotList(Ballot b) {
            ballots.add(b);
            ballotCount++;
    }

    /**
     * Gets the list of received ballots by the candidate (for testing purposes).
     *
     * @return The list of received ballots.
     */
    public ArrayList<Ballot> getBallots() {
        return ballots;
    }
}
