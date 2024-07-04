package designpatterns.singleton;
//IN this version created a private constructor  and public method to create instance still not achieved singleton as
// we are creating new objects inside method also.

public class DatabaseConnectionV3 {
    private String url;
    private String username;
    private String password;
    private String port;

    private DatabaseConnectionV3() {
    }

    public static DatabaseConnectionV3 getConnection(){
        return new DatabaseConnectionV3();
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
