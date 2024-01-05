import java.util.ArrayList;

/**
 * The `Party` class helps tally votes for OPL and organize candidates belonging to a certain party.
 * It contains getters and setters for important Party attributes like seatCount, partyVoteCount, name and the list of candidates.
 * @author Neha Bhatia, Praful Das
 */

public class Party {

  private String name;
  private ArrayList<Candidate> candidateList;
  private int partyVoteCount;
  private int seatCount;
  private int votesLeft;

  public Party(String partyName) {
    name = partyName;
    candidateList = new ArrayList<>();
    partyVoteCount = 0;
    seatCount = 0;
    votesLeft = 0;
  }

  /**
   * Adds a new candidate to the list of candidates belonging to this party
   * @param candidate A candidate to add to the Party's candidate list
   */

  public void addCandidateToList(Candidate candidate) {
    if (!candidateList.contains(candidate)) {
      candidateList.add(candidate);
    }
  }

  /**
   * Gets the existing list of candidates after sorting from highest to lowest candidate vote count
   * @return ArrayList<Candidate>
   */
  public ArrayList<Candidate> getCandidateList() {
    sort(candidateList);
    return candidateList;
  }

  /**
   * Returns the number of candidates in this party
   * @return int size of the candidate list
   */
  public int getCandidateCount() {
    return candidateList.size();
  }

  /**
   * Increases the party vote count by 1
   * @return int size of the candidate list
   */
  public void incrementVoteCount() {
    partyVoteCount++;
  }

  /**
   * Returns the name of the party
   * @return String
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the number of seats the party has
   * @return int - the party seat couint
   */
  public int getPartySeatCount() {
    return seatCount;
  }

  /**
   * Returns the total votes acquired by a party
   * @return int - the total party vote count
   */
  public int getVoteCount() {
    return partyVoteCount;
  }

  /**
   * Returns the votes left after quotas have been subtracted while running the election
   * @return int - the number of votes that have not been counted towards a seat
   */
  public int getVotesLeft() {
    return votesLeft;
  }

  /**
   * Sets the votes left to the remainder of votes after quotas have been satisfied
   * @param votesLeft the number of votes that have not been counted towards a seat
   */
  public void setVotesLeft(int votesLeft) {
    this.votesLeft = votesLeft;
  }

  /**
   * Increases party seat count by n
   * @param n the number of seats that go to this party
   */
  public void addToPartySeatCount(int n) {
    if (n > 0) {
      seatCount += n;
    }
  }

  /**
   * Gets the current number of seats allocated to this party
   * @return int
   */
  public int getSeatCount() {
    return seatCount;
  }

  /**
   * Overriding the sort function to sort the candidate list by highest to lowest ballot count
   * @param c_list a list of candidates to be sorted
   */
  public static void sort(ArrayList<Candidate> c_list) {
    c_list.sort((c1, c2) -> c2.getBallotCount() - c1.getBallotCount());
  }
}
