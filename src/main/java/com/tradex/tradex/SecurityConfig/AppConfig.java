package com.tradex.tradex.SecurityConfig;

import com.tradex.tradex.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        // We reuse your awesome triple-search method so the filter can find the user
        // no matter what identifier is stamped on the token!
        return identifier -> userRepository.findByUsernameOrEmailOrPhoneNumber(identifier, identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in database"));
    }
}