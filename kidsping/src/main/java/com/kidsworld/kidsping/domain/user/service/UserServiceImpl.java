package com.kidsworld.kidsping.domain.user.service;

import com.kidsworld.kidsping.domain.user.dto.response.GetKidListResponse;
import com.kidsworld.kidsping.domain.user.dto.request.RegisterRequest;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.exception.DuplicateEmailException;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }

        return userRepository.save(registerRequest.toEntity());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }


    @Override
    @Transactional(readOnly = true)
    public List<GetKidListResponse> getKidsList(Long userId) {
        return userRepository.findByUserIdAndIsDeletedFalse(userId).stream()
                .map(GetKidListResponse::from)
                .collect(Collectors.toList());
    }


    @Override
    public User save(RegisterRequest registerRequest) { return userRepository.save(registerRequest.toEntity());}

    //기존 유저 정보 수정
    @Override
    public User update(User user) { return userRepository.save(user);}

    @Override
    public Optional<User> findBySocialId(String socialId) { return userRepository.findBySocialId(socialId);}

}
