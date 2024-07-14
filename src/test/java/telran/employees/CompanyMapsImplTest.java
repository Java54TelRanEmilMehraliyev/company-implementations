package telran.employees;


import org.junit.jupiter.api.BeforeEach;

public class CompanyMapsImplTest extends CompanyTest {
	@Override
	@BeforeEach
	void setCompany() {
		company = getEmptyCompany();
		super.setCompany();
	}

	@Override
	protected Company getEmptyCompany() {
		
		return new CompanyMapsImpl();
	}
}