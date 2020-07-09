package org.pointsys.test;

public class Book {
    private int id;
    private String name;
    private String descript;

    public Book() {
        super();
    }

    public Book(int id, String name, String descript) {
        super();
        this.id = id;
        this.name = name;
        this.descript = descript;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

}
