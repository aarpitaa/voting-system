import java.util.ArrayList;
import java.util.HashMap;
import org.junit.*;

/**
 * Testing IRElection functions: checkWinner1(), checkWinner2(), checkWinner3() to test entire IR Election process. 
 * checkGetInvalidatedBallots(), checkDisplayIRElectionResults.
 * @author Stuti Arora, Neha Bhatia, Arpita Dev
 */

public class IRElectionTesting {

    //TEST #1: Using IR Example From Requriments Document - No ties, special cicumstances

    @Test
    public void checkWinner1() {
        //Create 4 Candidates
        Candidate c1 = new Candidate("Rosen");
        Candidate c2 = new Candidate("Kleinberg");
        Candidate c3 = new Candidate("Chou");
        Candidate c4 = new Candidate("Royce");

        //Add candidates to listOfCandidates to pass into translateBallot()
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);

        //Create a Map to attribute string names of parties to Party objects + add to listOfParties
        HashMap<String, Party> listOfParties = new HashMap<>();
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        Party p4 = new Party("L");
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);
        listOfParties.put("L", p4);

        //Assign each candidate to respective party
        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p2);
        c3.assignCandidateToParty(p3);
        c4.assignCandidateToParty(p4);
        
        //Create 6 ballots, translate them to correct form to be used by runElection() algorithms
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();
        Ballot b3 = new Ballot();
        Ballot b4 = new Ballot();
        Ballot b5 = new Ballot();
        Ballot b6 = new Ballot();
        ArrayList<Candidate> translatedB1 = b1.translateBallot("1,3,4,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB2 = b2.translateBallot("1,,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB3 = b3.translateBallot("1,2,3,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB4 = b4.translateBallot("3,2,1,4", 4, listOfCandidates);
        ArrayList<Candidate> translatedB5 = b5.translateBallot(",,1,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB6 = b6.translateBallot(",,,1", 4, listOfCandidates);
        b1.setFormattedBallot(translatedB1);
        b2.setFormattedBallot(translatedB2);
        b3.setFormattedBallot(translatedB3);
        b4.setFormattedBallot(translatedB4);
        b5.setFormattedBallot(translatedB5);
        b6.setFormattedBallot(translatedB6);

        b1.setInputtedBallot("1,3,4,2");
        b2.setInputtedBallot("1,,2");
        b3.setInputtedBallot("1,2,3,");
        b4.setInputtedBallot("3,2,1,4");
        b5.setInputtedBallot(",,1,2");
        b6.setInputtedBallot(",,,1");
        
        //Assign ballots to each candidate based on their ranking #1
        c1.addToBallotList(b1);
        c1.addToBallotList(b2);
        c1.addToBallotList(b3);
        c3.addToBallotList(b4);
        c3.addToBallotList(b5);
        c3.addToBallotList(b6);
        
        //Create election and attribute candidate list, party list, ballot count to object. Then run election. 
        IRElection elec = new IRElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(6);
        elec.runElection();
        Assert.assertEquals("Rosen", elec.getWinner().getName());
    }

    //TEST #2: IR  - With Ties - NOTE: WILL ALWAYS TIME OUT because least voted candidates have no votes attributed to them 
    //so redistribution cannot occur. 
     
    @Test
    public void checkWinner2() {
        //Create 4 Candidates
        Candidate c1 = new Candidate("Rosen");
        Candidate c2 = new Candidate("Kleinberg");
        Candidate c3 = new Candidate("Chou");
        Candidate c4 = new Candidate("Royce");

        //Add candidates to listOfCandidates to pass into translateBallot()
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);

        //Create a Map to attribute string names of parties to Party objects + add to listOfParties
        HashMap<String, Party> listOfParties = new HashMap<>();
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        Party p4 = new Party("L");
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);
        listOfParties.put("L", p4);

        //Assign each candidate to respective party
        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p2);
        c3.assignCandidateToParty(p3);
        c4.assignCandidateToParty(p4);
        
        //Create 6 ballots, translate them to correct form to be used by runElection() algorithms
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();
        Ballot b3 = new Ballot();
        Ballot b4 = new Ballot();
        Ballot b5 = new Ballot();
        Ballot b6 = new Ballot();
        ArrayList<Candidate> translatedB1 = b1.translateBallot("1,3,4,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB2 = b2.translateBallot("1,,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB3 = b3.translateBallot("1,2,3,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB4 = b4.translateBallot("2,1,3,4", 4, listOfCandidates);
        ArrayList<Candidate> translatedB5 = b5.translateBallot(",1,,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB6 = b6.translateBallot(",1,,2", 4, listOfCandidates);
        b1.setFormattedBallot(translatedB1);
        b2.setFormattedBallot(translatedB2);
        b3.setFormattedBallot(translatedB3);
        b4.setFormattedBallot(translatedB4);
        b5.setFormattedBallot(translatedB5);
        b6.setFormattedBallot(translatedB6);

        b1.setInputtedBallot("1,3,4,2");
        b2.setInputtedBallot("1,,2");
        b3.setInputtedBallot("1,2,3,");
        b4.setInputtedBallot("2,1,3,4");
        b5.setInputtedBallot(",1,,2");
        b6.setInputtedBallot(",1,,2");
        
       //Assign ballots to each candidate based on their ranking #1
       c1.addToBallotList(b1);
       c1.addToBallotList(b2);
       c1.addToBallotList(b3);
       c2.addToBallotList(b4);
       c2.addToBallotList(b5);
       c2.addToBallotList(b6);

       //Create election and attribute candidate list, party list, ballot count to object. Then run election.  
        IRElection elec = new IRElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(6);
        elec.runElection();
        Assert.assertEquals("Rosen", elec.getWinner().getName());
    }

//TEST #3: IR  - With Ties
     
    @Test
    public void checkWinner3() {
        //Create 4 Candidates
        Candidate c1 = new Candidate("Rosen");
        Candidate c2 = new Candidate("Kleinberg");
        Candidate c3 = new Candidate("Chou");
        Candidate c4 = new Candidate("Royce");

        //Add candidates to listOfCandidates to pass into translateBallot()
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);

        //Create a Map to attribute string names of parties to Party objects + add to listOfParties
        HashMap<String, Party> listOfParties = new HashMap<>();
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        Party p4 = new Party("L");
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);
        listOfParties.put("L", p4);
        
        //Assign each candidate to respective party
        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p2);
        c3.assignCandidateToParty(p3);
        c4.assignCandidateToParty(p4);
        
        //Create 6 ballots, translate them to correct form to be used by runElection() algorithms
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();
        Ballot b3 = new Ballot();
        Ballot b4 = new Ballot();
        Ballot b5 = new Ballot();
        Ballot b6 = new Ballot();
        ArrayList<Candidate> translatedB1 = b1.translateBallot("1,3,4,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB2 = b2.translateBallot("1,,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB3 = b3.translateBallot("3,2,1,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB4 = b4.translateBallot("2,1,3,4", 4, listOfCandidates);
        ArrayList<Candidate> translatedB5 = b5.translateBallot(",2,,1", 4, listOfCandidates);
        ArrayList<Candidate> translatedB6 = b6.translateBallot(",1,,2", 4, listOfCandidates);
        b1.setFormattedBallot(translatedB1);
        b2.setFormattedBallot(translatedB2);
        b3.setFormattedBallot(translatedB3);
        b4.setFormattedBallot(translatedB4);
        b5.setFormattedBallot(translatedB5);
        b6.setFormattedBallot(translatedB6);

        b1.setInputtedBallot("1,3,4,2");
        b2.setInputtedBallot("1,,2");
        b3.setInputtedBallot("3,2,1,");
        b4.setInputtedBallot("2,1,3,4");
        b5.setInputtedBallot(",2,,1");
        b6.setInputtedBallot(",1,,2");
    
        //Assign ballots to each candidate based on their ranking #1
        c1.addToBallotList(b1);
        c1.addToBallotList(b2);
        c3.addToBallotList(b3);
        c2.addToBallotList(b4);
        c4.addToBallotList(b5);
        c2.addToBallotList(b6);
        
        //Create election and attribute candidate list, party list, ballot count to object. Then run election.  
        IRElection elec = new IRElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(6);
        elec.runElection();
        Assert.assertEquals("Kleinberg", elec.getWinner().getName());
    }

    @Test
    public void testHandlingOfInvalidatedBallots() {
        // Setup candidates
        Candidate c1 = new Candidate("Rosen");
        Candidate c2 = new Candidate("Kleinberg");
        Candidate c3 = new Candidate("Chou");
        Candidate c4 = new Candidate("Royce");

        // Add candidates to the list
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);

        // Setup parties and assign to candidates
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        Party p4 = new Party("L");

        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p2);
        c3.assignCandidateToParty(p3);
        c4.assignCandidateToParty(p4);

        // Create a map of party names to Party objects
        HashMap<String, Party> listOfParties = new HashMap<>();
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);
        listOfParties.put("L", p4);

        // Create ballots with preferences that lead to invalidation
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();
        Ballot b3 = new Ballot();
        ArrayList<Candidate> translatedB1 = b1.translateBallot("4,3,2,1", 4, listOfCandidates);
        ArrayList<Candidate> translatedB2 = b2.translateBallot("4,3,2,1", 4, listOfCandidates);
        ArrayList<Candidate> translatedB3 = b3.translateBallot("4,3,2,1", 4, listOfCandidates);

        b1.setFormattedBallot(translatedB1);
        b2.setFormattedBallot(translatedB2);
        b3.setFormattedBallot(translatedB3);

        b1.setInputtedBallot("4,3,2,1");
        b2.setInputtedBallot("4,3,2,1");
        b3.setInputtedBallot("4,3,2,1");
  

        // Assign ballots to last preferred candidate
        c4.addToBallotList(b1);
        c4.addToBallotList(b2);
        c4.addToBallotList(b3);

        // Create an IRElection instance and set properties
        IRElection elec = new IRElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(3);

        // Run the election
        elec.runElection();

        // Collect invalidated ballots
        ArrayList<Ballot> invalidatedBallots = elec.getInvalidatedBallots();

        System.out.println(invalidatedBallots);
        // Assert that all ballots are invalidated
        //Assert.assertEquals(3, invalidatedBallots.size());
        // Assert.assertTrue(invalidatedBallots.contains(b1));
        // Assert.assertTrue(invalidatedBallots.contains(b2));
        // Assert.assertTrue(invalidatedBallots.contains(b3));
    }

    // TODO: Does not work
    @Test
    public void checkGetInvalidatedBallots() {
        Candidate c1 = new Candidate("Rosen");
        Candidate c2 = new Candidate("Kleinberg");
        Candidate c3 = new Candidate("Chou");
        Candidate c4 = new Candidate("Royce");
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);

        //ArrayList<Party> listOfParties = new ArrayList<>();
        HashMap<String, Party> listOfParties = new HashMap<>();
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        Party p4 = new Party("L");
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);
        listOfParties.put("L", p4);

        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p2);
        c3.assignCandidateToParty(p3);
        c4.assignCandidateToParty(p4);
        
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();
        Ballot b3 = new Ballot();
        Ballot b4 = new Ballot();
        Ballot b5 = new Ballot();
        Ballot b6 = new Ballot();
        ArrayList<Candidate> translatedB1 = b1.translateBallot("1,3,4,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB2 = b2.translateBallot("1,3,2,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB3 = b3.translateBallot(",,1,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB4 = b4.translateBallot("2,1,3,4", 4, listOfCandidates);
        ArrayList<Candidate> translatedB5 = b5.translateBallot(",2,,1", 4, listOfCandidates);
        ArrayList<Candidate> translatedB6 = b6.translateBallot(",1,,2", 4, listOfCandidates);
        
        b1.setFormattedBallot(translatedB1);
        b2.setFormattedBallot(translatedB2);
        b3.setFormattedBallot(translatedB3);
        b4.setFormattedBallot(translatedB4);
        b5.setFormattedBallot(translatedB5);
        b6.setFormattedBallot(translatedB6);

        b1.setInputtedBallot("1,3,4,2");
        b2.setInputtedBallot("1,,2,");
        b3.setInputtedBallot(",,1,2");
        b4.setInputtedBallot("2,1,3,4");
        b5.setInputtedBallot(",2,,1");
        b6.setInputtedBallot(",1,,2");
        
       c1.addToBallotList(b1);
       c1.addToBallotList(b2);
       c3.addToBallotList(b3);
       c2.addToBallotList(b4);
       c4.addToBallotList(b5);
       c2.addToBallotList(b6);
        
        IRElection elec = new IRElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(6);
        elec.runElection();
        Assert.assertEquals(",,1,2", elec.getInvalidatedBallots().get(0).getInputtedBallot());
    }

    @Test
    public void checkDisplayIRElectionResults() {
        Candidate c1 = new Candidate("Rosen");
        Candidate c2 = new Candidate("Kleinberg");
        Candidate c3 = new Candidate("Chou");
        Candidate c4 = new Candidate("Royce");
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);

        //ArrayList<Party> listOfParties = new ArrayList<>();
        HashMap<String, Party> listOfParties = new HashMap<>();
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        Party p4 = new Party("L");
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);
        listOfParties.put("L", p4);

        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p2);
        c3.assignCandidateToParty(p3);
        c4.assignCandidateToParty(p4);
        
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();
        Ballot b3 = new Ballot();
        Ballot b4 = new Ballot();
        Ballot b5 = new Ballot();
        Ballot b6 = new Ballot();
        ArrayList<Candidate> translatedB1 = b1.translateBallot("1,3,4,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB2 = b2.translateBallot("1,,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB3 = b3.translateBallot("1,2,3,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB4 = b4.translateBallot("3,2,1,4", 4, listOfCandidates);
        ArrayList<Candidate> translatedB5 = b5.translateBallot(",,1,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB6 = b6.translateBallot(",,,1", 4, listOfCandidates);
        
        b1.setFormattedBallot(translatedB1);
        b2.setFormattedBallot(translatedB2);
        b3.setFormattedBallot(translatedB3);
        b4.setFormattedBallot(translatedB4);
        b5.setFormattedBallot(translatedB5);
        b6.setFormattedBallot(translatedB6);

        b1.setInputtedBallot("1,3,4,2");
        b2.setInputtedBallot("1,,2");
        b3.setInputtedBallot("1,2,3,");
        b4.setInputtedBallot("3,2,1,4");
        b5.setInputtedBallot(",,1,2");
        b6.setInputtedBallot(",,,1");
        
       c1.addToBallotList(b1);
       c1.addToBallotList(b2);
       c1.addToBallotList(b3);
       c3.addToBallotList(b4);
       c3.addToBallotList(b5);
       c3.addToBallotList(b6);
        
        
        IRElection elec = new IRElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(6);
        elec.runElection();
        //manual check
        elec.displayIRElectionResults();

    }


    @Test
    public void testMajorityInFirstRound() {
        // Create 4 Candidates
        Candidate c1 = new Candidate("Rosen");
        Candidate c2 = new Candidate("Kleinberg");
        Candidate c3 = new Candidate("Chou");
        Candidate c4 = new Candidate("Royce");

        // Add candidates to the list
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);

        // Create Parties and add to the list
        HashMap<String, Party> listOfParties = new HashMap<>();
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        Party p4 = new Party("L");
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);
        listOfParties.put("L", p4);

        // Assign candidates to respective parties
        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p2);
        c3.assignCandidateToParty(p3);
        c4.assignCandidateToParty(p4);

        // Create 6 Ballots and Translate Them
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();
        Ballot b3 = new Ballot();
        Ballot b4 = new Ballot();
        Ballot b5 = new Ballot();
        Ballot b6 = new Ballot();

        ArrayList<Candidate> translatedB1 = b1.translateBallot("1,3,4,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB2 = b2.translateBallot("1,,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB3 = b3.translateBallot("1,2,3,", 4, listOfCandidates);
        ArrayList<Candidate> translatedB4 = b4.translateBallot("3,2,1,4", 4, listOfCandidates);
        ArrayList<Candidate> translatedB5 = b5.translateBallot(",,1,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB6 = b6.translateBallot(",,,1", 4, listOfCandidates);

        b1.setFormattedBallot(translatedB1);
        b2.setFormattedBallot(translatedB2);
        b3.setFormattedBallot(translatedB3);
        b4.setFormattedBallot(translatedB4);
        b5.setFormattedBallot(translatedB5);
        b6.setFormattedBallot(translatedB6);

        b1.setInputtedBallot("1,3,4,2");
        b2.setInputtedBallot("1,,2");
        b3.setInputtedBallot("1,2,3,");
        b4.setInputtedBallot("3,2,1,4");
        b5.setInputtedBallot(",,1,2");
        b6.setInputtedBallot(",,,1");

        // Assign ballots to each candidate based on their ranking #1
        c1.addToBallotList(b1);
        c1.addToBallotList(b2);
        c1.addToBallotList(b3);
        c1.addToBallotList(b4);
        c2.addToBallotList(b5);
        c3.addToBallotList(b6);

        // Run the IR election
        IRElection elec = new IRElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(6);
        elec.runElection();

        // Assert that c1 (Rosen) is declared the winner immediately
        Assert.assertEquals("Rosen", elec.getWinner().getName());
    }

    @Test
    public void testMajorityInLaterRounds() {
        // Create Candidates
        Candidate c1 = new Candidate("Rosen");
        Candidate c2 = new Candidate("Kleinberg");
        Candidate c3 = new Candidate("Chou");
        Candidate c4 = new Candidate("Royce");

        // Add candidates to list
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);

        // Create Parties and assign candidates
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        Party p4 = new Party("L");
        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p2);
        c3.assignCandidateToParty(p3);
        c4.assignCandidateToParty(p4);

        // Create a map of party names to Party objects
        HashMap<String, Party> listOfParties = new HashMap<>();
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);
        listOfParties.put("L", p4);

        // Create and translate ballots
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();
        Ballot b3 = new Ballot();
        Ballot b4 = new Ballot();
        Ballot b5 = new Ballot();
        Ballot b6 = new Ballot();
        ArrayList<Candidate> translatedB1 = b1.translateBallot("1,3,4,2", 4, listOfCandidates);
        ArrayList<Candidate> translatedB2 = b2.translateBallot("2,1,3,4", 4, listOfCandidates);
        ArrayList<Candidate> translatedB3 = b3.translateBallot("3,2,1,4", 4, listOfCandidates);
        ArrayList<Candidate> translatedB4 = b4.translateBallot("4,2,3,1", 4, listOfCandidates);
        ArrayList<Candidate> translatedB5 = b5.translateBallot("2,1,4,3", 4, listOfCandidates);
        ArrayList<Candidate> translatedB6 = b6.translateBallot("3,4,1,2", 4, listOfCandidates);

        b1.setFormattedBallot(translatedB1);
        b2.setFormattedBallot(translatedB2);
        b3.setFormattedBallot(translatedB3);
        b4.setFormattedBallot(translatedB4);
        b5.setFormattedBallot(translatedB5);
        b6.setFormattedBallot(translatedB6);

        b1.setInputtedBallot("1,3,4,2");
        b2.setInputtedBallot("2,1,3,4");
        b3.setInputtedBallot("3,2,1,4");
        b4.setInputtedBallot("4,2,1,3");
        b5.setInputtedBallot("2,1,4,3");
        b6.setInputtedBallot("3,4,1,2");

        // Assign ballots to candidates based on their first-choice ranking
        c1.addToBallotList(b1);
        c2.addToBallotList(b2);
        c3.addToBallotList(b3);
        c4.addToBallotList(b4);
        c2.addToBallotList(b5);
        c3.addToBallotList(b6);

        // Initialize IRElection and set properties
        IRElection elec = new IRElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(6);

        // Run the election
        elec.runElection();

        // Assert the correct winner is found after several rounds
        // Not always true, breaks tie between Kleinberg and Royce
        Assert.assertEquals("Kleinberg", elec.getWinner().getName());
    }

    @Test
    public void testTieInLastPlace() {
        // Create 4 Candidates
        Candidate c1 = new Candidate("Rosen");
        Candidate c2 = new Candidate("Kleinberg");
        Candidate c3 = new Candidate("Chou");
        Candidate c4 = new Candidate("Royce");

        // Add candidates to the list
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);

        // Create parties and assign to candidates
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        Party p4 = new Party("L");
        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p2);
        c3.assignCandidateToParty(p3);
        c4.assignCandidateToParty(p4);

        // Create a map of party names to Party objects
        HashMap<String, Party> listOfParties = new HashMap<>();
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);
        listOfParties.put("L", p4);

        // Create ballots with a tie in the last place
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();
        Ballot b3 = new Ballot();
        Ballot b4 = new Ballot();
        Ballot b5 = new Ballot();
        Ballot b6 = new Ballot();

        ArrayList<Candidate> translatedB1 = b1.translateBallot("1,2,3,4", 4, listOfCandidates);
        ArrayList<Candidate> translatedB2 = b2.translateBallot("2,1,3,4", 4, listOfCandidates);
        ArrayList<Candidate> translatedB3 = b3.translateBallot("3,2,1,4", 4, listOfCandidates);
        ArrayList<Candidate> translatedB4 = b4.translateBallot("4,1,2,3", 4, listOfCandidates);
        ArrayList<Candidate> translatedB5 = b5.translateBallot("1,3,2,4", 4, listOfCandidates);
        ArrayList<Candidate> translatedB6 = b6.translateBallot("2,3,4,1", 4, listOfCandidates);

        b1.setFormattedBallot(translatedB1);
        b2.setFormattedBallot(translatedB2);
        b3.setFormattedBallot(translatedB3);
        b4.setFormattedBallot(translatedB4);
        b5.setFormattedBallot(translatedB5);
        b6.setFormattedBallot(translatedB6);

        b1.setInputtedBallot("1,2,3,4");
        b2.setInputtedBallot("2,1,3,4");
        b3.setInputtedBallot("3,2,1,4");
        b4.setInputtedBallot("4,1,2,3");
        b5.setInputtedBallot("1,3,2,4");
        b6.setInputtedBallot("2,3,4,1");


        // Assign ballots to each candidate based on their first-choice ranking
        c1.addToBallotList(b1);
        c2.addToBallotList(b2);
        c3.addToBallotList(b3);
        c4.addToBallotList(b4);
        c1.addToBallotList(b5);
        c2.addToBallotList(b6);

        // Create an IRElection instance and set properties
        IRElection elec = new IRElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(6);

        // Run the election
        elec.runElection();

        // Assert that a winner is declared even with a tie in the last place
        Assert.assertNotNull(elec.getWinner());
    }

    /**
     * Tests the findInvalidBallots() method of the IRElection class.
     * The test creates an election scenario with both valid and invalid ballots,
     * where invalid ballots are those that do not rank at least half of the
     * candidates (rounded up). The method should identify and return these invalid ballots.
     */
    @Test
    public void testFindInvalidBallots() {
     
        // Setup election and candidates
        IRElection election = new IRElection();
        ArrayList<Candidate> candidates = new ArrayList<>();
        int totalCandidates = 4; // Total number of candidates in the election

        for (int i = 1; i <= totalCandidates; i++) {
            candidates.add(new Candidate("Candidate " + i));
        }

        election.setCandidateList(candidates);
        election.setBallotCount(4);
        election.setCandidatesCount(4);

        // Create some ballots, some of which do not rank at least 2 candidates
        Ballot validBallot1 = new Ballot();
        Ballot validBallot2 = new Ballot();
        Ballot invalidBallot1 = new Ballot();
        Ballot invalidBallot2 = new Ballot();

        ArrayList<Candidate> formattedvalidBallot1 = validBallot1.translateBallot("1,2,,", totalCandidates, candidates);
        ArrayList<Candidate> formattedvalidBallot2 = validBallot2.translateBallot("1,2,,", totalCandidates, candidates);
        ArrayList<Candidate> formattedinvalidBallot1 = invalidBallot1.translateBallot("1,,,", totalCandidates, candidates);
        ArrayList<Candidate> formattedinvalidBallot2 = invalidBallot2.translateBallot(",,1,", totalCandidates, candidates);

        validBallot1.setFormattedBallot(formattedvalidBallot1);
        validBallot2.setFormattedBallot(formattedvalidBallot2);
        invalidBallot1.setFormattedBallot(formattedinvalidBallot1);
        invalidBallot2.setFormattedBallot(formattedinvalidBallot2);

        // Assign these ballots to candidates
        candidates.get(0).addToBallotList(validBallot1);
        candidates.get(0).addToBallotList(validBallot2);
        candidates.get(0).addToBallotList(invalidBallot1);
        candidates.get(2).addToBallotList(invalidBallot2);

        // Run the method to find invalid ballots
        ArrayList<Ballot> invalidBallots = election.findInvalidBallots();

        // Check the correctness of the method
        Assert.assertEquals("Number of invalid ballots should be 2", 2, invalidBallots.size());
        Assert.assertTrue("Contains invalidBallot1", invalidBallots.contains(invalidBallot1));
        Assert.assertTrue("Contains invalidBallot2", invalidBallots.contains(invalidBallot2));
        

        /*
         * //Create 4 Candidates
        Candidate c1 = new Candidate("Rosen");
        Candidate c2 = new Candidate("Kleinberg");
        Candidate c3 = new Candidate("Chou");
        Candidate c4 = new Candidate("Royce");

        //Add candidates to listOfCandidates to pass into translateBallot()
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(c1);
        listOfCandidates.add(c2);
        listOfCandidates.add(c3);
        listOfCandidates.add(c4);

        //Create a Map to attribute string names of parties to Party objects + add to listOfParties
        HashMap<String, Party> listOfParties = new HashMap<>();
        Party p1 = new Party("D");
        Party p2 = new Party("R");
        Party p3 = new Party("I");
        Party p4 = new Party("L");
        listOfParties.put("D", p1);
        listOfParties.put("R", p2);
        listOfParties.put("I", p3);
        listOfParties.put("L", p4);

        //Assign each candidate to respective party
        c1.assignCandidateToParty(p1);
        c2.assignCandidateToParty(p2);
        c3.assignCandidateToParty(p3);
        c4.assignCandidateToParty(p4);

        // Create some ballots, some of which do not rank at least 2 candidates
        Ballot validBallot1 = new Ballot();
        Ballot validBallot2 = new Ballot();
        Ballot invalidBallot1 = new Ballot();
        Ballot invalidBallot2 = new Ballot();

        ArrayList<Candidate> formattedvalidBallot1 = validBallot1.translateBallot("1,2,,", totalCandidates, candidates);
        ArrayList<Candidate> formattedvalidBallot2 = validBallot2.translateBallot("1,2,,", totalCandidates, candidates);
        ArrayList<Candidate> formattedinvalidBallot1 = invalidBallot1.translateBallot("1,,,", totalCandidates, candidates);
        ArrayList<Candidate> formattedinvalidBallot2 = invalidBallot2.translateBallot(",,1,", totalCandidates, candidates);
        
        validBallot1.setFormattedBallot(formattedvalidBallot1);
        validBallot2.setFormattedBallot(formattedvalidBallot2);
        invalidBallot1.setFormattedBallot(formattedinvalidBallot1);
        invalidBallot2.setFormattedBallot(formattedinvalidBallot2);

        // Assign these ballots to candidates
        listOfCandidates.get(0).addToBallotList(validBallot1);
        listOfCandidates.get(0).addToBallotList(validBallot2);
        listOfCandidates.get(0).addToBallotList(invalidBallot1);
        listOfCandidates.get(2).addToBallotList(invalidBallot2);

        //Create election and attribute candidate list, party list, ballot count to object. Then run election. 
        IRElection elec = new IRElection();
        elec.setCandidateList(listOfCandidates);
        elec.setPartyList(listOfParties);
        elec.setBallotCount(4);
        elec.runElection();
         */
    }
    

}
