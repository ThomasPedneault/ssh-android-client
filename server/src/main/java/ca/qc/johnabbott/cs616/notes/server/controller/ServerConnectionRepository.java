package ca.qc.johnabbott.cs616.notes.server.controller;

import ca.qc.johnabbott.cs616.notes.server.model.ServerConnection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "serverconnection", path = "serverconnection")
public interface ServerConnectionRepository extends CrudRepository<ServerConnection, Long> {
}
