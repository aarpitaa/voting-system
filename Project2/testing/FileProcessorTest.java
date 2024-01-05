import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * The `FileProcessorTest` class is responsible for testing all methods inside the FileProcessor class
 * With the current set up, automated testing will only run if your lib files for JUnit reside inside the Project2/src folder
 * @author Praful Das, Neha Bhatia
 */

public class FileProcessorTest {

  // Testing to check if the createAuditFile method generates a file name with the current date and time
  @Test
  public void testCreateAuditFile() {
    FileProcessor fileProcessor = new FileProcessor();

    //Manual check in auditFiles folder
    fileProcessor.createAuditFile("OPL");
    fileProcessor.createAuditFile("MPO");
    fileProcessor.createAuditFile("IR");
  }

  /**
   * OPL Election Specific Test:
   * Test reading the header for an OPL election.
   * Expected behavior: readHeader initializes OPLElection attributes based on the input file data.
   * @throws IOException
   */
  @Test
  public void testReadHeaderOPL() throws IOException {
    // Arrange
    FileProcessor fileProcessor = new FileProcessor();
    OPLElection oplElection = new OPLElection();

    // Act
    String inputFile = "OPLElection.csv";
    fileProcessor.setInputFile(inputFile);

    ElectionType electionType = fileProcessor.readHeader();

    // Assert
    assertTrue(electionType instanceof OPLElection);

    // Verify that OPLElection attributes are initialized correctly
    assertEquals(6, oplElection.getCandidatesCount());
    assertEquals(3, oplElection.getPartyList().size());
    assertEquals(9, oplElection.getBallotCount());
    assertEquals(0, oplElection.getNumberOfSeats());

    // Validate candidate list
    ArrayList<Candidate> expectedCandidates = new ArrayList<>();
    expectedCandidates.add(new Candidate("Pike"));
    expectedCandidates.add(new Candidate("Foster"));
    expectedCandidates.add(new Candidate("Deutsch"));
    expectedCandidates.add(new Candidate("Borg"));
    expectedCandidates.add(new Candidate("Jones"));
    expectedCandidates.add(new Candidate("Smith"));

    for (Candidate candidate : expectedCandidates) {
      assertNotNull(oplElection.getPartyList().get(candidate.getName()));
      assertEquals(
        candidate.getCandidateParty(),
        oplElection.getPartyList().get(candidate.getName())
      );
    }
  }

  /**
   * Alternative Election Type Test:
   * Test reading an input file for an IR election.
   * Expected behavior: readHeader initializes an IRElection object.
   *
   * @throws IOException
   */
  @Test
  public void testReadHeaderForIRElection1() throws IOException {
    // Arrange
    FileProcessor fileProcessor = new FileProcessor();
    fileProcessor.setInputFile("IRTest.csv");

    // Act
    ElectionType electionType = fileProcessor.readHeader();

    // Assert
    assertTrue(electionType instanceof IRElection);

    assertEquals(4, electionType.getCandidatesCount());
    assertEquals(6, electionType.getBallotCount());

    ArrayList<Candidate> candidateList = electionType.getCandidateList();
    assertNotNull(candidateList);
    assertEquals(4, candidateList.size());

    // Validate candidate names and parties
    assertEquals("Rosen", candidateList.get(0).getName());
    assertEquals("D", candidateList.get(0).getCandidateParty().getName());

    assertEquals("Kleinberg", candidateList.get(1).getName());
    assertEquals("R", candidateList.get(1).getCandidateParty().getName());

    assertEquals("Chou", candidateList.get(2).getName());
    assertEquals("I", candidateList.get(2).getCandidateParty().getName());

    assertEquals("Royce", candidateList.get(3).getName());
    assertEquals("L", candidateList.get(3).getCandidateParty().getName());
  }

  /**
   * Asserts if readHeaderMPO correctly returns an object of instance MPOElection
   *
   * @throws IOException
   */
  @Test
  public void testReadHeaderMPOElection() throws IOException {
    //Set up
    FileProcessor fp = new FileProcessor();
    fp.setInputFile("MPOElection.csv");

    ElectionType elec = fp.readHeader();

    // Assert
    assertTrue(elec instanceof MPOElection);

    assertEquals(6, elec.getCandidatesCount());
    assertEquals(2, ((MPOElection) elec).getNumberOfSeats());
    assertEquals(9, elec.getBallotCount());
  }

