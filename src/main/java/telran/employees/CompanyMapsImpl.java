package telran.employees;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import telran.io.Persistable;

//So far we do consider optimization
public class CompanyMapsImpl implements Company, Persistable {
	TreeMap<Long, Employee> employees = new TreeMap<>();
	TreeMap<String, List<Employee>> employeesDepartment = new TreeMap<>();
	TreeMap<Float, List<Manager>> factorManagers = new TreeMap<>();
	private class CompanyIterator implements Iterator<Employee>{
		Iterator<Employee> iterator = employees.values().iterator();
		Employee prev;
		@Override
		public boolean hasNext() {
			
			return iterator.hasNext();
		}

		@Override
		public Employee next() {
			prev = iterator.next();
			return prev;
		}
		@Override
		public  void remove() {
			iterator.remove();
			removeFromIndexMaps(prev);
		}
		
	}
	@Override
	public synchronized Iterator<Employee> iterator() {
		
		return new CompanyIterator();
	}

	@Override
	public synchronized void addEmployee(Employee empl) {
		if (employees.putIfAbsent(empl.getId(), empl) != null) {
			throw new IllegalStateException("employee already exists");
		}
		addToIndexMap(employeesDepartment, empl.getDepartment(), empl);
		if (empl instanceof Manager) {
			Manager manager = (Manager)empl;
			addToIndexMap(factorManagers, manager.factor, manager);
		}

	}

	@Override
	public synchronized Employee getEmployee(long id) {
		
		return employees.get(id);
	}

	@Override
	public synchronized Employee removeEmployee(long id) {
		Employee empl = employees.remove(id);
		if(empl == null) {
			throw new NoSuchElementException("employee doesn't exist");
		}
		removeFromIndexMaps(empl);
		return empl;
	}

	private void removeFromIndexMaps(Employee empl) {
		removeFromIndexMap(employeesDepartment, empl.getDepartment(), empl);
		if(empl instanceof Manager) {
			Manager manager = (Manager)empl;
			removeFromIndexMap(factorManagers, manager.factor, manager);
		}
	}
	private <K, V extends Employee> void removeFromIndexMap(Map<K, List<V>> map, K key, V empl) {
		map.computeIfPresent(key, (k, v) -> {
			v.remove(empl);
			return v.isEmpty() ? null : v;
		});
		
	}
	private <K, V extends Employee> void addToIndexMap(Map<K, List<V>> map, K key, V empl) {
		map.computeIfAbsent(key, k -> new ArrayList<>()).add(empl);
		
	}

	@Override
	public synchronized int getDepartmentBudget(String department) {
		return employeesDepartment.getOrDefault(department,
				Collections.emptyList()).stream()
		.collect(Collectors.summingInt(Employee::computeSalary));
	}

	@Override
	public synchronized String[] getDepartments() {
		return employeesDepartment.keySet()
				.toArray(String[]::new);
	}

	@Override
	public synchronized Manager[] getManagersWithMostFactor() {
		Manager[] res = new Manager[0];
		if(!factorManagers.isEmpty()) {
			res = factorManagers.lastEntry().getValue().toArray(res);
		}
		return res;
	}

	@Override
	public synchronized void save(String filePathStr) {
		try(PrintWriter writer = new PrintWriter(filePathStr)) {
			this.forEach(empl -> writer.println(empl.getJSON()));
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public synchronized void restore(String filePathStr) {
		try(BufferedReader reader = new BufferedReader(new FileReader(filePathStr))){
			reader.lines().map(l -> (Employee) new Employee().setObject(l))
			.forEach(this::addEmployee);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	

}