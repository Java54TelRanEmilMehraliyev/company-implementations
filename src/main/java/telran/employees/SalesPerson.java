package telran.employees;

import org.json.JSONObject;

public class SalesPerson extends WageEmployee {
	
	float percent;
	long sales;
	public SalesPerson() {
		
	}
	public SalesPerson(long id, int basicSalary, String department, int hours, int wage, float percent, long sales) {
		super(id, basicSalary, department, hours, wage);
		this.percent = percent;
		this.sales = sales;
	}
	@Override
	public int computeSalary() {
		return Math.round(super.computeSalary() + sales * percent / 100);
	}
	@Override
    protected void fillJSONObject(JSONObject jsonObject) {
		fillClassName(jsonObject);
		super.fillJSONObject(jsonObject);
		jsonObject.put("percent" , percent);
		jsonObject.put("sales" , sales);
    }
    @Override
    protected void fillEmployee(JSONObject jsonObject) {
    	super.fillEmployee(jsonObject);
    	percent = jsonObject.getFloat("percent");
        sales = jsonObject.getLong("sales");
    }
  

}
