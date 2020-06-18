package jp.co.nri.point.web.domain;

import java.io.Serializable;

public class EmployeeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String address;

    private Integer age;

    private Long department;

    private Integer salary;

    private String tel;

    public EmployeeVO() {
        super();
    }

    public EmployeeVO(Long id, String name, String address, Integer age, Long department, Integer salary, String tel) {
        super();
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
        this.department = department;
        this.salary = salary;
        this.tel = tel;
    }

    public EmployeeVO(Long id, String name, String address, Integer age) {
        super();
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", address=" + address + ", age=" + age + "]";
    }

}
