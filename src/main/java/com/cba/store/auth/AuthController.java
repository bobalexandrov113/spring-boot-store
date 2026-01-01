package com.cba.store.auth;

import com.cba.store.users.UserMapper;
import com.cba.store.users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody AuthRequestDto request,
            HttpServletResponse response
    ){
       authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
       );
       var userOptional = userRepository.findByEmail(request.getEmail());
       var user = userOptional.get();
       var accessToken = jwtService.generateAccessToken(user);
       var refreshToken = jwtService.generateRefreshToken(user);

       var cookie = new Cookie("refreshToken", refreshToken.toString());
       cookie.setPath("/auth/refresh");
       cookie.setHttpOnly(true);
       cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
       cookie.setSecure(true);
       response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }



    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value="refreshToken") String refreshToken
    )
    {
        var jwt = jwtService.parse(refreshToken);

        if(jwt == null || jwt.isExpired())
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var userId = jwt.getUserId();
        var user = userRepository.findById(userId);
        var accessToken = jwtService.generateAccessToken(user.get());

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser()
    {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long)authentication.getPrincipal();
        var userOptional = userRepository.findById(userId);
        var user = userOptional.get();
        if(user == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(userMapper.userToUserDto(user));

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e){
        return ResponseEntity.status(401).body(e.getMessage());
    }

}
