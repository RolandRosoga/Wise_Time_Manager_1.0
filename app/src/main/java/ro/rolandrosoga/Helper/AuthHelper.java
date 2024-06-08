package ro.rolandrosoga.Helper;

import ro.rolandrosoga.Mock.User;

public class AuthHelper {
    private static AuthHelper instance;
    private User currentUser;
    private boolean loggedIn;

    public static synchronized AuthHelper getInstance() {
        if (instance == null) {
            instance = new AuthHelper();
        }
        return instance;
    }
    public synchronized User getCurrentUser() {return currentUser;}
    public synchronized void setCurrentUser(User currentUser) {this.currentUser = currentUser;}
    public synchronized boolean getLoggedIn() {return loggedIn;}
    public synchronized void setLoggedIn(boolean loggedIn) {this.loggedIn = loggedIn;}
}