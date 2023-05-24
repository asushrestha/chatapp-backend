package com.leapfrog.chattest.commons.context;

import com.leapfrog.chattest.entity.Users;
import com.leapfrog.chattest.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
public class ContextHolderService {
    private UserRepository userRepository;
    @Autowired
    public ContextHolderService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public Context getContext() {
        return  ContextHolder.get();
    }

    public Boolean setContext(String userName){
        Optional<Users> userOptional = userRepository.findUserByUserName(userName.toUpperCase());
        if(userOptional.isPresent()){
            Users users = userOptional.get();
            ContextHolder thread = new ContextHolder(users.getId(),users.getUserName());
            thread.run();
            return  true;
        }

        return  false;
    }
}