  /**
   * Create Candidates and Parties Test:
   * Test creating candidates and parties based on input data.
   * Expected behavior: createCandidatesAndParties sets up candidates and parties correctly.
   * @throws IOException
   */
  @Test
  public void testCreateCandidatesAndParties() {
    // Arrange
    FileProcessor fileProcessor = new FileProcessor();
    OPLElection oplelection = new OPLElection();
    String inputLine =
      "Pike (D), Foster (D), Deutsch (R), Borg (R), Jones (R), Smith (I)";

    // Act
    fileProcessor.createCandidatesAndParties(inputLine, oplelection);

    // Assert
    // Verify that candidates and parties are created correctly in the IRElection object
    assertNotNull(oplelection.getCandidateList());
    assertNotNull(oplelection.getPartyList());

    // Validate the number of candidates and parties created
    assertEquals(6, oplelection.getCandidateList().size());
    assertEquals(3, oplelection.getPartyList().size());

    // Validate the details of specific candidates and parties
    Map<String, Party> partyMap = oplelection.getPartyList();
    ArrayList<Candidate> candidateList = oplelection.getCandidateList();

    // Verify Pike
    Candidate pike = candidateList
      .stream()
      .filter(c -> c.getName().equals("Pike"))
      .findFirst()
      .orElse(null);
    assertNotNull(pike);
    assertEquals("D", pike.getCandidateParty().getName());

    // Verify Foster
    Candidate foster = candidateList
      .stream()
      .filter(c -> c.getName().equals("Foster"))
      .findFirst()
      .orElse(null);
    assertNotNull(foster);
    assertEquals("D", foster.getCandidateParty().getName());

    // Verify Deutsch
    Candidate deutsch = candidateList
      .stream()
      .filter(c -> c.getName().equals("Deutsch"))
      .findFirst()
      .orElse(null);
    assertNotNull(deutsch);
    assertEquals("R", deutsch.getCandidateParty().getName());

    // Verify Borg
    Candidate borg = candidateList
      .stream()
      .filter(c -> c.getName().equals("Borg"))
      .findFirst()
      .orElse(null);
    assertNotNull(borg);
    assertEquals("R", borg.getCandidateParty().getName());

    // Verify Jones
    Candidate jones = candidateList
      .stream()
      .filter(c -> c.getName().equals("Jones"))
      .findFirst()
      .orElse(null);
    assertNotNull(jones);
    assertEquals("R", jones.getCandidateParty().getName());

    // Verify Smith
    Candidate smith = candidateList
      .stream()
      .filter(c -> c.getName().equals("Smith"))
      .findFirst()
      .orElse(null);
    assertNotNull(smith);
    assertEquals("I", smith.getCandidateParty().getName());
  }

  /**
   * Tests that the candidate list and the associated party list are populated correctly for an MPO object
   * @throws IOException
   */
  @Test
  public void testCreateCandidatesAndPartiesMPO() throws IOException {
    // Set up
    FileProcessor fp = new FileProcessor();
    MPOElection elec = new MPOElection();
    String inputLine =
      "[Pike, D], [Foster, D], [Deutsch, R], [Borg, R], [Jones, R], [Smith, I]";

    // Call function
    fp.createCandidatesAndPartiesMPO(inputLine, elec);

    // Assert
    Map<String, Party> parties = elec.getPartyList();
    ArrayList<Candidate> candidates = elec.getCandidateList();

    /*
        * Expected
        *  
        * Parties: 
            Party  R:
            Candidates in each party: Deutsch( R ), Borg( R ), Jones( R ), 
            Party  D:
            Candidates in each party: Pike( D ), Foster( D ), 
            Party  I:
            Candidates in each party: Smith( I ), 
            
            Candidates: Pike( D ), Foster( D ), Deutsch( R ), Borg( R ), Jones( R ), Smith( I ),
        */

    System.out.print("Parties: ");
    for (String p : parties.keySet()) {
      System.out.println("\nParty " + p + ":");
      System.out.print("Candidates in each party: ");
      for (Candidate c : parties.get(p).getCandidateList()) {
        System.out.print(
          c.getName() + "(" + c.getCandidateParty().getName() + " ), "
        );
      }
    }
    System.out.println();
    System.out.print("Candidates: ");
    for (Candidate c : candidates) {
      System.out.print(
        c.getName() + "(" + c.getCandidateParty().getName() + " ), "
      );
    }
  }

