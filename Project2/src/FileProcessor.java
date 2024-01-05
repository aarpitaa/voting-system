import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * The `FileProcessor` class is responsible for processing input files
 * related to election data, reading and updating election objects, and
 * creating audit files. It contains methods for reading headers, creating
 * candidates and parties, and handling the remaining lines forIR
 * (Instant Runoff), OPL (Open Party List) and MPO (Multiple Popularity Only) elections.
 * @author Praful Das, Neha Bhatia, Stuti Arora
 */
public class FileProcessor {

  private String auditFileName;
  private String inputFile;

  /**
   * Creates a filename for the audit file based on the current date and time and sets the class variable auditFileName to it
   * @param electionType the type of election that is creating the audit file
   */
  public void createAuditFile(String electionType) {

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd_HH-mm-ss"
    );
    String timestamp = now.format(formatter);

    // Create the directory if it doesn't exist
    String folderPath = "auditFiles";
    File directory = new File(folderPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }

    // Set the audit file name with the folder path
    auditFileName = folderPath + File.separator + "audit_" + electionType + "_" + timestamp + ".txt";
    System.out.println("name of created audit file " + auditFileName);
  }

  /**
   * Reads the header of the input file and initializes an ElectionType object.
   * Handles both IR and OPL election types.
   * @return The initialized ElectionType object.
   * @throws IOException If an I/O error occurs.
   */
  public ElectionType readHeader() throws IOException {
    // Create a BufferedReader to read the input file
    try (
      BufferedReader reader = new BufferedReader(new FileReader(inputFile))
    ) {
      // Read the first line from the input file
      String firstLine = reader.readLine();

      ElectionType electionType;

      if (firstLine.equals("IR")) {
        // Initialize an IRElection if the election type is IR
        electionType = new IRElection();
        electionType.setElectionType("IR");
        electionType.setCandidatesCount(Integer.parseInt(reader.readLine()));

        String thirdLine = reader.readLine();
        createCandidatesAndParties(thirdLine, electionType);
      } else if (firstLine.equals("OPL")) {
        // Initialize an OPLElection if the election type is OPL
        electionType = new OPLElection();
        electionType.setElectionType("OPL");
        electionType.setCandidatesCount(Integer.parseInt(reader.readLine()));

        String thirdLine = reader.readLine();
        createCandidatesAndParties(thirdLine, electionType);

        ((OPLElection) electionType).setNumberOfSeats(
            Integer.parseInt(reader.readLine())
          );
        electionType.setBallotCount(Integer.parseInt(reader.readLine()));
      } else if (firstLine.equals("MPO")) {
        // Initialize an MPOElection if the election type is MPO
        electionType = new MPOElection();
        electionType.setElectionType("MPO");
        ((MPOElection) electionType).setNumberOfSeats(
            Integer.parseInt(reader.readLine())
          );
        electionType.setCandidatesCount(Integer.parseInt(reader.readLine()));

        String fourthLine = reader.readLine();
        createCandidatesAndPartiesMPO(fourthLine, electionType);
        electionType.setBallotCount(Integer.parseInt(reader.readLine()));
      } else {
        // Handle an unknown election type or invalid input
        throw new IllegalArgumentException(
          "Invalid election type in the input file."
        );
      }

      return electionType;
    }
  }

  /**
   * Creates candidates and parties based on the input data.
   * Used in the readHeader method.
   * @param line The input line containing candidate and party data.
   * @param elec The ElectionType object to update.
   */
  public void createCandidatesAndParties(String line, ElectionType elec) {
    // Split the line into individual candidate representations
    String[] candidateData = line.split(", ");

    // Initialize data structures to store parties and candidates
    HashMap<String, Party> partyList = new HashMap<>();
    ArrayList<Candidate> candidateList = new ArrayList<>();

    for (String candidateRepresentation : candidateData) {
      // Extract candidate name and party abbreviation
      String[] parts = candidateRepresentation.split(" \\(");
      String candidateName = parts[0];
      String partyAbbreviation = parts[1].replace(")", "");

      // Check if the party already exists in the party list
      Party partyObject;
      if (partyList.containsKey(partyAbbreviation)) {
        partyObject = partyList.get(partyAbbreviation);
      } else {
        // If the party doesn't exist, create a new party
        partyObject = new Party(partyAbbreviation);
        partyList.put(partyAbbreviation, partyObject);
      }

      // Create a candidate and assign it to the party
      Candidate candidate = new Candidate(candidateName);
      candidate.assignCandidateToParty(partyObject);

      // Add the candidate to the candidate list and party's candidate list
      candidateList.add(candidate);
      partyObject.addCandidateToList(candidate);
    }

    // Update the election type with the candidate and party information
    elec.setCandidateList(candidateList);
    elec.setPartyList(partyList);
  }

  /**
   * Creates candidates and parties based on the input data for an MPO election.
   * Used in the readHeader method.
   * @param line The input line containing candidate and party data.
   * @param elec The ElectionType object to update.
   */
  public void createCandidatesAndPartiesMPO(String line, ElectionType elec) {
    // Split the line into individual candidate representations
    String[] candidateData = line.split("], ");

    // Initialize data structures to store parties and candidates
    HashMap<String, Party> partyList = new HashMap<>();
    ArrayList<Candidate> candidateList = new ArrayList<>();

    for (String candidateRepresentation : candidateData) {
      // Extract candidate name and party abbreviation
      String[] parts = candidateRepresentation.split(",");
      String candidateName = parts[0].replace("[", "");
      String partyAbbreviation = parts[1].replace("]", "");

      // Check if the party already exists in the party list
      Party partyObject;
      if (partyList.containsKey(partyAbbreviation)) {
        partyObject = partyList.get(partyAbbreviation);
      } else {
        // If the party doesn't exist, create a new party
        partyObject = new Party(partyAbbreviation);
        partyList.put(partyAbbreviation, partyObject);
      }

      // Create a candidate and assign it to the party
      Candidate candidate = new Candidate(candidateName);
      candidate.assignCandidateToParty(partyObject);

      // Add the candidate to the candidate list and party's candidate list
      candidateList.add(candidate);
      partyObject.addCandidateToList(candidate);
    }

    // Update the election type with the candidate and party information
    elec.setCandidateList(candidateList);
    elec.setPartyList(partyList);
  }

  /**
   * Creates candidates and parties based on the input data.
   * Used in the readHeader method.
   * @param elec The ElectionType object to update.
   */
  public void readRemainingIR(ElectionType elec) {
    try (BufferedReader br = new BufferedReader(new FileReader((inputFile)))) {
      String line;
      int lineCounter = 0;

      while ((line = br.readLine()) != null) {
        lineCounter++;
        if (lineCounter <= 4) {
          continue; // Skip the first four lines
        }

        Ballot ballot = new Ballot();
        ballot.setInputtedBallot(line);

        ArrayList<Candidate> translatedBallot = ballot.translateBallot(
          line,
          elec.getCandidatesCount(),
          elec.getCandidateList()
        );
        ballot.setFormattedBallot(translatedBallot);

        if (!translatedBallot.isEmpty()) {
          Candidate topCandidate = translatedBallot.get(0);
          topCandidate.addToBallotList(ballot);
        }
      }
      elec.runElection();
      ((IRElection) elec).displayIRElectionResults();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads the remaining lines for an OPL election, processes them, and updates
   * the OPLElection object accordingly.
   * @param elec The OPLElection object to update.
   */
  public void readRemainingOPL(ElectionType elec) {
    ArrayList<Candidate> candidateList = elec.getCandidateList();
    try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
      String line;
      int lineCounter = 0;
      while ((line = br.readLine()) != null) {
        lineCounter++;
        if (lineCounter <= 5) {
          continue; // Skip the first five lines
        }
        String[] preferences = line.split(",", -1);
        for (int i = 0; i < preferences.length; i++) {
          if (preferences[i].compareTo("1") == 0) {
            Candidate candidate = candidateList.get(i);
            candidate.getCandidateParty().incrementVoteCount();
            candidate.incrementBallotCount();
            break;
          }
        }
      }

      elec.runElection();
      ((OPLElection) elec).displayElectionResults();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads the remaining lines for an MPO election, processes them, and updates
   * the MPOElection object accordingly.
   * @param elec The MPOElection object to update.
   */
  public void readRemainingMPO(ElectionType elec) {
    ArrayList<Candidate> candidateList = elec.getCandidateList();
    try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
      String line;
      int lineCounter = 0;
      while ((line = br.readLine()) != null) {
        lineCounter++;
        if (lineCounter <= 5) {
          continue; // Skip the first five lines
        }
        String[] preferences = line.split(",", -1);
        for (int i = 0; i < preferences.length; i++) {
          if (preferences[i].compareTo("1") == 0) {
            Candidate candidate = candidateList.get(i);
            candidate.getCandidateParty().incrementVoteCount();
            candidate.incrementBallotCount();
            break;
          }
        }
      }

      elec.runElection();
      ((MPOElection) elec).displayElectionResults();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes election results and associated information to an audit file for IR elections.
   * @param candidateBallotMap A map of candidates and their associated ballots.
   * @param text Additional information to write to the audit file.
   */
  public void writeToAuditFileIR(
    boolean closeAudit,
    ArrayList<Candidate> candidates,
    String text,
    ArrayList<Ballot> removedBallots
  ) {
    // System.out.println("in write to audit file function 1: " + auditFileName);
    //System.out.println("in write to audit file function 2: " + auditFileName);
    // System.out.println(
    //   "Current Working Directory: " + System.getProperty("user.dir")
    // );
    // System.out.println(
    //   "Full File Path: " + new File(auditFileName).getAbsolutePath()
    // );
    //Path directoryPath = Paths.get(auditFileName).getParent();
    // System.out.println("Path: " + directoryPath);

    try (
      BufferedWriter writer = new BufferedWriter(
        new FileWriter(auditFileName, true)
      )
    ) {
      //System.out.println("gets in bufferedwriter part ");
      writer.newLine();
      writer.write(text);
      for (int i = 0; i < candidates.size(); i++) {
        if (candidates.get(i).isInElection()) {
          writer.write("Candidate " + candidates.get(i).getName());
          writer.newLine();
          writer.write("Associated Ballots: ");
          ArrayList<Ballot> ballots = candidates.get(i).getBallots();
          for(Ballot b: ballots){
            writer.write(b.getInputtedBallot());
          }
          writer.newLine();
        }
      }
      writer.write("Eliminated Candidates:");
      writer.newLine();
      for (int i = 0; i < candidates.size(); i++) {
        if (!candidates.get(i).isInElection()) {
          writer.write("Candidate " + candidates.get(i).getName());
          writer.newLine();
        }
      }
      writer.write("Invalidated Ballots");
      writer.newLine();
      for (int i = 0; i < removedBallots.size(); i++) {
        writer.write(removedBallots.get(i).getInputtedBallot());
      }

      if (closeAudit) {
        closeAuditFile(writer);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes election results and associated information to an audit file for OPL elections.
   * @param quota The seat quota for this OPL election
   * @param round The current round of OPL election.
   * @param partyList The list of parties participating in the election.
   * @param votesLeftDict A dictionary of remaining votes for each party.
   * @param seatsLeft The number of seats left to be assigned.
   */
  public void writeToAuditFileOPL(
    boolean closeAudit,
    int quota,
    String round,
    ArrayList<Party> partyList,
    HashMap<Integer, ArrayList<Party>> votesLeftDict,
    int seatsLeft
  ) {
    createAuditFile("OPL");
    try (
      BufferedWriter writer = new BufferedWriter(
        new FileWriter(auditFileName, true)
      )
    ) {
      writer.newLine();
      writer.write("OPL ELECTION AUDIT FILE");
      writer.newLine();
      writer.write(round);
      writer.newLine();
      writer.write("Total Seats: " + partyList.size());
      writer.newLine();
      writer.write("Current Seats Left: " + seatsLeft);
      writer.newLine();
      writer.write("Seat Quota: " + quota);
      writer.newLine();
      writer.write(
        "Party Name   |   Total Votes   |   Seats Assigned   |   Remainder Votes  "
      );

      for (Party party : partyList) {
        writer.newLine();
        writer.write(
          party.getName() +
          "   |   " +
          party.getVoteCount() +
          "   |   " +
          party.getPartySeatCount() +
          "   |   " +
          party.getVotesLeft()
        );
        writer.newLine();
      }

      // if its the last call to writeToAudit, we want permissions to be read-only
      if (closeAudit) {
        closeAuditFile(writer);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Closes the audit file by closing the writer and setting the file to read-only.
   * @param writer The BufferedWriter used to write to the audit file.
   */

  public void closeAuditFile(BufferedWriter writer) {
    if (writer != null) {
      try {
        // Close the writer to audit file
        writer.close();
        // Set the file to read-only
        setFileReadOnly(auditFileName);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Sets the input file's permissions to read-only.
   * @param fileName The path to the input file.
   */
  private void setFileReadOnly(String fileName) {
    // Set file permissions to read-only (Unix-like systems)
    Set<PosixFilePermission> perms = PosixFilePermissions.fromString(
      "r--r--r--"
    );
    // FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);

    // Convert the file name to a Path
    Path path = Path.of(fileName);

    try {
      // Set file permissions
      Files.setPosixFilePermissions(path, perms);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets the input file for processing.
   * @param inputFile The path to the input file.
   */
  public boolean setInputFile(String inputFile) {
    this.inputFile = "electionFiles/" + inputFile;
    File file = new File(this.inputFile);
    return (file.exists() && file.isFile() && file.canRead());
  }

  /**
   * Gets the input file for processing.
   * @return inputFile The path to the input file.
   */
  public String getInputFile() {
    return inputFile;
  }
}
