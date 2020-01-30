import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Scanner;
import java.io.*;

public class JobScraper {
    static String jobTitle = "";
    static String jobLocation = "";
    static String company = "";
    static String jobURL = "";

    /* Main function to parse Jsoup Documents for job titles,
     * job locations, and URL to job posting.  Appends output
     * per job to CSV file.
     */
    public static void stackOverflowJobs(Document doc, String userFileName) {
        for (Element job : doc.select("div.-job")) {
            jobTitle = job.select("a.s-link").text();
            jobLocation = job.select("h3.fc-black-700").select("span.fc-black-500").text();
            company = job.select("h3.fc-black-700").select("span").text();
            jobURL = job.select("a.s-link").attr("href");
            jobURL = "https://stackoverflow.com" + jobURL;

            System.out.println("Job Title: " + jobTitle);
            System.out.println("Job Location: " + jobLocation);
            System.out.println("Company: " + company);
            System.out.println("Link to job desc: " + jobURL);

            outputToCSV(jobTitle, jobLocation, company, jobURL, userFileName);

        }
    }

    /* Main function to parse Jsoup Documents for job titles,
     * job locations, and URL to job posting.  Appends output
     * per job to CSV file.
     */
    public static void monsterJobs(Document doc, String fileName) {
        for (Element job : doc.select("div.flex-row")) {
            jobTitle = job.getElementById("a").text();
            jobLocation = job.select("company.name").text();
            //company = doc.getElementByClass("company");
            //jobURL = doc.getElementById("");
            System.out.println("Job Title: " + jobTitle);
            System.out.println("Job Location: " + jobLocation);
        }
    }

    // Output job site results to CSV file
    public static void outputToCSV(String jobTitle, String jobLocation, 
            String company, String jobURL, String userFileName) {
                
        try {
            FileWriter csvWriter = new FileWriter("C:\\Users\\racerx\\JavaCode\\JobScraperJava\\" + userFileName, true);
            csvWriter.append(jobTitle);
            csvWriter.append(",");
            csvWriter.append(jobLocation);
            csvWriter.append(",");
            csvWriter.append(company);
            csvWriter.append(",");
            csvWriter.append(jobURL);
            csvWriter.append("\n");

            csvWriter.flush();
            csvWriter.close();
        }
        catch (IOException e) {
            System.out.println("Error writing to CSV!");
        }
    }

    // Initialize CSV file to output jobs
    public static void initCSV(String userFileName) {
        try {
            FileWriter csvWriter = new FileWriter(userFileName);
            csvWriter.append("Title");
            csvWriter.append(",");
            csvWriter.append("Location");
            csvWriter.append(",");
            csvWriter.append("Company");
            csvWriter.append(",");
            csvWriter.append("Link to Job Description");
            csvWriter.append("\n");

            csvWriter.flush();
            csvWriter.close();
        }
        catch (IOException e) {
            System.out.println("Error writing to CSV!");
        }
    }

    public static void main(String[] args) {
        String monsterURL = "https://www.monster.com/jobs/search/?q=Software+Engineer&where=North-Carolina";
        String stackOverflowURL = "https://stackoverflow.com/jobs?q=Software+Engineer&l=North+Carolina%2C+USA&d=20&u=Miles";
        String indeedURL = "https://www.indeed.com/jobs?q=software+engineer&l=Raleigh,+NC&explvl=entry_level";
        String header = "FIX ME: Insert fake user agent here.";
        String userJobTitle = "";
        String userLocation = "";
        String userFileName = "JobScraper.csv";

        /* Prompt user to enter job title and location for job search
         * Validates user input, and replaces unnecessary chars for
         * insertion into search query. */
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter job title:");
        if (scnr.hasNext()) {
            userJobTitle = scnr.nextLine();
            userJobTitle = userJobTitle.replace(" ", "+").replace(",", "");
        }
        System.out.println("Enter job location:");
        if (scnr.hasNext()) {
            userLocation = scnr.nextLine();
            userLocation = userLocation.replace(" ", "+").replace(",", "");
        }
        System.out.println("Enter file name to save:");
        if (scnr.hasNext()) {
            userFileName = scnr.nextLine();
        }
        
        // Output app info and user input
        System.out.print("Job Scraper v1 using jsoup.\nSearching for . . .\n");
        System.out.printf("job title: %s\n", userJobTitle);
        System.out.printf("location:  %s\n", userLocation);

        try {
            // Jsoup gets HTML from each Jobsite's URL
            Document stackOverflowDoc = Jsoup.connect(stackOverflowURL).timeout(1000000).get();
            //indeedDoc = Jsoup.connect(indeedURL).get();

            // Initialize csv file to put header info before adding jobs
            initCSV(userFileName);

            // Run JobScraper on Job Boards
            stackOverflowJobs(stackOverflowDoc, userFileName);
        }
        catch (Exception e) {
            System.out.println("Error could not connect: " + e);
        }
 
    }
}
