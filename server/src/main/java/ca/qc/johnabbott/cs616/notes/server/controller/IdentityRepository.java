package ca.qc.johnabbott.cs616.notes.server.controller;


import ca.qc.johnabbott.cs616.notes.server.model.Identity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "identity", path = "identity")
public interface IdentityRepository extends CrudRepository<Identity, Long> {
}