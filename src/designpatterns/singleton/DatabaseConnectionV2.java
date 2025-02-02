package designpatterns.singleton;

public class DatabaseConnectionV2 {
    private String url;
    private String username;
    private String password;
    private String port;
    //IN this version created a private constructor still not achieved singleton as we are not able to create the class itlself.
    private DatabaseConnectionV2() {
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
