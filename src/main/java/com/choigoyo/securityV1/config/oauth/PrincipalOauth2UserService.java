package com.choigoyo.securityV1.config.oauth;

import com.choigoyo.securityV1.config.auth.PrincipalDerails;
import com.choigoyo.securityV1.config.oauth.provider.FaceBookUserInfo;
import com.choigoyo.securityV1.config.oauth.provider.GoogleUserInfo;
import com.choigoyo.securityV1.config.oauth.provider.NaverUserInfo;
import com.choigoyo.securityV1.config.oauth.provider.OAuth2UserInfo;
import com.choigoyo.securityV1.entity.User;
import com.choigoyo.securityV1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글회원의 회원가입 시 password 를 위한 Autowired
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    // 기존 회원정보와 중복되는지 확인을 위해 Autowired
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        /*
         * 구글로그인버튼 클릭 -> 구글로그인창-> 로그인 완료 -> 코드를 리턴 - > AccessToken요청
         * userRequest정보 -> loadUser 메서드 호출 -> 회원 프로필을 받음
         * */
        System.out.println("USER-Request-getAttributes :"+ super.loadUser(userRequest).getAttributes()); // 회원번호와 이름 ,이메일 정보를 가져올 수 있다
        System.out.println("USER-Request-getClientRegistration"+userRequest.getClientRegistration()); // 회원이 어떤 Oauth로 로그인했는지 알 수 있다 - registrationId로 어떤 oauth로 로그인했는지 확인가능
        OAuth2User oAuth2User = super.loadUser(userRequest); // 회원프로필을 받기위한 메서드 loadUser()
        System.out.println("==============================================");
        System.out.println("getAttributes : "+ oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        // 구글 로그인을 한 경우
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) { 
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) { // 페이스북 로그인을 한 경우
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FaceBookUserInfo(oAuth2User.getAttributes());

        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) { // 네이버 로그인을 한 경우
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        } else {
            System.out.println("구글,페이스북,네이버 로그인으로 시도해주세요.");
        }

        // Oauth 로그인을 한 사용자의 회원가입을 위한 정보 수집
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String userName = provider + "_" + providerId; //  userName 의 중복 방지를 위해서
        String email = oAuth2UserInfo.getEmail(); // email 정보
        String password = bCryptPasswordEncoder.encode("겟인데어"); // password
        String role = "ROLE_USER"; // role

        // 이미 존재하는 회원인지 아닌지 확인
        User userEntity = userRepository.findByUsername(userName);
        if (userEntity == null) { // userEntity가 null이라면
            userEntity = User.builder() // 생성자를 통해 값을 세팅
                    .username(userName)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
            System.out.println("회원정보 저장완료 : " + userEntity);
        } else {
            System.out.println("이미 존재하는 회원입니다 : "+ userEntity.getEmail());
        }
        // 세팅이 완료된 principalDetails 는 authentication 에 들어가게 됨
        return new PrincipalDerails(userEntity,oAuth2User.getAttributes());
    }
}
