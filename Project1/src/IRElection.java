import java.util.ArrayList;

/**
 * The `IRElection` class is a specific type of the ElectionType class that modifies methods
 * to fit to IR election practices and regulations. It contains a getWinner(), runElection(),
 * getLeastVotedCandidate(), getInvalidatedBallots(), displayIRElectionResults()
 * @author Stuti Arora
 */

class IRElection extends ElectionType {

  protected Candidate winner;
  protected ArrayList<Ballot> invalidatedBallots = new ArrayList<>();

  public IRElection() {}

  /**
   * Returns the winner candidate once IR Election has completed. If election is not
   * completed, will return null.
   * @return String
   */
  public Candidate getWinner() {
    return winner;
  }

  /**
   * Runs through IR Election process. Uses list of candidates from Election Type object.
   * Checks if there is a winner at every iteration and reshuffles ballots accordingly.
   * Ballots with rankings of candidates who have been eliminated are stored in invalidated ballots list.
   */
  public void runElection() {
    boolean winnerFound = false;
    //[TODO]: check if needs to be ballotCount/2 or ballotCount/2 +1
    int numOfVotesToBeWinner = ballotCount / 2;
    //[TODO]: dummy variable used for testing purposes (will be removed)
    int dummy = 0;

    // To write to the Audit file
    FileProcessor fp = new FileProcessor();


    outerloop:while (!winnerFound && dummy < 5) {
      int leastNumOfVotes = Integer.MAX_VALUE;
      //System.out.println("CHECKING FOR WINNER");

      //First checks to see if election has a winner
      for (int i = 0; i < candidateList.size(); i++) {
        //System.out.println("candidateName " + candidateList.get(i).getName());
        if (
          candidateList.get(i).getBallotCount() >= numOfVotesToBeWinner &&
          candidateList.get(i).isInElection()
        ) {
          //Winner Found!
          //System.out.println("winner found " + candidateList.get(i).getName());
          winner = candidateList.get(i);
          winnerFound = true;
          
          break outerloop;
        }
        //System.out.println("ballots for candidate " + candidateList.get(i).getBallotCount());
        //System.out.println("get candidate list count " + candidateList.get(i).getBallotCount());
        if (candidateList.get(i).getBallotCount() < leastNumOfVotes) {
          leastNumOfVotes = candidateList.get(i).getBallotCount();
          //System.out.println("found new minimum " + leastNumOfVotes);
        }
      }
      //Get the candidate to be removed (whose ballots will be redistributed)
      Candidate candidateToBeRemoved = getLeastVotedCandidate(leastNumOfVotes);
      //System.out.println("candidateToBeRemoved ballots " + candidateToBeRemoved.getBallotCount());

      for (int i = 0; i < candidateToBeRemoved.getBallotCount(); i++) {
        Ballot ballotToBeReshuffled = candidateToBeRemoved.removeBallot();
        Candidate candidateToBeMovedTo = ballotToBeReshuffled.getNextCandidate();
        //System.out.println("SHUFFLING BALLOTS FROM " + candidateToBeRemoved.getName() + " TO " + candidateToBeMovedTo.getName());
        if (candidateToBeMovedTo != null) {
          if (candidateToBeMovedTo.isInElection()) {
            candidateToBeMovedTo.addToBallotList(ballotToBeReshuffled);
          } else {
            //If no other candidate, put in invalidated ballot list
            invalidatedBallots.add(ballotToBeReshuffled);
            candidateToBeMovedTo.removeCandidateFromElection();
          }
        }
      }
      fp.writeToAuditFileIR((winnerFound),candidateList, "After iteration ", invalidatedBallots);
      dummy++;
    }
  }

  /**
   * Finds the candidate(s) with fewest number of votes and runs tie breaker function
   * to return the candidate who will be eliminated from the election.
   * @param leastNumOfVotes
   * @return Candidate
   */
  public Candidate getLeastVotedCandidate(int leastNumOfVotes) {
    //Get the all candidates with least number of votes
    ArrayList<Candidate> leastVotedCandidates = new ArrayList<>();
    //System.out.println("TRYING TO FIND LEAST VOTED CANDIDATE");
    for (int i = 0; i < candidateList.size(); i++) {
      //System.out.println("in candidate list " + candidateList.get(i).getName() + " " + candidateList.get(i).getBallotCount());
      if (
        candidateList.get(i).getBallotCount() == leastNumOfVotes &&
        candidateList.get(i).isInElection()
      ) {
        leastVotedCandidates.add(candidateList.get(i));
        //System.out.println("candidates with fewest votes " + candidateList.get(i).getName());
      }
    }

    Candidate candidateToBeRemoved = (Candidate) tieBreaker(
      leastVotedCandidates
    );
    //System.out.println("candidate to be eliminated " + candidateToBeRemoved.getName());
    return candidateToBeRemoved;
  }

  /**
   * Returns the list of invalidated ballots (to be used for audit file)
   * @return ArrayList<Ballot>
   */
  public ArrayList<Ballot> getInvalidatedBallots() {
    return invalidatedBallots;
  }

  /**
   * Displays IR Election results to the user
   */
  public void displayIRElectionResults() {
    String accent = "--------------------------------------------";
    System.out.printf(
      "%64s %n %54s %n%64s\n\n\n",
      accent,
      "Instant Runoff Elections Results",
      accent
    );
    System.out.printf("%25s%n", "-------------------------");
    System.out.printf("%9s%14s%n", "| WINNER: ", winner.getName() + "  |");
    System.out.printf("%25s%n", "-------------------------");
    System.out.println();
    for (int i = 0; i < this.getCandidateList().size(); i++) {
      System.out.println(
        this.getCandidateList().get(i).getName() +
        ": " +
        this.getCandidateList().get(i).getBallotCount() +
        " votes"
      );
      System.out.println();
    }
  }
}
