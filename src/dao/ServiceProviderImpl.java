package dao;

import entity.model.*;
import exception.DatabaseConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.DBConnUtil;
import util.DBPropertyUtil;

public class ServiceProviderImpl implements ServiceProvider {
    private Connection conn;

    public ServiceProviderImpl() throws DatabaseConnectionException {
        try {
            String connectionString = DBPropertyUtil.getConnectionString("db.properties");
            this.conn = DBConnUtil.getConnection(connectionString);
        } catch (Exception e) {
            throw new DatabaseConnectionException("Error initializing ServiceProviderImpl: " + e.getMessage());
        }
    }

    @Override
    public void initializeDatabase() {
        String createCompanies = "CREATE TABLE IF NOT EXISTS Companies (" +
                "CompanyID INT PRIMARY KEY AUTO_INCREMENT," +
                "CompanyName VARCHAR(255) NOT NULL," +
                "Location VARCHAR(255) NOT NULL" +
                ")";
        String createJobs = "CREATE TABLE IF NOT EXISTS Jobs (" +
                "JobID INT PRIMARY KEY AUTO_INCREMENT," +
                "CompanyID INT," +
                "JobTitle VARCHAR(255) NOT NULL," +
                "JobDescription TEXT," +
                "JobLocation VARCHAR(255)," +
                "Salary DECIMAL(10,2)," +
                "JobType VARCHAR(50)," +
                "PostedDate DATETIME," +
                "FOREIGN KEY (CompanyID) REFERENCES Companies(CompanyID)" +
                ")";
        String createApplicants = "CREATE TABLE IF NOT EXISTS Applicants (" +
                "ApplicantID INT PRIMARY KEY AUTO_INCREMENT," +
                "FirstName VARCHAR(255) NOT NULL," +
                "LastName VARCHAR(255) NOT NULL," +
                "Email VARCHAR(255) UNIQUE NOT NULL," +
                "Phone VARCHAR(20)" +
                ")";
        String createApplications = "CREATE TABLE IF NOT EXISTS Applications (" +
                "ApplicationID INT PRIMARY KEY AUTO_INCREMENT," +
                "JobID INT," +
                "ApplicantID INT," +
                "ApplicationDate DATETIME," +
                "CoverLetter TEXT," +
                "FOREIGN KEY (JobID) REFERENCES Jobs(JobID)," +
                "FOREIGN KEY (ApplicantID) REFERENCES Applicants(ApplicantID)" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createCompanies);
            stmt.execute(createJobs);
            stmt.execute(createApplicants);
            stmt.execute(createApplications);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    @Override
    public void insertCompany(Company company) {
        String sql = "INSERT INTO Companies (CompanyName, Location) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, company.getCompanyName());
            pstmt.setString(2, company.getLocation());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    company.setCompanyID(rs.getInt(1));
                }
            }
            System.out.println("Company inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting company: " + e.getMessage());
        }
    }

    @Override
    public void insertJobListing(JobListing job) {
        String sql = "INSERT INTO Jobs (CompanyID, JobTitle, JobDescription, JobLocation, Salary, JobType, PostedDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, job.getCompanyID());
            pstmt.setString(2, job.getJobTitle());
            pstmt.setString(3, job.getJobDescription());
            pstmt.setString(4, job.getJobLocation());
            pstmt.setDouble(5, job.getSalary());
            pstmt.setString(6, job.getJobType());
            pstmt.setTimestamp(7, new Timestamp(job.getPostedDate().getTime()));
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    job.setJobID(rs.getInt(1));
                }
            }
            System.out.println("Job listing inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting job listing: " + e.getMessage());
        }
    }

    @Override
    public void insertApplicant(Applicant applicant) {
        String sql = "INSERT INTO Applicants (FirstName, LastName, Email, Phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, applicant.getFirstName());
            pstmt.setString(2, applicant.getLastName());
            pstmt.setString(3, applicant.getEmail());
            pstmt.setString(4, applicant.getPhone());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    applicant.setApplicantID(rs.getInt(1));
                }
            }
            System.out.println("Applicant inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting applicant: " + e.getMessage());
        }
    }

    @Override
    public void insertJobApplication(JobApplication application) {
        String sql = "INSERT INTO Applications (JobID, ApplicantID, ApplicationDate, CoverLetter) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, application.getJobID());
            pstmt.setInt(2, application.getApplicantID());
            pstmt.setTimestamp(3, new Timestamp(application.getApplicationDate().getTime()));
            pstmt.setString(4, application.getCoverLetter());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    application.setApplicationID(rs.getInt(1));
                }
            }
            System.out.println("Job application inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting job application: " + e.getMessage());
        }
    }

    @Override
    public List<JobListing> getJobListings() {
        List<JobListing> jobs = new ArrayList<>();
        String sql = "SELECT * FROM Jobs";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                JobListing job = new JobListing();
                job.setJobID(rs.getInt("JobID"));
                job.setCompanyID(rs.getInt("CompanyID"));
                job.setJobTitle(rs.getString("JobTitle"));
                job.setJobDescription(rs.getString("JobDescription"));
                job.setJobLocation(rs.getString("JobLocation"));
                job.setSalary(rs.getDouble("Salary"));
                job.setJobType(rs.getString("JobType"));
                job.setPostedDate(rs.getTimestamp("PostedDate"));
                jobs.add(job);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving job listings: " + e.getMessage());
        }
        return jobs;
    }

    @Override
    public List<Company> getCompanies() {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM Companies";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Company company = new Company();
                company.setCompanyID(rs.getInt("CompanyID"));
                company.setCompanyName(rs.getString("CompanyName"));
                company.setLocation(rs.getString("Location"));
                companies.add(company);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving companies: " + e.getMessage());
        }
        return companies;
    }

    @Override
    public List<Applicant> getApplicants() {
        List<Applicant> applicants = new ArrayList<>();
        String sql = "SELECT * FROM Applicants";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Applicant applicant = new Applicant();
                applicant.setApplicantID(rs.getInt("ApplicantID"));
                applicant.setFirstName(rs.getString("FirstName"));
                applicant.setLastName(rs.getString("LastName"));
                applicant.setEmail(rs.getString("Email"));
                applicant.setPhone(rs.getString("Phone"));
                applicants.add(applicant);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving applicants: " + e.getMessage());
        }
        return applicants;
    }

    @Override
    public List<JobApplication> getApplicationsForJob(int jobID) {
        List<JobApplication> applications = new ArrayList<>();
        String sql = "SELECT * FROM Applications WHERE JobID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, jobID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                JobApplication app = new JobApplication();
                app.setApplicationID(rs.getInt("ApplicationID"));
                app.setJobID(rs.getInt("JobID"));
                app.setApplicantID(rs.getInt("ApplicantID"));
                app.setApplicationDate(rs.getTimestamp("ApplicationDate"));
                app.setCoverLetter(rs.getString("CoverLetter"));
                applications.add(app);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving job applications: " + e.getMessage());
        }
        return applications;
    }

    @Override
    public void applyForJob(int jobID, int applicantID, String coverLetter) {
        JobApplication application = new JobApplication();
        application.setJobID(jobID);
        application.setApplicantID(applicantID);
        application.setApplicationDate(new Date());
        application.setCoverLetter(coverLetter);
        insertJobApplication(application);
    }

    @Override
    public List<Applicant> getApplicantsForJob(int jobID) {
        List<Applicant> applicants = new ArrayList<>();
        String sql = "SELECT a.ApplicantID, a.FirstName, a.LastName, a.Email, a.Phone " +
                "FROM Applicants a " +
                "JOIN Applications ap ON a.ApplicantID = ap.ApplicantID " +
                "WHERE ap.JobID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, jobID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Applicant applicant = new Applicant();
                applicant.setApplicantID(rs.getInt("ApplicantID"));
                applicant.setFirstName(rs.getString("FirstName"));
                applicant.setLastName(rs.getString("LastName"));
                applicant.setEmail(rs.getString("Email"));
                applicant.setPhone(rs.getString("Phone"));
                applicants.add(applicant);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving applicants for job: " + e.getMessage());
        }
        return applicants;
    }
}
