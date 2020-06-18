package jp.co.nri.point.web.dto;

import java.util.List;

import jp.co.nri.point.web.domain.Department;

public class DeptList {
    private List<Department> departments;

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

}
