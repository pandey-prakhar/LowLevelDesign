package designpatterns.singleton;

/**
 * Singleton Pattern - Final Version (Double-Checked Locking)
 *
 * Problem: Only one database connection should exist across the entire application.
 *
 * Approach:
 * - Private constructor prevents direct instantiation.
 * - Static instance holds the single object.
 * - volatile ensures the instance is correctly visible across threads.
 * - Double-checked locking avoids synchronization overhead on every call:
 *     1st check: skip locking if instance already exists (fast path).
 *     lock: ensure only one thread creates the instance.
 *     2nd check: another thread may have created it while we were waiting for the lock.
 */
public class DatabaseConnectionV4 {

    private String url;
    private String username;
    private String password;
    private String port;

    private static volatile DatabaseConnectionV4 instance;

    private DatabaseConnectionV4() {
    }

    public static DatabaseConnectionV4 getConnection() {
        if (instance == null) {
            synchronized (DatabaseConnectionV4.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionV4();
                }
            }
        }
        return instance;
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }
}
