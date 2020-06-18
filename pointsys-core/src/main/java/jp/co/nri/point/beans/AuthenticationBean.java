package jp.co.nri.point.beans;

import java.io.Serializable;

public class AuthenticationBean implements Serializable {

    private static final long serialVersionUID = 8739179896784018733L;

    private String username;

    private String password;

    public AuthenticationBean() {
        super();
    }

    public AuthenticationBean(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
