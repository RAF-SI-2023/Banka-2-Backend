package rs.edu.raf.IAMService.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.data.entites.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    UserDto findById(Long id);

    UserDto findByEmail(String email);

    PrivateClientDto createPrivateClient(PrivateClientDto privateClientDtoDto);

    CorporateClientDto createCorporateClient(CorporateClientDto corporateClientDto);

    Long passwordActivation(String clientId, String password);

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    EmployeeDto activateEmployee(String email, String password);

    AgentDto createAgent(AgentDto agentDto);

    User employeeActivation(int id);

    User employeeDeactivation(int id);

    Optional<User> findUserByEmail(String email);


    List<Permission> getUserPermissions(Long id);

    void addUserPermission(Long id, Permission permission);

    void removeUserPermission(Long id, Permission permission);

    void deleteAndSetUserPermissions(Long id, List<Permission> permissionList);

    void sendToQueue(String email, String urlLink);

    /**
     * Updates the UserEntity, but should be only used for updating passwords,activation and such. For regular use
     * use updateUser(UserDto userDto) method.
     *
     * @param user - user that needs to be updated
     * @return User - user that was updated
     */
    User updateEntity(User user);

    /**
     * Finds all users, and returns them as List<UserDto>. Role and permission checks are done in the controller.
     *
     * @return List<UserDto> - list of all users
     */
    List<UserDto> findAll();

    /**
     * Deletes user by email, and returns the deleted user as UserDto. Role and permission checks are done in the controller.
     *
     * @param email - email of the user that needs to be deleted
     * @return UserDto - user that was deleted //TODO check if u want to return void
     */
    Integer deleteUserByEmail(String email);

    /**
     * Updates users information, and returns the updated user as UserDto. Role and permission checks are done in the controller.
     *
     * @param userDto - user that needs to be updated
     * @return UserDto - user that was updated
     */
    UserDto updateUser(UserDto userDto);


    void PasswordResetsendToQueue(String email,String urlLink);


    User setAgentLimit(int id, BigDecimal limit);
}
