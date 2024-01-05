import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;

/**
 * The `IRElection` class is a specific type of the ElectionType class that modifies methods
 * to fit to IR election practices and regulations. It contains a getWinner(), runElection(),
 * getLeastVotedCandidate(), getInvalidatedBallots(), displayIRElectionResults()
 * @author Stuti Arora, Neha Bhatia
 */

class IRElection extends ElectionType {

  protected Candidate winner;
  //[TODO]: should be renamed to eliminated ballots to distinguish
  protected ArrayList<Ballot> invalidatedBallots = new ArrayList<>();

  protected ArrayList<Ballot> invalidBallots = new ArrayList<>();

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
   * Prints out formatted ballot. Used for testing and debugging.
   */
  public void printFormattedBallot(ArrayList<Candidate> b) {
    for(int i = 0; i < b.size(); i++) {
        if(b.get(i) != null){
            System.out.print(b.get(i).getName() + " ");
        }
    }
    System.out.println("");
  }

  /**
   * Finds invalid ballots and removes them from an election. Invalid ballots for IR are defined to have 
   * fewer rankings than half the number of candidates in an election. For example,
   * if election has 4 candidates, any ballot with less than 2 rankings is considered invalid. 
   *
   * @return The list of invalid ballots
   */
  public ArrayList<Ballot> findInvalidBallots() {
    //Loop through candidates and all their respectvie ballots
    for(Candidate c: candidateList) {
      ArrayList<Ballot> ballots = c.getBallots();

      for(Ballot b: ballots) {
        //For each ballot, count the number of candidate rankings they have
        int countOfRankings = 0;
        // printFormattedBallot(b.getFormattedBallot());

        for(int i = 0; i < b.getFormattedBallot().size(); i++) {
          if(b.getFormattedBallot().get(i) != null) {
            countOfRankings++;
            //System.out.println("count of rankings " + countOfRankings);
          }
        }
        
        //If number of rankings is below half the candidate count, they are considered invalid
        if(countOfRankings < (getCandidatesCount()/2)) {
          //System.out.println("removing ballot " + countOfRankings);
          invalidBallots.add(b);
        }
      }
      
    }

    //Remove invalid ballots from election
    for(Candidate c: candidateList) {
      ArrayList<Ballot> ballots = c.getBallots();
      for(Ballot invalidBallot: invalidBallots) {
        ballots.remove(invalidBallot);
        c.decrementBallotCount();
      }
    }

    // Check to make sure they are removec
    // System.out.println("CHECKING REMOVAL");
    // for(Candidate c: candidateList) {
    //   ArrayList<Ballot> ballots = c.getBallots();
    //   for(Ballot b: ballots){
    //     printFormattedBallot(b.getFormattedBallot());
    //   }
    // }

    return invalidBallots;
  }


  /**
   * Runs through IR Election process. Uses list of candidates from Election Type object.
   * Checks if there is a winner at every iteration and reshuffles ballots accordingly.
   * Ballots with rankings of candidates who have been eliminated are stored in invalidated ballots list.
   */
  public void runElection() {
    boolean winnerFound = false;
    int numOfVotesToBeWinner = ballotCount / 2 + 1;
    FileProcessor fp = new FileProcessor();
    fp.createAuditFile("IR");

    while (!winnerFound) {
        for (Candidate candidate : candidateList) {
            if (candidate.getBallotCount() >= numOfVotesToBeWinner && candidate.isInElection()) {
                winner = candidate;
                winnerFound = true;
                break;
            }
        }

        if (!winnerFound) {
            int leastNumOfVotes = candidateList.stream()
                                               .filter(Candidate::isInElection)
                                               .mapToInt(Candidate::getBallotCount)
                                               .min().orElse(Integer.MAX_VALUE);

            Candidate candidateToBeRemoved = getLeastVotedCandidate(leastNumOfVotes);
            System.out.println(candidateToBeRemoved.getName());
            redistributeBallots(candidateToBeRemoved);
        }

        fp.writeToAuditFileIR(winnerFound, candidateList, "After iteration ", invalidatedBallots);
    }

    fp.writeToAuditFileIR(winnerFound, candidateList, "Winner Found ", invalidatedBallots);
}

/* Redistributes ballots of eliminated candidate */
private void redistributeBallots(Candidate candidateToBeRemoved) {
    for (Ballot ballot : candidateToBeRemoved.getBallots()) {
        Candidate nextCandidate = ballot.getNextCandidate();

        while (nextCandidate != null && !nextCandidate.isInElection()) {
          nextCandidate = ballot.getNextCandidate();
        }

        if (nextCandidate == null) {
          invalidatedBallots.add(ballot);
          System.out.println(invalidatedBallots);
        } 
        
        else {
            nextCandidate.addToBallotList(ballot);
            System.out.println(nextCandidate.getBallots());
        }
    }

    candidateToBeRemoved.removeCandidateFromElection();
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
   * Returns the list of invalid ballots (to be used for testing)
   * @return ArrayList<Ballot>
   */
  public ArrayList<Ballot> getInvalidBallots() {
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

  /**
   * Writes the invalidated ballots to a file
   */
  public void writeInvalidBallotsToFile() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String dateStr = now.format(formatter);

    // Specify the relative path to the IRElectionFiles folder
    String folderPath = "IRElectionFiles/";
    String fileName = folderPath + "invalidated_" + dateStr + ".txt";

    // Ensure that the folder exists
    new File(folderPath).mkdirs();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
        for (Ballot ballot : invalidBallots) {
            writer.write(ballot.getInputtedBallot() + System.lineSeparator());
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
  }
}
