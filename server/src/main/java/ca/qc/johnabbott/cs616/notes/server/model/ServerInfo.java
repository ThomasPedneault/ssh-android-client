package ca.qc.johnabbott.cs616.notes.server.model;

import javax.persistence.*;

@Entity
@Table(name = "serverinfo")
public class ServerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="serverinfoid")
    private long id;

    @Column(name="ip")
    private String ip;

    @Column(name="name")
    private String name;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "owner")
    private User owner;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
