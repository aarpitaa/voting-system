import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The `OPLElection` class is second ElectionType class that modifies methods
 * to fit to the OPL election practices and regulations. It contains a getNumberOfSeats(), runElection(),
 * displayOPLElectionResults()
 * @author Neha Bhatia
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

    // Will be decremented throughout the cycle
    int seatsLeft = numberOfSeats;

    // Maps each party's remaining votes after the first round to an array of parties in case of ties
    HashMap<Integer, ArrayList<Party>> votesLeftToPartyMap = new HashMap<Integer, ArrayList<Party>>();

    // The list of party objects must be shuffled to prevent biases against assigning seats to the first
    // party in the list that fills the quota (2 seats but 3 parties fill the quota)
    ArrayList<Party> parties = new ArrayList<>(partyList.values());
    Collections.shuffle(parties);

    // To write to the Audit file
    FileProcessor fp = new FileProcessor();

    // Round 1 Allocations - based on ballots
    for (int i = 0; i < parties.size(); i++) {
      Party party = parties.get(i);

      seatsLeft -= (party.getVoteCount() / quota);
      party.addToPartySeatCount(party.getVoteCount() / quota);

      // used to track each party's remainder votes
      party.setVotesLeft(party.getVoteCount() % quota);

      // if the remainder votes exists in the map, get the existing list of parties
      if (votesLeftToPartyMap.containsKey(party.getVotesLeft())) {
        ArrayList<Party> tempParties = votesLeftToPartyMap.get(party.getVotesLeft());
        tempParties.add(party);
        votesLeftToPartyMap.put(party.getVotesLeft(), tempParties);
      } 
      else {
        ArrayList<Party> p = new ArrayList<Party>() { 
          {
            add(party);
          } 
        };
        votesLeftToPartyMap.put(party.getVotesLeft(), p);
      }

      // Call audit file with current status
      fp.writeToAuditFileOPL(
        (seatsLeft <= 0),
        quota,
        "Round 1 Allocations",
        parties,
        votesLeftToPartyMap,
        seatsLeft
      );
    }

    // Round 2 Allocations if we still have seats left
    if (seatsLeft > 0) {

      // map winner to the list of parties from which the tie was broken in
      Map<Party, ArrayList<Party>> tieBreaks = new HashMap<Party, ArrayList<Party>>();

      //create a list of sorted remainder vote values, iterate through them from highest remaining votes to lowest
      ArrayList<Integer> sortedRemainders = new ArrayList<Integer>(
        votesLeftToPartyMap.keySet()
      );
      Collections.sort(sortedRemainders, Collections.reverseOrder());

      for (int voteCount : sortedRemainders) {
        ArrayList<Party> tempParties = votesLeftToPartyMap.get(voteCount);
        Party winner;
        if (tempParties.size() > 1) {
          winner = (Party) tieBreaker(tempParties);
          tieBreaks.put(winner, tempParties);
        } 
        else {
          winner = tempParties.get(0);
        }

        if (seatsLeft <= 0) {
          break;
        } 
        else {
          winner.setVotesLeft(0);
          seatsLeft -= 1;
          winner.addToPartySeatCount(1);
        }
      }

      fp.writeToAuditFileOPL(
        (seatsLeft <= 0),
        quota,
        "Round 2 Allocations",
        parties,
        votesLeftToPartyMap,
        seatsLeft
      );
    }

    // round 3 - random allocations
    if (seatsLeft > 0) {
      ArrayList<Party> remainingParties = new ArrayList<Party>(
        partyList.values()
      );
      Collections.shuffle(remainingParties);
      for (Party p : remainingParties) {
        if (seatsLeft == 0) break;
        //add new methods
        if (p.getSeatCount() < p.getCandidateCount()) {
          p.addToPartySeatCount(1);
          seatsLeft -= 1;
        }
      }
      fp.writeToAuditFileOPL(
        true,
        quota,
        "Round 3 Allocations",
        parties,
        votesLeftToPartyMap,
        seatsLeft
      );
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
      "Candidates are ordered from most to lease votes, with their votes displayed in the parenthesis\n"
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

      // Iterate through a sorted list of candidates by vote to get winning candidates from each party
      ArrayList<Candidate> candidates = p.getCandidateList();
      for (int i = 0; i < p.getSeatCount(); i++) {
        if (candidates.get(i).getBallotCount() > 0) {
          System.out.print(
            "  " +
            (i + 1) +
            ". " +
            (
              candidates.get(i).getName() +
              " (Votes: " +
              candidates.get(i).getBallotCount() +
              "),  "
            )
          );
        }
      }
      System.out.printf("%n%18s", ("(" + p.getVoteCount() + " votes)"));
      System.out.println();
      System.out.println(accent + accent);
    }

    System.out.println("\n" + accent + accent + accent);
  }
}
