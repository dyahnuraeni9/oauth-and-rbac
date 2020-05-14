package com.project.user.management.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.project.user.management.config.ResourceServerConfig;
import com.project.user.management.dto.BaseResponse;
import com.project.user.management.dto.MyPage;
import com.project.user.management.dto.response.UserListResponseDTO;
import com.project.user.management.model.User;
import com.project.user.management.repository.UserRepository;
import com.project.user.management.service.UserService;
import com.project.user.management.util.PageConverter;
import com.project.user.management.util.PropertiesUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

@Service
@PropertySource(value = "classpath:application.properties")
public class UserServiceImpl implements UserService{
    
    @Autowired
    private Environment env;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientDetailsService clientDetailsStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Autowired
    public AuthorizationServerTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setAccessTokenValiditySeconds(-1);

        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }
    
    @Override
    public User getByUsername(String username) {
        try {
            return userRepository.getByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public BaseResponse resetAttempt(Long id) {
        try {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                user.setLoginAttempt(0);
                user.setIsActive(1);
                user.setUpdateTime(new Date());
                userRepository.save(user);

                return BaseResponse.ok();
            } else {
                return BaseResponse.error("99", "Data User tidak ditemukan");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.error("99", e.getMessage());
        }
    }

    @Override
    public OAuth2AccessToken getToken(HashMap<String, String> params) throws HttpRequestMethodNotSupportedException {
        if (params.get("username") == null) {
            throw new UsernameNotFoundException("username not found");
        }

        if (params.get("password") == null) {
            throw new UsernameNotFoundException("password not found");
        }

        params.put("client_id", ResourceServerConfig.CLIENT_ID);
        params.put("client_secret", ResourceServerConfig.CLIENT_SECRET);
        params.put("grant_type", "password");

        DefaultOAuth2RequestFactory defaultOAuth2RequestFactory = new DefaultOAuth2RequestFactory(clientDetailsStore);
        AuthorizationRequest authorizationRequest = defaultOAuth2RequestFactory.createAuthorizationRequest(params);
        authorizationRequest.setApproved(true);

        OAuth2Request oauth2Request = defaultOAuth2RequestFactory.createOAuth2Request(authorizationRequest);
        UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(params.get("username"), params.get("password"));
        Authentication authentication = authenticationManager.authenticate(loginToken);

        OAuth2Authentication authenticationRequest = new OAuth2Authentication(oauth2Request, authentication);
        authenticationRequest.setAuthenticated(true);

        OAuth2AccessToken token = tokenServices().createAccessToken(authenticationRequest);

        Map<String, Object> adInfo = new HashMap<>();
        adInfo.put("role", null);

        try {
            User user = (User) authentication.getPrincipal();
            adInfo.put("role", user.getRole().getRoleTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((DefaultOAuth2AccessToken) token).setAdditionalInformation(adInfo);

        return token;
    }

    @Override
    public OAuth2AccessToken getToken(User user) throws HttpRequestMethodNotSupportedException {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("client_id", ResourceServerConfig.CLIENT_ID);
        params.put("client_secret", ResourceServerConfig.CLIENT_SECRET);
        params.put("grant_type", "password");
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());

        DefaultOAuth2RequestFactory defaultOAuth2RequestFactory = new DefaultOAuth2RequestFactory(clientDetailsStore);
        AuthorizationRequest authorizationRequest = defaultOAuth2RequestFactory.createAuthorizationRequest(params);
        authorizationRequest.setApproved(true);

        OAuth2Request oauth2Request = defaultOAuth2RequestFactory.createOAuth2Request(authorizationRequest);
        UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(user, null, null); // user.getAuthorities()

        OAuth2Authentication authenticationRequest = new OAuth2Authentication(oauth2Request, loginToken);
        authenticationRequest.setAuthenticated(true);

        OAuth2AccessToken token = tokenServices().createAccessToken(authenticationRequest);

        Map<String, Object> adInfo = new HashMap<>();

        adInfo.put("role", null);

        try {
            adInfo.put("role", user.getRole());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((DefaultOAuth2AccessToken) token).setAdditionalInformation(adInfo);

        return token;
    }

    @Override
    public BaseResponse suspendUserLogin(Long id) {
        try {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                user.setIsActive(2); // suspend
                user.setUpdateTime(new Date());
                user.setLastLoginAttempt(new Date());
                userRepository.save(user);

                return BaseResponse.ok();
            } else {
                return BaseResponse.error("99", "Gagal Suspend User, data User tidak ditemukan");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.error("99", e.getMessage());
        }
    }

    @Override
    public BaseResponse updateAttempt(Long id) {
        try {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                if (user.getLoginAttempt() == null) {
                    user.setLoginAttempt(0);
                }

                user.setLoginAttempt(user.getLoginAttempt() + 1);
                user.setUpdateTime(new Date());
                user.setLastLoginAttempt(new Date());
                userRepository.save(user);

                return BaseResponse.ok();
            } else {
                return BaseResponse.error("99", "Data User tidak ditemukan");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.error("99", e.getMessage());
        }
    }

    @Override
    public BaseResponse logout(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                String tokenValue = authHeader.replace("Bearer", "").trim();
                OAuth2AccessToken accessToken = tokenStore().readAccessToken(tokenValue);
                tokenStore().removeAccessToken(accessToken);

                return BaseResponse.ok();
            } else {
                return BaseResponse.error("99", "Session Invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.error("99", e.getMessage());
        }
    }

    @Override
    public BaseResponse findAll() {
        // if( page == null ) {
        //     page = 1 ;
        // }
        // if(size == null ){
        //     size = 10;
        // }

        // page = page - 1;
        // Pageable paging = PageRequest.of(page, size, Sort.by("id").descending());        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userSession = (User) auth.getPrincipal();

        try {
            if (!userSession.getRole().getId().equals(PropertiesUtil.getRoleSysAdminID(env))) {
                return BaseResponse.error("401", "Hanya Role SYSADMIN yang dapat List Data User");
            }

            // List<User> userDataResponse;
            
            // String search = "";
            // // Page<UserDataResponse> responseUser = new Page<UserDataResponse>;
            // if ( isActive != null ) {
            //     userDataResponse = userRepository.findUserByStatus(isActive, paging);
            // } 
            // else {
                List<User> userDataResponse = userRepository.findAll();
    
            // }

            // if( isActive != null){
            //     search += "&isActive"+isActive;
            // }
            
            // // responseUser = userDataResponse ;
            // Page<UserListResponseDTO> responseUserData = userDataResponse.map(this::fromEntityDataUser);

            // PageConverter<UserListResponseDTO> converter = new PageConverter<>();
            // MyPage<UserListResponseDTO> response = converter.convert(responseUserData, null, search);
            
            return BaseResponse.ok(userDataResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.error("401", e.getMessage());
        }
    }

    private UserListResponseDTO fromEntityDataUser(User userData) {
        UserListResponseDTO response = new UserListResponseDTO();
        BeanUtils.copyProperties(userData, response);
        return response;
    }
}