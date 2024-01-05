import java.beans.Transient;
import java.util.ArrayList;
import org.junit.*;

/**
 * Testing serveral cases for Candidate.
 * @author Stuti Arora
 */

public class CandidateTesting {
    
    @Test
    public void checkConstructor() {
        Candidate test1 = new Candidate("test1");
        Assert.assertEquals("test1", test1.getName());
        Assert.assertEquals(0, test1.getBallotCount());
        Assert.assertEquals(null, test1.getCandidateParty());
        Assert.assertTrue(test1.getBallots().isEmpty());
        Assert.assertEquals(true, test1.isInElection());
    }

    @Test
    public void checkAssignCandidateToParty() {
        Candidate can2 = new Candidate("can2");
        Party party1 = new Party("L");
        can2.assignCandidateToParty(party1);
        Assert.assertEquals("L", can2.getCandidateParty().getName());
    }

    @Test 
    public void checkRemoveCandidateFromElection() {
        Candidate can3 = new Candidate("can3");
        can3.removeCandidateFromElection();
        Assert.assertEquals(false, can3.isInElection());
    }

    @Test
    public void checkRemoveBallot() {
        Candidate can4 = new Candidate("can4");
        // Candidate can5 = new Candidate("can4");
        ArrayList<Candidate> listOfCandidates = new ArrayList<Candidate>();
        listOfCandidates.add(can4);
        // listOfCandidates.add(can5);

        //make ballot, translate it, set formatted ballot
        Ballot b1 = new Ballot();

        ArrayList<Candidate> translatedB1 = b1.translateBallot("1,2", 2, listOfCandidates);
        b1.setFormattedBallot(translatedB1);

        //attribute them to candidate
        can4.addToBallotList(b1);
        
        //remove ballot
        can4.removeBallot();

        //check the rest of the list 
        Assert.assertEquals(0, can4.getBallotCount());
    }

    @Test
    public void checkAddToBallotList() {
        Candidate can5 = new Candidate("can5");
        ArrayList<Candidate> listOfCandidates = new ArrayList<>();
        listOfCandidates.add(can5);

        Ballot b2 = new Ballot();
        ArrayList<Candidate> translatedB2 = b2.translateBallot("1", 1, listOfCandidates);
        b2.setFormattedBallot(translatedB2);

        can5.addToBallotList(b2);
        Assert.assertEquals(1, can5.getBallotCount());
    }
}
