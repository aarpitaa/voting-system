import org.junit.jupiter.api.Test;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.*;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileProcessorTest {
    
    // Testing to check if the createAuditFile method generates a file name with the current date and time
    @Test
    public void testCreateAuditFile() {
        FileProcessor fileProcessor = new FileProcessor();

        String result = fileProcessor.createAuditFile();
        
        // Manual Check 
        System.out.println("Result: " + result);
       
    }


    // OPL Election Specific Test:
    // Test reading the header for an OPL election.
    // Expected behavior: readHeader initializes OPLElection attributes based on the input file data.
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
            assertEquals(candidate.getCandidateParty(), oplElection.getPartyList().get(candidate.getName()));
        }
    }


	// Alternative Election Type Test:
    // Test reading an input file for an IR election.
    // Expected behavior: readHeader initializes an IRElection object.
    @Test
    public void testReadHeaderForIRElection1() {
        // Arrange
        FileProcessor fileProcessor = new FileProcessor();
        fileProcessor.setInputFile("IRTest.csv");

        // Act
        ElectionType electionType = fileProcessor.readHeader(fileProcessor.getInputFile());

        // Assert
        assertTrue(electionType instanceof IRElection);

        assertEquals(4, irElection.getCandidatesCount());
        assertEquals(6, irElection.getBallotCount());

        ArrayList<Candidate> candidateList = irElection.getCandidateList();
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
    
    // Create Candidates and Parties Test:
    // Test creating candidates and parties based on input data.
    // Expected behavior: createCandidatesAndParties sets up candidates and parties correctly.
    @Test
    public void testCreateCandidatesAndParties() {
        // Arrange
        FileProcessor fileProcessor = new FileProcessor();
        OPLElection oplelection = new OPLElection();
        String inputLine = "Pike (D), Foster (D), Deutsch (R), Borg (R), Jones (R), Smith (I)";

        // Act
        fileProcessor.createCandidatesAndParties(inputLine, irelection);

        // Assert
        // Verify that candidates and parties are created correctly in the IRElection object
        assertNotNull(irelection.getCandidateList());
        assertNotNull(irelection.getPartyList());

        // Validate the number of candidates and parties created
        assertEquals(6, irelection.getCandidateList().size());
        assertEquals(3, irelection.getPartyList().size());

        // Validate the details of specific candidates and parties
        HashMap<String, Party> partyMap = irelection.getPartyList();
        ArrayList<Candidate> candidateList = irelection.getCandidateList();

        // Verify Pike
        Candidate pike = candidateList.stream().filter(c -> c.getName().equals("Pike")).findFirst().orElse(null);
        assertNotNull(pike);
        assertEquals("D", pike.getCandidateParty().getName());

        // Verify Foster
        Candidate foster = candidateList.stream().filter(c -> c.getName().equals("Foster")).findFirst().orElse(null);
        assertNotNull(foster);
        assertEquals("D", foster.getCandidateParty().getName());

        // Verify Deutsch
        Candidate deutsch = candidateList.stream().filter(c -> c.getName().equals("Deutsch")).findFirst().orElse(null);
        assertNotNull(deutsch);
        assertEquals("R", deutsch.getCandidateParty().getName());

        // Verify Borg
        Candidate borg = candidateList.stream().filter(c -> c.getName().equals("Borg")).findFirst().orElse(null);
        assertNotNull(borg);
        assertEquals("R", borg.getCandidateParty().getName());

        // Verify Jones
        Candidate jones = candidateList.stream().filter(c -> c.getName().equals("Jones")).findFirst().orElse(null);
        assertNotNull(jones);
        assertEquals("R", jones.getCandidateParty().getName());

        // Verify Smith
        Candidate smith = candidateList.stream().filter(c -> c.getName().equals("Smith")).findFirst().orElse(null);
        assertNotNull(smith);
        assertEquals("I", smith.getCandidateParty().getName());
    }
    
    // IR Election Specific Test:
    // Test reading the remaining lines for an IR election.
    // Expected behavior: readRemainingIR processes the lines and updates the IRElection object.
    @Test
    public void testReadRemainingIR() {
        // Arrange
        FileProcessor fileProcessor = new FileProcessor();
        IRElection irelection = new IRElection();

        // Act
        fileProcessor.readRemainingIR(irelection, "IRTest.csv");

        // Assertions for the expected behavior.

        // Verify candidates and their ballots
        Candidate rosen = irelection.getCandidateByName("Rosen");
        Candidate kleinberg = irelection.getCandidateByName("Kleinberg");
        Candidate chou = irelection.getCandidateByName("Chou");
        Candidate royce = irelection.getCandidateByName("Royce");

        assertEquals(3, rosen.getBallotCount());
        assertEquals(2, chou.getBallotCount());
        assertEquals(1, royce.getBallotCount());
        assertTrue(kleinberg.getBallots().isEmpty()); // Kleinberg should have no ballots

        // Verify the ballots' content
        assertEquals("1,3,4,2", rosen.getBallots().get(0).getInputtedBallot());
        assertEquals("1,,2,", chou.getBallots().get(0).getInputtedBallot());
        assertEquals("1,2,3,", rosen.getBallots().get(1).getInputtedBallot());
        assertEquals("3,2,1,4", chou.getBallots().get(1).getInputtedBallot());
        assertEquals(",,1,2", rosen.getBallots().get(2).getInputtedBallot());
        assertEquals(",,,1", royce.getBallots().get(0).getInputtedBallot());

        // Verify the winner
        assertEquals(rosen, irelection.getWinner());
    }

    // OPL Election Specific Test:
    // Test reading the remaining lines for an OPL election.
    // Expected behavior: readRemainingOPL processes the lines and updates the OPLElection object.
    @Test
    public void testReadRemainingOPL() {
        
        // Arrange
        FileProcessor fileProcessor = new FileProcessor();
        OPLElection oplElection = new OPLElection();

        // Act
        fileProcessor.readRemainingOPL(oplelection, "OPLElection.csv");

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
            assertEquals(expectedVotes.get(candidate), candidate.getBallotCount());
        }

        // Validate party votes
        HashMap<Party, Integer> expectedPartyVotes = new HashMap<>();
        expectedPartyVotes.put(new Party("D"), 5);
        expectedPartyVotes.put(new Party("R"), 2);
        expectedPartyVotes.put(new Party("I"), 2);

        for (Party party : oplElection.getPartyList().values()) {
            assertEquals(expectedPartyVotes.get(party), party.getVoteCount());
        }
    }
    

    
    
    
    
}
