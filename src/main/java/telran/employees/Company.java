package telran.employees;




public interface Company extends Iterable<Employee>{
	public void addEmployee(Employee empl) ;
	public Employee getEmployee(long id) ;
	public Employee removeEmployee(long id) ;
	public int getDepartmentBudget(String department) ;
	public String[] getDepartments() ;
	public Manager[] getManagersWithMostFactor() ;
	
}
