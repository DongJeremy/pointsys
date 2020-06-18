package jp.co.nri.point.api.dto;

import java.io.Serializable;

import jp.co.nri.point.annotation.ExcelInfo;

public class EmployeeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelInfo(columnIndex = 0, columnName = "序号")
    private Long id;

    @ExcelInfo(columnIndex = 1, columnName = "用户名")
    private String name;

    @ExcelInfo(columnIndex = 2, columnName = "地址")
    private String address;

    @ExcelInfo(columnIndex = 3, columnName = "年龄")
    private Integer age;

    @ExcelInfo(columnIndex = 6, columnName = "部门")
    private Long department;

    @ExcelInfo(columnIndex = 5, columnName = "工资")
    private Integer salary;

    @ExcelInfo(columnIndex = 4, columnName = "手机")
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
