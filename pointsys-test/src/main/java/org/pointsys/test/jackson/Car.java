package org.pointsys.test.jackson;

public class Car<T> {
    private int id;
    private T data;
    private String color;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "Car [id=" + id + ", data=" + data + ", color=" + color + "]";
    }
}