  /**
   * IR Election Specific Test:
   * Test reading the remaining lines for an IR election.
   * Expected behavior: readRemainingIR processes the lines and updates the IRElection object.
   * @throws IOException
   */
  @Test
  public void testReadRemainingIR() throws IOException {
    // Arrange
    FileProcessor fileProcessor = new FileProcessor();
    fileProcessor.setInputFile("tests.csv");
    IRElection irelection = (IRElection) fileProcessor.readHeader();

    // Act
    fileProcessor.readRemainingIR(irelection);

    // Verify candidates and their ballots
    ArrayList<Candidate> candidates = irelection.getCandidateList();

    // Expected
    // Rosen: 3 votes
    // Chou: 2 votes
    // Royce: 1 vote
    // Kleinberg: 0 votes
    System.out.println("Verifying vote count");
    for (Candidate candidate : candidates) {
      System.out.println(
        candidate.getName() + " " + candidate.getBallotCount()
      );
    }

    // Verify the ballots' content
    // Expected output:
    // Rosen: [1,3,4,2] [1,,2,] [1,2,3,]
    // Kleinberg:
    // Chou: [3,2,1,4] [,,1,2]
    // Royce: [,,,1]

    System.out.println("\nVerifying inputted ballots");
    for (Candidate candidate : candidates) {
      System.out.print(candidate.getName() + ": ");

      // for each candidate, get their inputted ballots
      for (Ballot ballot : candidate.getBallots()) {
        System.out.print("[" + ballot.getInputtedBallot() + "] ");
      }
      System.out.println();
    }
  }

  /**
   * OPL Election Specific Test:
   * Test reading the remaining lines for an OPL election.
   * Expected behavior: readRemainingOPL processes the lines and updates the OPLElection object.
   * @throws IOException
   */
  @Test
  public void testReadRemainingOPL() throws IOException {
    // Arrange
    FileProcessor fileProcessor = new FileProcessor();
    fileProcessor.setInputFile("testsOPL.csv");
    OPLElection oplElection = (OPLElection) fileProcessor.readHeader();

    // Act
    fileProcessor.readRemainingOPL(oplElection);

    // Assert
    // Verify that OPLElection object is updated correctly
    assertEquals(5, oplElection.getCandidateList().size()); // Assuming readHeader already adds candidates
    assertEquals(3, oplElection.getPartyList().size()); // Assuming readHeader already adds parties

    // Validate candidate votes
    HashMap<Candidate, Integer> expectedVotes = new HashMap<>();
    expectedVotes.put(new Candidate("Pike"), 3);
    expectedVotes.put(new Candidate("Foster"), 2);
    expectedVotes.put(new Candidate("Deutsch"), 0);
    expectedVotes.put(new Candidate("Borg"), 2);
    expectedVotes.put(new Candidate("Jones"), 1);
    expectedVotes.put(new Candidate("Smith"), 1);

    for (Candidate candidate : oplElection.getCandidateList()) {
      assertEquals(
        (int) expectedVotes.get(candidate),
        candidate.getBallotCount()
      );
    }

    // Validate party votes
    HashMap<Party, Integer> expectedPartyVotes = new HashMap<>();
    expectedPartyVotes.put(new Party("D"), 5);
    expectedPartyVotes.put(new Party("R"), 2);
    expectedPartyVotes.put(new Party("I"), 2);

    for (Party party : oplElection.getPartyList().values()) {
      assertEquals((int) expectedPartyVotes.get(party), party.getVoteCount());
    }
  }

  /**
   * Tests that the ballots are read in and assigned correctly for an MPO object
   * @throws IOException
   */
  @Test
  public void testReadRemainingMPO() throws IOException {
    // Arrange
    FileProcessor fileProcessor = new FileProcessor();
    fileProcessor.setInputFile("MPOElection.csv");
    MPOElection mpoElection = (MPOElection) fileProcessor.readHeader();

    // Act
    fileProcessor.readRemainingMPO(mpoElection);

    // Assert
    // Verify that MPOElection object is updated correctly
    assertEquals(6, mpoElection.getCandidateList().size()); // Assuming readHeader already adds candidates
    assertEquals(3, mpoElection.getPartyList().size()); // Assuming readHeader already adds parties

    // Validate candidate votes
    HashMap<String, Integer> expectedVotes = new HashMap<>();
    expectedVotes.put("Pike", 3);
    expectedVotes.put("Foster", 2);
    expectedVotes.put("Deutsch", 0);
    expectedVotes.put("Borg", 2);
    expectedVotes.put("Jones", 1);
    expectedVotes.put("Smith", 1);

    for (Candidate candidate : mpoElection.getCandidateList()) {
      assertEquals(
        (int) expectedVotes.get(candidate.getName().trim()),
        candidate.getBallotCount()
      );
    }
    // For MPO, party votes do not matter
  }
}
