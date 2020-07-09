package org.pointsys.test;

public class Demo {
    private int id;
    private String message;

    public Demo() {
        super();
    }

    public Demo(int id, String message) {
        super();
        this.id = id;
        this.message = message;
    }

    public static void hi(int id, String message) {
        System.out.println("大家好，我叫" + message + "，今年" + id + "岁");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Demo [id=" + id + ", message=" + message + "]";
    }
}
