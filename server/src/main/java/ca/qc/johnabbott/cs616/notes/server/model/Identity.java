package ca.qc.johnabbott.cs616.notes.server.model;

import javax.persistence.*;

@Entity
@Table(name = "identity")
public class Identity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="identityid")
    private long id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "owner")
    private User owner;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
