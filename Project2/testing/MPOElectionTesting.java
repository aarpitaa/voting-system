import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.*;

/**
 * Testing IRElection functions: checkWinner1(), checkWinner2(), checkWinner3() to test entire IR Election process. 
 * checkGetInvalidatedBallots(), checkDisplayIRElectionResults.
 * @author Stuti Arora
 */

public class MPOElectionTesting {

    //TEST #1: Using MPO Example From Requriments Document 
    //Pike: 3, Foster 2, Deutsch 0, Borg 2, Jones 1, Smith 1
    //Winners (Pike, [tie bt so one chosen] Foster and Borg)

    @Test
    public void checkWinner1() {
        //Create 4 Candidates
        Candidate c1 = new Candidate("Pike");
        Candidate c2 = new Candidate("Foster");
        Candidate c3 = new Candidate("Deutsch");
        Candidate c4 = new Candidate("Borg");
        Candidate c5 = new Candidate("Jones");
        Candidate c6 = new Candidate("Smith");

        //Add candidates to listOfCandidates to pass into translateBallot()
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);
        listOfCandidates.add(c5);
        listOfCandidates.add(c6);

        //Create a Map to attribute string names of parties to Party objects + add to listOfParties
        HashMap<String, Party> listOfParties = new HashMap<>();
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);

        //Assign each candidate to respective party
        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p1);
        c3.assignCandidateToParty(p2);
        c4.assignCandidateToParty(p2);
        c5.assignCandidateToParty(p2);
        c6.assignCandidateToParty(p3);
        
        
        //Create 6 ballots, translate them to correct form to be used by runElection() algorithms
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();
        Ballot b3 = new Ballot();
        Ballot b4 = new Ballot();
        Ballot b5 = new Ballot();
        Ballot b6 = new Ballot();
        Ballot b7 = new Ballot();
        Ballot b8 = new Ballot();
        Ballot b9 = new Ballot();

        ArrayList<Candidate> translatedB1 = b1.translateBallot("1,,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB2 = b2.translateBallot("1,,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB3 = b3.translateBallot(",1,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB4 = b4.translateBallot(",,,,1,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB5 = b5.translateBallot(",,,,,1", 4, listOfCandidates);
        ArrayList<Candidate> translatedB6 = b6.translateBallot(",,,1,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB7 = b7.translateBallot(",,,1,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB8 = b8.translateBallot("1,,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB9 = b9.translateBallot(",1,,,,", 4, listOfCandidates);
        b1.setFormattedBallot(translatedB1);
        b2.setFormattedBallot(translatedB2);
        b3.setFormattedBallot(translatedB3);
        b4.setFormattedBallot(translatedB4);
        b5.setFormattedBallot(translatedB5);
        b6.setFormattedBallot(translatedB6);
        b7.setFormattedBallot(translatedB7);
        b8.setFormattedBallot(translatedB8);
        b9.setFormattedBallot(translatedB9);

        //Assign ballots to each candidate based on their ranking 
        c1.addToBallotList(b1);
        c1.addToBallotList(b2);
        c1.addToBallotList(b8);
        c2.addToBallotList(b3);
        c2.addToBallotList(b9);
        c4.addToBallotList(b6);
        c4.addToBallotList(b7);
        c5.addToBallotList(b4);
        c6.addToBallotList(b5);
        
        //Create election and attribute candidate list, party list, ballot count to object. Then run election. 
        MPOElection elec = new MPOElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(9);
        elec.setNumberOfSeats(2);
        elec.runElection();
        ArrayList<Candidate> winners = elec.getWinners();
        System.out.println("winner");
        for(Candidate c: winners) {
            System.out.println(c.getName() + " " + c.getBallotCount());
        }
        //Manual Check
        //Winners (Pike, [tie bt so one chosen] Foster and Borg)
    }

    //Checking for no winner with all empty ballots
    @Test
    public void checkWinner2() {
        //Create 4 Candidates
        Candidate c1 = new Candidate("Pike");
        Candidate c2 = new Candidate("Foster");
        Candidate c3 = new Candidate("Deutsch");
        Candidate c4 = new Candidate("Borg");
        Candidate c5 = new Candidate("Jones");
        Candidate c6 = new Candidate("Smith");

        //Add candidates to listOfCandidates to pass into translateBallot()
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);
        listOfCandidates.add(c5);
        listOfCandidates.add(c6);

        //Create a Map to attribute string names of parties to Party objects + add to listOfParties
        HashMap<String, Party> listOfParties = new HashMap<>();
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);

        //Assign each candidate to respective party
        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p1);
        c3.assignCandidateToParty(p2);
        c4.assignCandidateToParty(p2);
        c5.assignCandidateToParty(p2);
        c6.assignCandidateToParty(p3);
        
        
        //Create 6 ballots, translate them to correct form to be used by runElection() algorithms
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();
        Ballot b3 = new Ballot();
        Ballot b4 = new Ballot();
        Ballot b5 = new Ballot();
        Ballot b6 = new Ballot();
        Ballot b7 = new Ballot();
        Ballot b8 = new Ballot();
        Ballot b9 = new Ballot();

        ArrayList<Candidate> translatedB1 = b1.translateBallot(",,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB2 = b2.translateBallot(",,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB3 = b3.translateBallot(",,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB4 = b4.translateBallot(",,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB5 = b5.translateBallot(",,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB6 = b6.translateBallot(",,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB7 = b7.translateBallot(",,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB8 = b8.translateBallot(",,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB9 = b9.translateBallot(",,,,,", 4, listOfCandidates);
        b1.setFormattedBallot(translatedB1);
        b2.setFormattedBallot(translatedB2);
        b3.setFormattedBallot(translatedB3);
        b4.setFormattedBallot(translatedB4);
        b5.setFormattedBallot(translatedB5);
        b6.setFormattedBallot(translatedB6);
        b7.setFormattedBallot(translatedB7);
        b8.setFormattedBallot(translatedB8);
        b9.setFormattedBallot(translatedB9);

        //Assign ballots to each candidate based on their ranking 
        //All ballots empty so no assignment needed
        
        //Create election and attribute candidate list, party list, ballot count to object. Then run election. 
        MPOElection elec = new MPOElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(9);
        elec.setNumberOfSeats(2);
        elec.runElection();
        ArrayList<Candidate> winners = elec.getWinners();
        System.out.println("winner");
        for(Candidate c: winners) {
            System.out.println(c.getName() + " " + c.getBallotCount());
        }
        //Manual Check
        //Should get a output statement saying: "All Candidates had 0 votes so no winner(s) can be determined"
    }

    //TEST #3 Using MPO Example with all ties 
    //Pike: 1, Foster 1, Deutsch 1, Borg 1, Jones 1, Smith 1
    //Winners: any candidate
    @Test
    public void checkWinner3() {
        //Create 4 Candidates
        Candidate c1 = new Candidate("Pike");
        Candidate c2 = new Candidate("Foster");
        Candidate c3 = new Candidate("Deutsch");
        Candidate c4 = new Candidate("Borg");
        Candidate c5 = new Candidate("Jones");
        Candidate c6 = new Candidate("Smith");

        //Add candidates to listOfCandidates to pass into translateBallot()
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);
        listOfCandidates.add(c5);
        listOfCandidates.add(c6);

        //Create a Map to attribute string names of parties to Party objects + add to listOfParties
        HashMap<String, Party> listOfParties = new HashMap<>();
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);

        //Assign each candidate to respective party
        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p1);
        c3.assignCandidateToParty(p2);
        c4.assignCandidateToParty(p2);
        c5.assignCandidateToParty(p2);
        c6.assignCandidateToParty(p3);
        
        
        //Create 6 ballots, translate them to correct form to be used by runElection() algorithms
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();
        Ballot b3 = new Ballot();
        Ballot b4 = new Ballot();
        Ballot b5 = new Ballot();
        Ballot b6 = new Ballot();
        Ballot b7 = new Ballot();
        Ballot b8 = new Ballot();
        Ballot b9 = new Ballot();

        ArrayList<Candidate> translatedB1 = b1.translateBallot("1,,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB2 = b2.translateBallot(",1,,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB3 = b3.translateBallot(",,1,,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB4 = b4.translateBallot(",,,1,,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB5 = b5.translateBallot(",,,,1,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB6 = b6.translateBallot(",,,,,1", 4, listOfCandidates);
        b1.setFormattedBallot(translatedB1);
        b2.setFormattedBallot(translatedB2);
        b3.setFormattedBallot(translatedB3);
        b4.setFormattedBallot(translatedB4);
        b5.setFormattedBallot(translatedB5);
        b6.setFormattedBallot(translatedB6);

        //Assign ballots to each candidate based on their ranking 
        c1.addToBallotList(b1);
        c2.addToBallotList(b2);
        c3.addToBallotList(b3);
        c4.addToBallotList(b4);
        c5.addToBallotList(b5);
        c6.addToBallotList(b6);
        
        //Create election and attribute candidate list, party list, ballot count to object. Then run election. 
        MPOElection elec = new MPOElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(9);
        elec.setNumberOfSeats(2);
        elec.runElection();
        ArrayList<Candidate> winners = elec.getWinners();
        System.out.println("winner");
        for(Candidate c: winners) {
            System.out.println(c.getName() + " " + c.getBallotCount());
        }
        //Manual Check
        //Winners: can be any candidate
    }
    
    // Test #4: Tests the displayElectionResults method in the MPOElection class
    // simulates an MPOElection with predefined candidates and votes,
    // runs the election to determine winners, and then invokes the displayElectionResults
    // method to output the election results
    @Test
    public void checkDisplayElectionResults() {
        // Setup: Create candidates and assign votes
        Candidate c1 = new Candidate("Pike");
        Candidate c2 = new Candidate("Foster");
        Candidate c3 = new Candidate("Borg");

        c1.incrementBallotCount();
        c1.incrementBallotCount();
        c2.incrementBallotCount();
        c3.incrementBallotCount();
        c3.incrementBallotCount();
        c3.incrementBallotCount();

        // Add candidates to a list
        ArrayList<Candidate> candidates = new ArrayList<>();
        candidates.add(c1);
        candidates.add(c2);
        candidates.add(c3);

        // Create MPOElection object and set properties
        MPOElection election = new MPOElection();
        election.setCandidateList(candidates);
        election.setBallotCount(6); // Total votes
        election.setNumberOfSeats(2); // Number of seats

        // Run the election to determine winners
        election.runElection();

        // Redirect stdout to capture the output of displayElectionResults
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Call displayElectionResults
        election.displayElectionResults();

        // Reset stdout
        System.setOut(originalOut);

        // Output the captured content to stdout for manual verification
        System.out.println(outContent.toString());

        // Manual Check: Verify the output format and data correctness
    }
}
