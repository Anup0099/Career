package Main;

import dao.ServiceProvider;
import dao.ServiceProviderImpl;
import entity.model.*;
import exception.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;

public class MainModule {
    private static ServiceProvider serviceProvider;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            serviceProvider = new ServiceProviderImpl();
        } catch (DatabaseConnectionException e) {
            System.out.println(e.getMessage());
            return;
        }

        while (true) {
            showMenu();
            int choice = getChoice();
            switch (choice) {
                case 1:
                    serviceProvider.initializeDatabase();
                    break;
                case 2:
                    insertCompany();
                    break;
                case 3:
                    insertJobListing();
                    break;
                case 4:
                    insertApplicant();
                    break;
                case 5:
                    applyForJob();
                    break;
                case 6:
                    viewJobListings();
                    break;
                case 7:
                    viewCompanies();
                    break;
                case 8:
                    viewApplicants();
                    break;
                case 9:
                    viewApplicationsForJob();
                    break;
                case 10:
                    System.out.println("Exiting application.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please choose again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\\n=== CareerHub Job Board ===");
        System.out.println("1. Initialize Database");
        System.out.println("2. Insert Company");
        System.out.println("3. Insert Job Listing");
        System.out.println("4. Insert Applicant");
        System.out.println("5. Apply for Job");
        System.out.println("6. View Job Listings");
        System.out.println("7. View Companies");
        System.out.println("8. View Applicants");
        System.out.println("9. View Applications for a Job");
        System.out.println("10. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getChoice() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch(NumberFormatException e){
            // Invalid input, return -1
        }
        return choice;
    }

    private static void insertCompany() {
        System.out.print("Enter Company Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Company Location: ");
        String location = scanner.nextLine();
        Company company = new Company();
        company.setCompanyName(name);
        company.setLocation(location);
        serviceProvider.insertCompany(company);
    }

    private static void insertJobListing() {
        System.out.print("Enter Company ID: ");
        int companyID;
        try {
            companyID = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Company ID.");
            return;
        }
        System.out.print("Enter Job Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Job Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Job Location: ");
        String location = scanner.nextLine();
        double salary = 0.0;
        try {
            System.out.print("Enter Salary: ");
            salary = Double.parseDouble(scanner.nextLine());
            if (salary < 0) {
                throw new NegativeSalaryException("Salary cannot be negative.");
            }
        } catch (NegativeSalaryException e) {
            System.out.println(e.getMessage());
            return;
        } catch (NumberFormatException e) {
            System.out.println("Invalid salary input.");
            return;
        }
        System.out.print("Enter Job Type (Full-time, Part-time, Contract): ");
        String jobType = scanner.nextLine();
        JobListing job = new JobListing();
        job.setCompanyID(companyID);
        job.setJobTitle(title);
        job.setJobDescription(description);
        job.setJobLocation(location);
        job.setSalary(salary);
        job.setJobType(jobType);
        job.setPostedDate(new Date());
        serviceProvider.insertJobListing(job);
    }

    private static void insertApplicant() {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        String email = "";
        try {
            System.out.print("Enter Email: ");
            email = scanner.nextLine();
            if (!isValidEmail(email)) {
                throw new InvalidEmailException("Invalid email format.");
            }
        } catch (InvalidEmailException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        Applicant applicant = new Applicant();
        applicant.setFirstName(firstName);
        applicant.setLastName(lastName);
        applicant.setEmail(email);
        applicant.setPhone(phone);
        serviceProvider.insertApplicant(applicant);
    }

    private static void applyForJob() {
        System.out.print("Enter Applicant ID: ");
        int applicantID;
        try {
            applicantID = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Applicant ID.");
            return;
        }
        System.out.print("Enter Job ID: ");
        int jobID;
        try {
            jobID = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Job ID.");
            return;
        }

        System.out.print("Enter Cover Letter: ");
        String coverLetter = scanner.nextLine();

        // File upload simulation
        System.out.print("Enter Resume file path: ");
        String filePath = scanner.nextLine();
        try {
            handleFileUpload(filePath);
        } catch (FileUploadException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Application deadline check (for simplicity, not implemented)
        // Assume all jobs are open

        serviceProvider.applyForJob(jobID, applicantID, coverLetter);
    }

    private static void viewJobListings() {
        List<JobListing> jobs = serviceProvider.getJobListings();
        System.out.println("\\n--- Job Listings ---");
        for (JobListing job : jobs) {
            System.out.println("Job ID: " + job.getJobID());
            System.out.println("Company ID: " + job.getCompanyID());
            System.out.println("Job Title: " + job.getJobTitle());
            System.out.println("Job Description: " + job.getJobDescription());
            System.out.println("Job Location: " + job.getJobLocation());
            System.out.println("Salary: " + job.getSalary());
            System.out.println("Job Type: " + job.getJobType());
            System.out.println("Posted Date: " + job.getPostedDate());
            System.out.println("------------------------");
        }
    }

    private static void viewCompanies() {
        List<Company> companies = serviceProvider.getCompanies();
        System.out.println("\\n--- Companies ---");
        for (Company company : companies) {
            System.out.println("Company ID: " + company.getCompanyID());
            System.out.println("Company Name: " + company.getCompanyName());
            System.out.println("Location: " + company.getLocation());
            System.out.println("------------------------");
        }
    }

    private static void viewApplicants() {
        List<Applicant> applicants = serviceProvider.getApplicants();
        System.out.println("\\n--- Applicants ---");
        for (Applicant applicant : applicants) {
            System.out.println("Applicant ID: " + applicant.getApplicantID());
            System.out.println("First Name: " + applicant.getFirstName());
            System.out.println("Last Name: " + applicant.getLastName());
            System.out.println("Email: " + applicant.getEmail());
            System.out.println("Phone: " + applicant.getPhone());
            System.out.println("------------------------");
        }
    }

    private static void viewApplicationsForJob() {
        System.out.print("Enter Job ID: ");
        int jobID;
        try {
            jobID = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Job ID.");
            return;
        }
        List<JobApplication> applications = serviceProvider.getApplicationsForJob(jobID);
        System.out.println("\\n--- Applications for Job ID " + jobID + " ---");
        for (JobApplication app : applications) {
            System.out.println("Application ID: " + app.getApplicationID());
            System.out.println("Applicant ID: " + app.getApplicantID());
            System.out.println("Application Date: " + app.getApplicationDate());
            System.out.println("Cover Letter: " + app.getCoverLetter());
            System.out.println("------------------------");
        }
    }

    private static boolean isValidEmail(String email) {
        // Simple regex for email validation
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private static void handleFileUpload(String filePath) throws FileUploadException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileUploadException("File not found.");
        }
        if (file.length() > 5 * 1024 * 1024) { // 5 MB limit
            throw new FileUploadException("File size exceeds limit.");
        }
        String fileExtension = getFileExtension(file);
        if (!fileExtension.equalsIgnoreCase("pdf") && !fileExtension.equalsIgnoreCase("docx")) {
            throw new FileUploadException("Unsupported file format.");
        }
        // For simplicity, do not actually upload
        System.out.println("File uploaded successfully.");
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
}
