package no.hvl.dat109.gruppe22.yatzy.service;

import no.hvl.dat109.gruppe22.yatzy.model.Bruker;
import no.hvl.dat109.gruppe22.yatzy.repository.BrukerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class YatzyUserDetailsService implements UserDetailsService {
    @Autowired
    private BrukerRepository brukerRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Bruker bruker = brukerRepo.findByBrukernavn(username).orElse(null);

        if (bruker == null) {
            throw new UsernameNotFoundException(username);
        }

        UserDetails userDetails = User.builder()
                .username(bruker.getBrukernavn())
                .password(bruker.getPwhash())
                .roles(bruker.roller())
                .build();

        return userDetails;
    }
}
