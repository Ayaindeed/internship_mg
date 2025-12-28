package internship.dao;


import internship.entity.Company;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CompanyDAO extends GenericDAO<Company> {

    public CompanyDAO() {
        super(Company.class);
    }
}
