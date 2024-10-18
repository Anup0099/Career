package dao;

import entity.model.Applicant;
import entity.model.Company;
import entity.model.JobApplication;
import entity.model.JobListing;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private List<JobListing> JobListings;
    private List<Company> Companies;
    private List<Applicant> Applicants;
    private List<JobApplication> JobApplications;

    // Initialize the database (table creation logic can be added here)
    public void initializeDatabase() {

        JobListings = new ArrayList<>();
        Companies = new ArrayList<>();
        Applicants = new ArrayList<>();
        JobApplications = new ArrayList<>();
        System.out.println("Database Initialized");

    }

    // Insert a job listing
    public void insertJobListing(JobListing job) {
        JobListings.add(job);

        System.out.println("Job Listing Inserted: " + job.getJobTitle());
        // Database insertion logic here
    }

    // Insert a company
    public void insertCompany(Company company) {
        Companies.add(company);
        System.out.println("Company Inserted: " + company.getCompanyName());
        // Database insertion logic here
    }

    // Insert an applicant
    public void insertApplicant(Applicant applicant) {
        Applicants.add(applicant);
        System.out.println("Applicant Inserted: " + applicant.getFirstName() + " " + applicant.getLastName());
        // Database insertion logic here
    }

    // Insert a job application
    public void insertJobApplication(JobApplication application) {
        JobApplications.add(application);
        System.out.println("Job Application Inserted for Job ID: " + application

                .getJobID());
        // Database insertion logic here

    }

    // Retrieve all job listings
    public List<JobListing> getJobListings() {

        System.out.println("Retrieving Job Listings");

        // Logic to retrieve job listings from the database
        // retrive all the job that has been posted
        return JobListings;

    }

    // Retrieve all companies
    public List<Company> getCompanies() {
        System.out.println("Retrieving Companies");
        return Companies;
        // Logic to retrieve companies from the database
    }

    // Retrieve all applicants
    public List<Applicant> getApplicants() {
        System.out.println("Retrieving Applicants");
        return Applicants;
        // Logic to retrieve applicants from the database
    }

    // Retrieve applications for a specific job
    public List<JobApplication> getApplicationsForJob(int jobID) {
        System.out.println("Retrieving applications for Job ID: " + jobID);
        List<JobApplication> applications = new ArrayList<>();
        for (JobApplication application : JobApplications) {
            if (application.getJobID() == jobID) {
                applications.add(application);
            }
        }
        return applications;
    }
}
