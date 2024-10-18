package dao;

import java.util.List;
import entity.model.*;

public interface ServiceProvider {
    // Initialize database
    void initializeDatabase();

    // Insert methods
    void insertJobListing(JobListing job);
    void insertCompany(Company company);
    void insertApplicant(Applicant applicant);
    void insertJobApplication(JobApplication application);

    // Get methods
    List<JobListing> getJobListings();
    List<Company> getCompanies();
    List<Applicant> getApplicants();
    List<JobApplication> getApplicationsForJob(int jobID);

    // Additional methods
    void applyForJob(int jobID, int applicantID, String coverLetter);
    List<Applicant> getApplicantsForJob(int jobID);
}
