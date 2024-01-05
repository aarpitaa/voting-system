import java.util.Scanner;
import java.io.IOException;

/**
 * The `Controller` class serves as the entry point for the Election Counter program.
 * It provides a command-line interface for users to interact with the system,
 * offering options to count votes for Instant Runoff (IR) or Open Party List (OPL)
 * elections and access a testing environment. The program guides users through the
 * process based on their input commands.
 * @author Praful Das
 */
public class Controller {

    /**
     * The main method where the program execution begins.
     * @param args Command-line arguments (not used in this application).
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Election Counter!");
        System.out.println("List of commands:");
        System.out.println("<yourfilename>.csv: To count votes for Instant Runoff or Open Party List voting, enter the full filename with the \".csv\" extension. Please make sure that your file is inside the folder \"electionFiles\" in the \"src\" directory.");
        System.out.println("test: To access the testing environment");
        System.out.println("^C: Quit the program");
        System.out.print("Enter your command: ");
        
        // Command processing
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        // Command processing
        if (userInput.equals("test")) {
            System.out.println("Run Test");
        } else if (userInput.endsWith(".csv")) {
            // Process CSV file for election
            FileProcessor fileProcessor = new FileProcessor();
	        if (fileProcessor.setInputFile(userInput)) {
                    ElectionType electionType = fileProcessor.readHeader();

                    // Determine the type of election and read remaining data accordingly
                    if (electionType.getElectionType().equals("IR")) {
                        fileProcessor.readRemainingIR(electionType);
                    } else {
                        fileProcessor.readRemainingOPL(electionType);
                    }
                } else {
                    // Handle invalid input file
                    System.out.println("The filename entered is incorrect");
                }     
        } else if (userInput.equals("exit")) {
            // Exit the program
            System.exit(0);
        } else {
            // Handle invalid user input
            System.out.println("Invalid input. Please enter a valid command.");
        }
        scanner.close();
    }

}
