package org.vitalii.fedyk.librarygenerated.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vitalii.fedyk.librarygenerated.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailAndIdNot(String email, Long id);
}
