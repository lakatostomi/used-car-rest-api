package org.usedcar.rest.webservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.usedcar.rest.webservice.business.UserService;
import org.usedcar.rest.webservice.dto.HttpResponseDTO;
import org.usedcar.rest.webservice.dto.LoginRequestDTO;
import org.usedcar.rest.webservice.dto.RegistrationDTO;
import org.usedcar.rest.webservice.model.User;
import org.usedcar.rest.webservice.security.Auth0JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/rest/v1/auth")
@Tag(name = "AuthController", description = "Handles endpoints that are connected with authentication process!")
public class AuthController {

    private final UserService userService;
    private final Auth0JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, Auth0JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Handles the signup process")
    @ApiResponses(value = {@ApiResponse(responseCode = "202", description = "Registration is finished successfully",
            content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Field validation fails, Email already exists, or Request body is missing."
                    , content = @Content(mediaType = "application/json"))})
    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @Parameter(description = "Contains the necessary fields for registration")
            @RequestBody @Valid RegistrationDTO registrationDTO) {
        userService.saveUser(registrationDTO);
        return ResponseEntity.accepted().body("Registration was successful, please login!");
    }

    @Operation(summary = "Handles the login process")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User is logged in successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(allOf = {HttpResponseDTO.class}))),
            @ApiResponse(responseCode = "400", description = "Field validation fails, Request body is missing"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "User provide bad credentials"
                    , content = @Content(mediaType = "application/json"))})
    @PostMapping("/login")
    public ResponseEntity<HttpResponseDTO> login(
            @Parameter(description = "Contains the necessary fields to login a User")
            @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        User user = userService.findByEmail(loginRequestDTO.getEmail());
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        String accessToken = jwtUtils.generateAccessToken((UserDetails) authenticate.getPrincipal());
        String refreshToken = jwtUtils.generateRefreshToken((UserDetails) authenticate.getPrincipal());
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Refresh-token", "Bearer " + refreshToken);
        return new ResponseEntity<>(new HttpResponseDTO(accessToken, refreshToken), HttpStatus.OK);
    }

    @Operation(summary = "Handles the logout process")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Logout is finished successfully",
            content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        userService.logoutUser();
        String jwtToken = request.getHeader("Authorization");
        jwtUtils.revokeToken(jwtToken.substring(("Bearer ").length()));
        return ResponseEntity.ok("Logout was successful");
    }
}
