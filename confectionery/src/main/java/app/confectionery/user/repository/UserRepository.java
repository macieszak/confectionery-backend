package app.confectionery.user.repository;

import app.confectionery.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Integer> {

    //REGISTRATION FUNCTIONALITY
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
    boolean existsUserByUsernameOrEmail(String username, String email);




}
