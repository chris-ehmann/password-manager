package com.example.password_manager.Models;

public class Password {

    private String Username;
    private String Password;
    private String SiteLink;
    private String SiteName;
    private final int id;

    public Password(String Username, String Password, String SiteLink, String SiteName, int id) {
        this.Username = Username;
        this.Password = Password;
        this.SiteLink = SiteLink;
        this.SiteName = SiteName;
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getSiteLink() {
        return SiteLink;
    }

    public void setSiteLink(String SiteLink) {
        this.SiteLink = SiteLink;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String SiteName) {
        this.SiteName = SiteName;
    }

    public int getId() { return id; }
}
