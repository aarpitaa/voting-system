import org.junit.*;
import org.junit.jupiter.api.Before;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;

/**
 * Testing serveral cases for Ballot.
 * @author Arpita Dev
 */

public class BallotTest {

    private ArrayList<Candidate> allCandidates;

    @Before
    public void setUp() {
        allCandidates = new ArrayList<>();
        allCandidates.add(new Candidate("Rosen"));
        allCandidates.add(new Candidate("Kleinberg"));
        allCandidates.add(new Candidate("Chou"));
        allCandidates.add(new Candidate("Royce"));
    }


    @Test
    public void testTranslateBallotCorrectInput() {
        Ballot ballot = new Ballot();
        ArrayList<Candidate> translatedBallot = ballot.translateBallot("1,2,3,4", 4, allCandidates);
        
        Assert.assertEquals(4, translatedBallot.size());
        Assert.assertEquals("Rosen", translatedBallot.get(0).getName());
        Assert.assertEquals("Kleinberg", translatedBallot.get(1).getName());
    }


    @Test
    public void testTranslateBallotEmptyBallot() {
        Ballot ballot = new Ballot();
        ArrayList<Candidate> translatedBallot = ballot.translateBallot(",,,,", 4, allCandidates);

        Assert.assertEquals(4, translatedBallot.size());
        for (Candidate candidate : translatedBallot) {
            Assert.assertNull(candidate);
        }
    }


    @Test
    public void testTranslateBallotWithMissingPreferences() {
        Ballot ballot = new Ballot();
        ArrayList<Candidate> translatedBallot = ballot.translateBallot("1,,3,", 4, allCandidates);

        Assert.assertEquals(4, translatedBallot.size());
        Assert.assertNull(translatedBallot.get(1)); // Missing preference should result in null
        Assert.assertEquals("Chou", translatedBallot.get(2).getName());
    }


    @Test
    public void testTranslateBallotWithInvalidInput() {
        Ballot ballot = new Ballot();
        assertThrows(NumberFormatException.class, () -> {
            ballot.translateBallot("1,invalid,3,4", 4, allCandidates);
        });
    }



    @Test
    public void testIncrementRankingDigit() {
        Ballot ballot = new Ballot();
        ballot.incrementRankingDigit();
        Assert.assertEquals(1, ballot.getBallotRankingDigit());
    }


    @Test
    public void testGetNextCandidate() {
        Ballot ballot = new Ballot();
        ArrayList<Candidate> formattedBallot = new ArrayList<>(Arrays.asList(allCandidates.get(0), allCandidates.get(1)));
        ballot.setFormattedBallot(formattedBallot);

        Candidate nextCandidate = ballot.getNextCandidate();
        Assert.assertEquals("Rosen", nextCandidate.getName());
        Assert.assertEquals(1, ballot.getFormattedBallot().size()); // Ensure the ballot list is decremented
    }
}
