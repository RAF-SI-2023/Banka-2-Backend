package rs.edu.raf.IAMService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.IAMService.data.entites.Agent;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
  
    Optional<Employee> findEmployeeByEmail(String email);

    Optional<User> findByUsername(String username);

    Integer removeUserByEmail(String email);

    Optional<User> findById(int id);
}
