package ca.qc.johnabbott.cs616.notes.server.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by ian on 15-10-02.
 */
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userid")
    private long id;

    @Column(name="name")
    private String name;

    @Column(name="password")
    private String password;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
