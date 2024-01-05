import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 *  OPLElectionTests.java
 * Testing OPLElection functions: testNumberOfSeats(), testRunOPLElection(), testTieBreakerOPL(), setUpElection().
 * @author Neha Bhatia
 */

public class OPLElectionTests {

  Party pD;
  Party pR;
  Party pA;

  @Test
  public void testNumberOfSeats() {
     OPLElection elec = new OPLElection();
    elec.setNumberOfSeats(10);
    Assert.assertEquals(10, elec.getNumberOfSeats());

    Exception exception = assertThrows(
      IllegalArgumentException.class,
      () -> {
        elec.setNumberOfSeats(-10);
      }
    );

    Assert.assertTrue(
      "Number of seats cannot be negative".contains(exception.getMessage())
    );
  }

  @Test
  public void testRunOPLElection() {
     OPLElection elec = new OPLElection();
    Party pD = new Party("D");
    Party pR = new Party("R");
    Party pA = new Party("A");

    Candidate a = new Candidate("a");
    Candidate b = new Candidate("b");
    a.assignCandidateToParty(pD);
    b.assignCandidateToParty(pD);
    pD.addCandidateToList(a);
    pD.addCandidateToList(b);

    Candidate c = new Candidate("c");
    Candidate d = new Candidate("d");
    c.assignCandidateToParty(pR);
    d.assignCandidateToParty(pR);
    pR.addCandidateToList(c);
    pR.addCandidateToList(d);

    Candidate e = new Candidate("e");
    Candidate f = new Candidate("f");
    e.assignCandidateToParty(pA);
    f.assignCandidateToParty(pA);
    pA.addCandidateToList(e);
    pA.addCandidateToList(f);

    elec.setNumberOfSeats(4);
    elec.setBallotCount(12);

    // all parties get 2 votes
    for (int i = 0; i < 2; i++) {
      // 2 votes
      pD.incrementVoteCount();

      // 4 votes
      pR.incrementVoteCount();
      pR.incrementVoteCount();

      // 6 votes
      pA.incrementVoteCount();
      pA.incrementVoteCount();
      pA.incrementVoteCount();

      a.incrementBallotCount(); // a has 2 votes
      c.incrementBallotCount(); // c has 2 votes
      f.incrementBallotCount();
      f.incrementBallotCount(); // f has 4 votes
    }

    System.out.println(pD.getVoteCount());
    System.out.println(pR.getVoteCount());
    System.out.println(pA.getVoteCount());

    c.incrementBallotCount(); // 3 votes

    d.incrementBallotCount(); // d has 1 vote

    e.incrementBallotCount(); // e has 1 vote

    f.incrementBallotCount(); // f has 5 votes

    Map<String, Party> parties = new HashMap<>();
    parties.put("D", pD);
    parties.put("R", pR);
    parties.put("A", pA);

    ArrayList<Candidate> candidates = new ArrayList<Candidate>();
    candidates.add(a);
    candidates.add(b);
    candidates.add(c);
    candidates.add(d);
    candidates.add(e);
    candidates.add(f);

    elec.setCandidateList(candidates);
    elec.setPartyList(parties);

    elec.runElection();

    Assert.assertEquals(1, pD.getPartySeatCount());
    Assert.assertEquals(1, pR.getPartySeatCount());
    Assert.assertEquals(2, pA.getPartySeatCount());

    Assert.assertTrue(pD.getCandidateCount() >= pD.getPartySeatCount());
    Assert.assertTrue(pR.getCandidateCount() >= pR.getPartySeatCount());
    Assert.assertTrue(pA.getCandidateCount() >= pA.getPartySeatCount());

    ((OPLElection) elec).displayElectionResults();
  }

  @Test
  public void testTieBreakerOPL() {

   int sum_pD = 0;
   int sum_pA = 0;

   for(int i = 0; i < 100; i++){
      OPLElection elec = setUpElection();
      elec.runElection();
      Assert.assertEquals(2, pR.getPartySeatCount());
      if(pD.getPartySeatCount() == 1 && pA.getPartySeatCount() == 0){
         sum_pD++;
      }
      else if(pD.getPartySeatCount() == 0 && pA.getPartySeatCount() == 1){
         sum_pA++;
      }

      
   }
      Assert.assertEquals(100, sum_pA + sum_pD);

      System.out.println("Percentage of pR getting 2 seats: " + sum_pD);
      System.out.println("Percentage of pA getting 2 seats: " + sum_pA);

    
    
  }

  public OPLElection setUpElection(){

    OPLElection elec = new OPLElection();
    pD = new Party("D");
    pR = new Party("R");
    pA = new Party("A");

    Candidate a = new Candidate("a");
    Candidate b = new Candidate("b");
    a.assignCandidateToParty(pD);
    b.assignCandidateToParty(pD);
    pD.addCandidateToList(a);
    pD.addCandidateToList(b);

    Candidate c = new Candidate("c");
    Candidate d = new Candidate("d");
    c.assignCandidateToParty(pR);
    d.assignCandidateToParty(pR);
    pR.addCandidateToList(c);
    pR.addCandidateToList(d);

    Candidate e = new Candidate("e");
    Candidate f = new Candidate("f");
    e.assignCandidateToParty(pA);
    f.assignCandidateToParty(pA);
    pA.addCandidateToList(e);
    pA.addCandidateToList(f);

    elec.setNumberOfSeats(3);
    elec.setBallotCount(12);

    // quota = 12/3 = 4
    // D, R, A
    // 2, 8, 2

    // 0, 2, 0
    // 1, 2, 0
    // 0, 2, 1

    // all parties get 2 votes
    for (int i = 0; i < 2; i++) {
      // 2 votes
      pD.incrementVoteCount();

      // 8 votes
      pR.incrementVoteCount();
      pR.incrementVoteCount();
      pR.incrementVoteCount();
      pR.incrementVoteCount();

      // 2 votes
      pA.incrementVoteCount();

      a.incrementBallotCount(); // a has 2 votes

      c.incrementBallotCount(); // c has 6 votes
      c.incrementBallotCount();
      c.incrementBallotCount();

      d.incrementBallotCount(); // d has 2 votes

      f.incrementBallotCount(); // f has 2 votes
    }

    Map<String, Party> parties = new HashMap<>();
    parties.put("D", pD);
    parties.put("R", pR);
    parties.put("A", pA);

    ArrayList<Candidate> candidates = new ArrayList<Candidate>();
    elec.setCandidateList(candidates);
    elec.setPartyList(parties);

    return elec;

  }

}
