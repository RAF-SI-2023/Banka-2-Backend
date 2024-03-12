package rs.edu.raf.IAMService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.IAMService.data.dto.ClientActivationDto;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import rs.edu.raf.IAMService.services.UserService;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/public/private-client")
    public PrivateClientDto createPrivateClient(@RequestBody PrivateClientDto clientDto) {
        return userService.createPrivateClient(clientDto);
    }

    @PostMapping("/public/corporate-client")
    public CorporateClientDto createCorporateClient(@RequestBody CorporateClientDto clientDto) {
        return userService.createCorporateClient(clientDto);
    }

    @PatchMapping("/public/{clientId}/activate")
    public Long activateClient(@PathVariable String clientId,
                               @RequestBody ClientActivationDto dto) {
        return userService.activateClient(clientId, dto.getPassword());
    }
}
