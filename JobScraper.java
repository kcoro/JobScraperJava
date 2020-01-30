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
            jobTitle = job.select("a.s-link").text().replace(",", " ");
            jobLocation = job.select("h3.fc-black-700").select("span.fc-black-500").text().replace(",", " ");
            company = job.select("h3.fc-black-700").select("span").text().replace(",", " ");
            jobURL = job.select("a.s-link").attr("href");
            jobURL = "https://stackoverflow.com" + jobURL;

            // Write each job element to CSV file
            outputToCSV(jobTitle, jobLocation, company, jobURL, userFileName);
        }
    }

    /* Parse Jsoup Documents for indeed job titles,
     * job locations, and URL to job posting.  Appends output
     * per job to CSV file.
     */
    public static void indeedJobs(Document doc, String userFileName) {
        for (Element job : doc.select("div.jobsearch-SerpJobCard")) {
            jobTitle = job.select("a.jobtitle").text().replace(",", " ");
            jobLocation = job.select("span.location").text().replace(",", " ");
            company = job.select("span.company").text().replace(",", " ");
            jobURL = job.select("a.jobtitle").attr("href");
            jobURL = "https://www.indeed.com/viewjob" + jobURL;

            // Write each job element to CSV file
            outputToCSV(jobTitle, jobLocation, company, jobURL, userFileName);
        }
    }

    /* Main function to parse Jsoup Documents for job titles,
     * job locations, and URL to job posting.  Appends output
     * per job to CSV file.
     */
    public static void monsterJobs(Document doc, String userFileName) {
        for (Element job : doc.select("div.flex-row")) {
            jobTitle = job.select("a").text().replace(",", " ");
            jobLocation = job.select("div.location").select("span.name").text().replace(",", " ");
            company = job.select("div.company").select("span.name").text().replace(",", " ");
            jobURL = job.select("a").attr("href");
            
            // Write each job element to CSV file
            outputToCSV(jobTitle, jobLocation, company, jobURL, userFileName);
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

    public static void main(String[] args) throws IOException {
        String monsterUrl = "https://www.monster.com/jobs/search/?q=Software+Engineer&where=North-Carolina";
        String stackOverflowUrl = "https://stackoverflow.com/jobs?q=Software+Engineer&l=North+Carolina%2C+USA&d=20&u=Miles";
        String indeedUrl = "https://www.indeed.com/jobs?q=software+engineer&l=Raleigh,+NC&explvl=entry_level";
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
        
        // Update job sites default url with user input query parameters
        monsterUrl = "https://www.monster.com/jobs/search/?q="+userJobTitle+"&where="+userLocation;
        stackOverflowUrl = "https://stackoverflow.com/jobs?q="+userJobTitle+"&l="+userLocation+"%2C+USA&d=20&u=Miles";
        indeedUrl = "https://www.indeed.com/jobs?q="+userJobTitle+"&l="+userLocation+"&explvl=entry_level";

        // Output app info and user input
        System.out.print("Job Scraper v1 using jsoup.\nSearching for . . .\n");
        System.out.printf("job title: %s\n", userJobTitle);
        System.out.printf("location:  %s\n", userLocation);

        try {
            // Jsoup gets HTML from each Jobsite's URL
            Document stackOverflowDoc = Jsoup.connect(stackOverflowUrl).timeout(1000000).get();
            Document indeedDoc = Jsoup.connect(indeedUrl).timeout(1000000).get();
            Document monsterDoc = Jsoup.connect(monsterUrl).timeout(1000000).get();

            // Initialize csv file to put header info before adding jobs
            initCSV(userFileName);

            // Run JobScraper on Job Boards
            stackOverflowJobs(stackOverflowDoc, userFileName);
            indeedJobs(indeedDoc, userFileName);
            monsterJobs(monsterDoc, userFileName);
        }
        catch (Exception e) {
            System.out.println("Error could not connect: " + e);
        }
 
    }
}
