package jp.co.nri.point.domain;

import java.io.Serializable;
import java.util.List;

public class Department implements Serializable {

    private static final long serialVersionUID = 3270682253504566850L;
    private Long id;
    private String name;
    private String description;

    private List<Employee> employees;

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Department() {
        super();
    }

    public Department(Long id) {
        super();
        this.id = id;
    }

    public Department(Long id, String name, String description) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
