package com.hust.grp1.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "web_user",
        indexes = @Index(name = "username_index", columnList = "username"),
        uniqueConstraints = @UniqueConstraint(name = "username_unique_constrain", columnNames = "username")
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "username", nullable = false)
    private String username;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    @Column(columnDefinition="TEXT")
    private String about;

    private long reputation;
    @CreationTimestamp
    private LocalDateTime createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getReputation() {
        return reputation;
    }

    public void setReputation(long reputation) {
        this.reputation = reputation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String moreInformation) {
        this.about = moreInformation;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void increaseReputation(long value) {
        this.reputation += value;
    }

    public void decreaseReputation(long value) {
        this.reputation -= value;
    }
}
