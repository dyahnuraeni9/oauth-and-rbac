package com.project.user.management.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.project.user.management.dto.BaseResponse;
import com.project.user.management.model.User;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.HttpRequestMethodNotSupportedException;

public interface UserService {
    
    User getByUsername(String username);
    BaseResponse resetAttempt(Long id);
    OAuth2AccessToken getToken(User user) throws HttpRequestMethodNotSupportedException;
    OAuth2AccessToken getToken(HashMap<String, String> params) throws HttpRequestMethodNotSupportedException;
    BaseResponse suspendUserLogin(Long id);
    BaseResponse updateAttempt(Long id);
    BaseResponse logout(HttpServletRequest request);
    BaseResponse findAll();
}