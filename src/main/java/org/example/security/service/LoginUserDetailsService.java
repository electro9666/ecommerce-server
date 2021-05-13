package org.example.security.service;

import java.util.stream.Collectors;

import org.example.entity.LoginMember;
import org.example.repository.LoginMemberRepository;
import org.example.security.dto.LoginMemberDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class LoginUserDetailsService  implements UserDetailsService {

    private final LoginMemberRepository loginMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("UserDetailsService loadUserByUsername " + username);

        LoginMember loginMember = loginMemberRepository.findByUsername(username, false);
        
        System.out.println("loginMember?" + loginMember);

        if(loginMember == null){
            throw new UsernameNotFoundException("Check User username or from Social ");
        }

        log.info("-----------------------------");
        log.info(loginMember);

        LoginMemberDTO loginMemberDto = new LoginMemberDTO(
        		loginMember.getId(),
                loginMember.getUsername(),
                loginMember.getPassword(),
                loginMember.getName(),
                loginMember.isFromSocial(),
                loginMember.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toSet())
        );
        loginMemberDto.setName(loginMember.getName());
        loginMemberDto.setFromSocial(loginMember.isFromSocial());

        return loginMemberDto;
    }
}
