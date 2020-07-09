package org.pointsys.test.fastjson;

public class UserInfo extends BaseEntity {

    private String name;
    private int age;

    public UserInfo(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserInfo [name=" + name + ", age=" + age + ", version=" + version + "]";
    }
    
    

}
