import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * The `OPLElection` class is second ElectionType class that modifies methods
 * to fit to the OPL election practices and regulations. It contains a getNumberOfSeats(), runElection(),
 * displayOPLElectionResults()
 * @author Neha Bhatia, Praful Das
 */

public class OPLElection extends ElectionType {

  protected int numberOfSeats;

  /**
   * Sets the total seats in this election
   * @param numberOfSeats - the number of seats available in this election
   */
  public void setNumberOfSeats(int numberOfSeats) {
    if (numberOfSeats >= 0) {
      this.numberOfSeats = numberOfSeats;
    } 
    else {
      throw new IllegalArgumentException("Number of seats cannot be negative");
    }
  }

  /**
   * Gets the total seats available in this election
   * @return int - the number of seats available in this election
   */
  public int getNumberOfSeats() {
    return numberOfSeats;
  }

  /**
   * Runs one cycle of the OPL Election to determine which party gets seats
   */
  public void runElection() {
    int quota = ballotCount / numberOfSeats;
    int seatsLeft = numberOfSeats;
    HashMap<Integer, ArrayList<Party>> remainderVotesToParties = new HashMap<Integer, ArrayList<Party>>();
    FileProcessor fp = new FileProcessor();
    int roundNumber = 1;  // Initialize round number

    // Calculate total votes for each party and initial seat allocation
    for (Party party : partyList.values()) {
      int partyVote = party.getVoteCount(); 
      int seats = Math.min(partyVote / quota, party.getCandidateCount());  // Ensure seats do not exceed candidate count
      party.addToPartySeatCount(seats); 
      seatsLeft -= seats;
      int remainingVotes = partyVote % quota;
      party.setVotesLeft(remainingVotes); 

      // Group parties by their remaining votes
      remainderVotesToParties.computeIfAbsent(remainingVotes, k -> new ArrayList<>()).add(party);
    }

    // Call audit file with current status
    fp.writeToAuditFileOPL(
      (seatsLeft <= 0),
      quota,
      "Round " + roundNumber + " Allocations",
      new ArrayList<>(partyList.values()),
      remainderVotesToParties,
      seatsLeft
    );
    roundNumber++;

    // Allocate remaining seats based on highest remainder votes
    while (seatsLeft > 0) {
      // Find the highest remainder votes
      int maxVotes = Collections.max(remainderVotesToParties.keySet());

      // Get the list of parties that have the highest remainder votes
      ArrayList<Party> partiesWithMaxRemainder = remainderVotesToParties.get(maxVotes);

      boolean seatAllocated = false;
      while (seatAllocated == false && !partiesWithMaxRemainder.isEmpty()) {
        // Resolve ties using tieBreaker, if necessary
        Party winningParty;
        if (partiesWithMaxRemainder.size() > 1) {
          winningParty = (Party) tieBreaker(partiesWithMaxRemainder);
        } else {
          winningParty = partiesWithMaxRemainder.get(0);
        }

        // Allocate a seat to the winning party if it has enough candidates
        if (winningParty.getSeatCount() < winningParty.getCandidateCount()) {
          winningParty.addToPartySeatCount(1);
          seatsLeft--;
          seatAllocated = true;
        } else {
          // Remove party from consideration and try next party
          partiesWithMaxRemainder.remove(winningParty);
        }    
      }

      // Remove the winning party's votes from consideration in the next round
      remainderVotesToParties.get(maxVotes).removeAll(partiesWithMaxRemainder);
      if (remainderVotesToParties.get(maxVotes).isEmpty()) {
        remainderVotesToParties.remove(maxVotes);
      }

      // Write to Audit file for subsequent rounds
      fp.writeToAuditFileOPL(
        (seatsLeft <= 0),
        quota,
        "Round " + roundNumber + " Allocations",
        new ArrayList<>(partyList.values()),
        remainderVotesToParties,
        seatsLeft
      );
      roundNumber++;
    }
  }

  /**
   * Displays the election results along with assigned candidates on screen to the user
   */
  public void displayElectionResults() {
    String accent = "--------------------------------------------";
    System.out.printf(
      "%64s %n %54s %n%64s\n\n\n",
      accent,
      "Open Party List Election Results",
      accent
    );
    System.out.println(
      "Candidates are ordered from most to least votes, with their votes displayed in the parenthesis\n"
    );

    System.out.println(accent + accent + accent);
    System.out.printf("%30s%-18s%n", "Party names | ", " Candidates");
    System.out.println(accent + accent);

    for (String name : partyList.keySet()) {
        Party p = partyList.get(name);
        System.out.printf(
            "%30s",
            ("Party " + name + " (" + p.getSeatCount() + " seats) | ")
        );

        // Sort and resolve ties in the candidate list
        ArrayList<Candidate> candidates = p.getCandidateList();
        resolveCandidateTies(candidates);

        for (int i = 0; i < p.getSeatCount(); i++) {
            if (i < candidates.size() && candidates.get(i).getBallotCount() > 0) {
                System.out.print(
                    "  " +
                    (i + 1) +
                    ". " +
                    candidates.get(i).getName() +
                    " (Votes: " +
                    candidates.get(i).getBallotCount() +
                    "),  "
                );
            }
        }
        System.out.printf("%n%18s", ("(" + p.getVoteCount() + " votes)"));
        System.out.println();
        System.out.println(accent + accent);
    }
    System.out.println("\n" + accent + accent + accent);
}

// Method to resolve ties among candidates using tieBreaker
private void resolveCandidateTies(ArrayList<Candidate> candidates) {
    // Sort candidates by votes in descending order
    candidates.sort((c1, c2) -> c2.getBallotCount() - c1.getBallotCount());
    
    // Iterate through the list and find ties
    for (int i = 0; i < candidates.size() - 1; i++) {
      if (candidates.get(i).getBallotCount() == candidates.get(i + 1).getBallotCount()) {
        // List of tied candidates
        ArrayList<Candidate> tiedCandidates = new ArrayList<>();
        tiedCandidates.add(candidates.get(i));
        tiedCandidates.add(candidates.get(i + 1));

        // Iterate through the rest of the list to find all tied candidates
        int j = i + 2;
        while (j < candidates.size() && candidates.get(j).getBallotCount() == candidates.get(i).getBallotCount()) {
            tiedCandidates.add(candidates.get(j));
            j++;
        }

        // Apply tieBreaker to tied candidates
        Candidate winner = (Candidate) tieBreaker(tiedCandidates);
        // Remove all tied candidates and reinsert the winner at the original position
        for (Candidate c : tiedCandidates) {
            candidates.remove(c);
        }
        candidates.add(i, winner);

        // Adjust the loop index to skip over resolved ties
        i = j - 1;
      }
    }
  }
}
