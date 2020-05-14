package com.project.user.management.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.project.user.management.dto.BaseResponse;
import com.project.user.management.dto.request.LoginRequestDTO;
import com.project.user.management.model.User;
import com.project.user.management.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    
    @Autowired
    private UserService userService;


    @PostMapping("login")
    public BaseResponse<OAuth2AccessToken> login(@RequestBody LoginRequestDTO request) {
        HashMap<String, String> params = request.getMap();
        User checkUser = userService.getByUsername(params.get("username"));
        if (checkUser == null) {
            return BaseResponse.error("0002", "User Not Found");
        }

        if (checkUser != null && checkUser.getIsActive() == 0) {
            return BaseResponse.error("0001", "Your account is inactive, please contact support center");
        }

        if (checkUser != null && checkUser.getIsActive() == 2) {
            return BaseResponse.error("0004", "Your account has been suspended, please contact support center");
        }

        try {
            OAuth2AccessToken token = this.userService.getToken(params);
            if (checkUser != null) {
                userService.resetAttempt(checkUser.getId());
            }

            return BaseResponse.ok(token);
        } catch (Exception e) {
            e.printStackTrace();
            if (checkUser != null) {
                if (checkUser.getLoginAttempt() != null && checkUser.getLoginAttempt() == 5) {
                    userService.suspendUserLogin(checkUser.getId());

                    return BaseResponse.error("0004", "Your account has been suspended, please contact support center");
                } else {
                    userService.updateAttempt(checkUser.getId());

                    return BaseResponse.error("0005", "Your password is incorrect");
                }
            }
        }

        return BaseResponse.error("500", "Internal Server Error");
    }

    @PostMapping("logout")
    public BaseResponse logout(HttpServletRequest request) {
        return userService.logout(request);
    }

    @GetMapping
    public BaseResponse getUserData(
    // ,HttpServletRequest request
    ) {
      
        return userService.findAll();

       // return new ResponseEntity<>(response, HttpStatus.OK);
    }
}