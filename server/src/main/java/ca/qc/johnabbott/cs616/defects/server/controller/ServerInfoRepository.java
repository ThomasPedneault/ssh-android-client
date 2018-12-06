package ca.qc.johnabbott.cs616.defects.server.controller;

import ca.qc.johnabbott.cs616.defects.server.model.ServerInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "serverinfo", path = "serverinfo")
public interface ServerInfoRepository extends CrudRepository<ServerInfo, Long> {
}
