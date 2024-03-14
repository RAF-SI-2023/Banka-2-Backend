package rs.edu.raf.IAMService.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import org.webjars.NotFoundException;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.User;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    /**
     * Finds user by id, and returns it as UserDto. Everyone? can search for users by id.
     *
     * @param id - id of the user that needs to be found
     * @return UserDto - user that was found
     * @throws NotFoundException - if user with given id is not found
     */
    UserDto findById(Long id);

    /**
     * Finds user by email, and returns it as UserDto. Everyone? can search for users by email.
     *
     * @param email - email of the user that needs to be found
     * @return UserDto - user that was found
     * @throws NotFoundException - if user with given email is not found
     */

    UserDto findByEmail(String email);
    PrivateClientDto createPrivateClient(PrivateClientDto privateClientDtoDto);
    CorporateClientDto createCorporateClient(CorporateClientDto corporateClientDto);
    Long activateClient(String clientId, String password);

    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeDto activateEmployee(String email, String password);

    /**
     * Finds User by email, and returns it as Optional<User>. This method is used for security purposes, like
     * changing a password.
     *
     * @param email - email of the user that needs to be found
     * @return Optional<User> - user that was found
     */
    User employeeActivation(int id);

    User employeeDeactivation(int id);

    Optional<User> findUserByEmail(String email);

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
    UserDto deleteUserByEmail(String email);

    /**
     * Updates users information, and returns the updated user as UserDto. Role and permission checks are done in the controller.
     *
     * @param userDto - user that needs to be updated
     * @return UserDto - user that was updated
     */
    UserDto updateUser(UserDto userDto);

}
