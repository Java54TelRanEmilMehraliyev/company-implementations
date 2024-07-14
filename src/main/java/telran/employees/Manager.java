package telran.employees;

import org.json.JSONObject;

import telran.io.JSONable;

public class Manager extends Employee {
    float factor;
    public Manager() {
    	
    }

	public Manager(long id, int basicSalary, String department, float factor) {
		super(id, basicSalary, department);
		this.factor = factor;
	}
    @Override
    public int computeSalary() {
    	return Math.round(super.computeSalary() * factor);
    }
    @Override
    protected void fillJSONObject(JSONObject jsonObject) {
    	fillClassName(jsonObject);
    	super.fillJSONObject(jsonObject);
    	jsonObject.put("factor", factor);
    }
    @Override
    protected void fillEmployee(JSONObject jsonObject) {
    	super.fillEmployee(jsonObject);
    	factor = jsonObject.getFloat("factor");
    }
}
