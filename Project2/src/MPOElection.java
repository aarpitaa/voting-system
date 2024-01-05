import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * The `MPOElection` class is a specific type of the ElectionType class that modifies methods
 * to fit to IR election practices and regulations. It contains a getWinners(), runElection(),
 * getCandidateWithMostVotes(), setNumberOfSeats(), getNumberOfSeats(), displayMPOElectionResults().
 * @author Stuti Arora, Praful Das 
 */

public class MPOElection extends ElectionType {
    protected ArrayList<Candidate> winners = new ArrayList<>();
    protected int numberOfSeats;

    public MPOElection() {}

    /**
     * Sets the total seats in this election.
     * @param numberOfSeats - the number of seats available in this election.
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
     * Gets the total seats available in this election.
     * @return int - the number of seats available in this election.
     */
    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    /**
     * Runs the election process for MPO Elections. 
     */
    public void runElection() {
        // Idea with Max Heap: just pull off next highest voted candidates - problem is how to deal w ties
        PriorityQueue<Candidate> maxHeap = new PriorityQueue<>((a,b) -> b.getBallotCount()-a.getBallotCount());
        System.out.println("candidates");

        for(Candidate c: candidateList) {
            maxHeap.add(c);
        }

        Candidate candidateToWinSeat;
        int remainingSeats = numberOfSeats;

        while(remainingSeats > 0) {
            //Pull the most voted candidate from max heap
            Candidate candidateWithMostVotes = maxHeap.poll();
            if(!maxHeap.isEmpty() && candidateWithMostVotes.getBallotCount() == 0) {
                //[TODO] what should be returned
                System.out.println("All Candidates had 0 votes so no winner(s) can be determined");
                return;
            }
            if(!maxHeap.isEmpty() && maxHeap.peek().getBallotCount() == candidateWithMostVotes.getBallotCount()) {
                //Tie exists
                ArrayList<Candidate> candidatesWithMostVotes = new ArrayList<>();
                while(!maxHeap.isEmpty() && maxHeap.peek().getBallotCount() == candidateWithMostVotes.getBallotCount()) {
                    Candidate additionalCandidateWithMostVotes = maxHeap.poll();
                    candidatesWithMostVotes.add(additionalCandidateWithMostVotes);
                }
                //Candidate that has the most votes + has been settled w tie 
                candidateToWinSeat = (Candidate) tieBreaker(candidatesWithMostVotes);
                
                //Put remaining candidates back into the maxHeap
                for(Candidate c: candidatesWithMostVotes) {
                    if(c != candidateToWinSeat) {
                        maxHeap.add(c);
                    }
                }
            } else {
                //No tie
                candidateToWinSeat = candidateWithMostVotes;
            }
            winners.add(candidateToWinSeat);
            remainingSeats--;
            
        }
    }

    /**
     * Gets List of Candidates that are winner(s) in MPO Election.
     * @return ArrayList<Candidate> - the winner(s) of MPO Election.
     */
    public ArrayList<Candidate> getWinners() {
        return winners;
    }

    /**
     * Gets Candiate with the most number of votes. Deals with ties accordingly.
     * @return Candidate - the candidate with most number of votes.
     */
    public Candidate getCandidateWithMostVotes() {
        //Will currently always return top most candidate - need a way to remove candidate
        int max = 0; 
        for(Candidate c: candidateList) {
            if(c.getBallotCount() > max) {
                max = c.getBallotCount();
            }
        }

        ArrayList<Candidate> candidatesWithMostVotes = new ArrayList<>();
        for(Candidate c: candidateList) {
            if(c.getBallotCount() == max) {
                candidatesWithMostVotes.add(c);
            }
        }

        return (Candidate) tieBreaker(candidatesWithMostVotes);
    }

    /**
     * Displays the election results along with the vote count and percentage of each candidate on screen to the user.
     */
    public void displayElectionResults() {
        String accent = "--------------------------------------------";
        int totalVotes = candidateList.stream().mapToInt(Candidate::getBallotCount).sum();

        System.out.printf(
            "%64s %n %54s %n%64s\n\n\n",
            accent,
            "Multiple Popularity Only (MPO) Election Results",
            accent
        );

        System.out.printf("%25s%n", "-------------------------");
        for (Candidate winner : winners) {
            System.out.printf("%9s%14s%n", "| WINNER: ", winner.getName() + "  |");
            System.out.printf("%25s%n", "-------------------------");
        }
        System.out.println();

        for (Candidate candidate : candidateList) {
            double votePercentage = totalVotes > 0 ? (double) candidate.getBallotCount() / totalVotes * 100 : 0;
            System.out.println(
                candidate.getName() +
                ": " +
                candidate.getBallotCount() +
                " votes (" +
                String.format("%.2f", votePercentage) + "%)"
            );
            System.out.println();
        }
    }
}