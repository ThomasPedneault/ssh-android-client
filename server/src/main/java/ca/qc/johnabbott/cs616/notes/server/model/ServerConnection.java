package ca.qc.johnabbott.cs616.notes.server.model;

import javax.persistence.*;

@Entity
@Table(name = "serverconnection")
public class ServerConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serverconnectionid")
    private long id;

    @ManyToOne(targetEntity = ServerInfo.class)
    @JoinColumn(name = "serverinfo")
    private ServerInfo serverInfo;

    @ManyToOne(targetEntity = Identity.class)
    @JoinColumn(name = "identity")
    private Identity identity;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "owner")
    private User owner;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
