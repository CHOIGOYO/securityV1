package com.choigoyo.securityV1.config.auth;

import com.choigoyo.securityV1.entity.User;
import com.choigoyo.securityV1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * security 설정에서 설정한 .loginProcessingUrl("/login")
 * login설정이오면 자동으로 UserDetailsService타입으로 IOC되어있는 loadByUsername 함수가 실행된다*/
@Service
public class PrincipalDerailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    /*
     * Security session(Authentication( UserDetails ( PrincipalDerails ) )
     * */
    @Override
    // 로그인폼 html에서 name값이 username이 아니라면 받아지지 않는다.
    // 다른 name을 사용해야한다면 Securityconfig 파일에서  usernameParameter("사용할 name")을 설정해야한다.
    // email로 name을 변경하여 사용하였음.
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 받아온 User email 값을 DB에 있는지 확인
        User userEntity = userRepository.findByEmail(email);
        if (userEntity != null) {
            return new PrincipalDerails(userEntity);
        }
        return null;
    }
}
