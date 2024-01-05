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

        // listOfParties.add(p1);
        // listOfParties.add(p2);
        // listOfParties.add(p3);
        // listOfParties.add(p4);

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

        // listOfParties.add(p1);
        // listOfParties.add(p2);
        // listOfParties.add(p3);
        // listOfParties.add(p4);

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

        // listOfParties.add(p1);
        // listOfParties.add(p2);
        // listOfParties.add(p3);
        // listOfParties.add(p4);
        
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
 
         // listOfParties.add(p1);
         // listOfParties.add(p2);
         // listOfParties.add(p3);
         // listOfParties.add(p4);

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
        Assert.assertEquals("[1,2,,],[,,,1]", elec.getInvalidatedBallots());
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

        // listOfParties.add(p1);
        // listOfParties.add(p2);
        // listOfParties.add(p3);
        // listOfParties.add(p4);

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
}
