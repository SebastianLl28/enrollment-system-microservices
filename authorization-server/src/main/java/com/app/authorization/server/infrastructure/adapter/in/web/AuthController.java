package com.app.authorization.server.infrastructure.adapter.in.web;

import com.app.authorization.server.application.dto.command.LoginCommand;
import com.app.authorization.server.application.dto.command.RegisterUserCommand;
import com.app.authorization.server.application.dto.command.TwoFactorValidateCommand;
import com.app.authorization.server.application.dto.command.UpdateUserRolesCommand;
import com.app.authorization.server.application.dto.command.VerifyTwoFactorCommand;
import com.app.authorization.server.application.dto.response.LoginResponse;
import com.app.authorization.server.application.dto.response.RegisterUserResponse;
import com.app.authorization.server.application.dto.response.TwoFactorConfirmResponse;
import com.app.authorization.server.application.dto.response.TwoFactorInitResponse;
import com.app.authorization.server.application.dto.response.UserRbacResponse;
import com.app.authorization.server.application.dto.response.UserResponse;
import com.app.authorization.server.application.dto.response.UserSummaryResponse;
import com.app.authorization.server.application.dto.response.ValidateTokenResponse;
import com.app.authorization.server.application.dto.response.VerifyTwoFactorResponse;
import com.app.authorization.server.application.port.in.FindUserByUserCase;
import com.app.authorization.server.application.port.in.FindUserByUserIdUseCase;
import com.app.authorization.server.application.port.in.LoginUseCase;
import com.app.authorization.server.application.port.in.RegisterUserUseCase;
import com.app.authorization.server.application.port.in.TwoFactorSetupUseCase;
import com.app.authorization.server.application.port.in.TwoFactorValidateUseCase;
import com.app.authorization.server.application.port.in.UpdateUserRolesUseCase;
import com.app.authorization.server.application.port.in.ValidateTokenUseCase;
import com.app.authorization.server.application.port.in.VerifyTwoFactorUseCase;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alonso
 */
@RestController
//@RequestMapping("/auth")
public class AuthController {
  
  private final LoginUseCase loginUseCase;
  private final RegisterUserUseCase registerUserUseCase;
  private final ValidateTokenUseCase validateTokenUseCase;
  private final TwoFactorSetupUseCase twoFactorSetupUseCase;
  private final TwoFactorValidateUseCase twoFactorValidateUseCase;
  private final VerifyTwoFactorUseCase verifyTwoFactorUseCase;
  private final FindUserByUserCase findUserUseCase;
  private final FindUserByUserIdUseCase findUserByUserIdUseCase;
  private final UpdateUserRolesUseCase updateUserRolesUseCase;
  
  public AuthController(LoginUseCase loginUseCase, RegisterUserUseCase registerUserUseCase,
    ValidateTokenUseCase validateTokenUseCase, TwoFactorSetupUseCase twoFactorSetupUseCase,
    TwoFactorValidateUseCase twoFactorValidateUseCase,
    VerifyTwoFactorUseCase verifyTwoFactorUseCase,
    FindUserByUserCase findUserUseCase, FindUserByUserIdUseCase findUserByUserIdUseCase,
    UpdateUserRolesUseCase updateUserRolesUseCase) {
    this.registerUserUseCase = registerUserUseCase;
    this.loginUseCase = loginUseCase;
    this.validateTokenUseCase = validateTokenUseCase;
    this.twoFactorSetupUseCase = twoFactorSetupUseCase;
    this.twoFactorValidateUseCase = twoFactorValidateUseCase;
    this.verifyTwoFactorUseCase = verifyTwoFactorUseCase;
    this.findUserUseCase = findUserUseCase;
    this.findUserByUserIdUseCase = findUserByUserIdUseCase;
    this.updateUserRolesUseCase = updateUserRolesUseCase;
  }
  
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginCommand loginCommand) {
    return ResponseEntity.ok(loginUseCase.login(loginCommand));
  }
  
  @PostMapping("/register")
  public ResponseEntity<RegisterUserResponse> register(
    @Valid @RequestBody RegisterUserCommand registerUserCommand) {
    return ResponseEntity.ok(registerUserUseCase.register(registerUserCommand));
  }
  
  @GetMapping("/validateToken")
  public ResponseEntity<ValidateTokenResponse> validateToken(
    @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
    if (authHeader == null || authHeader.isEmpty()) {
      throw new IllegalArgumentException("Token is missing");
    }
    String token = authHeader;
    if (authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
    }
    return ResponseEntity.ok(validateTokenUseCase.validateToken(token));
  }
  
  @PostMapping("/2fa/init")
  public TwoFactorInitResponse init2fa(Principal principal) {
    return twoFactorSetupUseCase.initiate(principal.getName());
  }
  
  @PostMapping("/2fa/confirm")
  public TwoFactorConfirmResponse confirm2fa(Principal principal,
    @RequestBody TwoFactorValidateCommand body) {
    return twoFactorValidateUseCase.validate(principal.getName(), body);
  }
  
  @PostMapping("/2fa/verify")
  public VerifyTwoFactorResponse verify2fa(@RequestBody VerifyTwoFactorCommand body) {
    return verifyTwoFactorUseCase.verify(body);
  }
  
  @GetMapping("/profile")
  public UserResponse getProfile(Principal principal) {
    return findUserUseCase.findByUsername(principal.getName());
  }
  
  @GetMapping("/user/{userId}")
  public UserSummaryResponse getUserById(@PathVariable Integer userId) {
    return findUserByUserIdUseCase.findByUserId(userId);
  }
  
  @GetMapping("/user")
  public ResponseEntity<List<UserRbacResponse>> getUsers() {
    return ResponseEntity.ok(findUserUseCase.findAllUsersWithRbac());
  }

  @PutMapping("/rbac/users/{userId}/roles")
  public ResponseEntity<UserRbacResponse> updateUserRoles(@PathVariable Integer userId,
    @RequestBody UpdateUserRolesCommand command) {
    UpdateUserRolesCommand updatedCommand = new UpdateUserRolesCommand(
      userId,
      command.roleIds()
    );
    UserRbacResponse response = updateUserRolesUseCase.updateUserRoles(updatedCommand);
    return ResponseEntity.ok(response);
  }
}
