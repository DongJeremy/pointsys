package jp.co.nri.point.dto;

public class PasswordBean {

    private Long id;
    private String original;
    private String password;
    private String confirm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    @Override
    public String toString() {
        return "PasswordBean [id=" + id + ", original=" + original + ", password=" + password + ", confirm=" + confirm
                + "]";
    }

}
