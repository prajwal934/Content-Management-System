package com.example.cms.serviceimpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.cms.dto.UserRequest;
import com.example.cms.dto.UserResponse;
import com.example.cms.exception.UserAlreadyExistByEmailException;
import com.example.cms.exception.UserNotFoundByIdException;
import com.example.cms.model.User;
import com.example.cms.repository.UserRepository;
import com.example.cms.service.UserService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepository;
	private ResponseStructure<UserResponse> responseStructure;
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) {
		if(userRepository.existsByEmail(userRequest.getEmail()))
			throw new UserAlreadyExistByEmailException("Failed to user register");
		
		User user = userRepository.save(mapToUserEntity(userRequest , new User()));
		
		return ResponseEntity.ok(responseStructure.setStatuscode(HttpStatus.OK.value())
				.setMessage("User Registered Successfully")
				.setData(mapToUserResponse(user)));
	}
	
	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder().userId(user.getUserId()).userName(user.getUserName()).email(user.getEmail())
				.lastModifiedAt(user.getLastModifiedAt())
				.createdAt(user.getCreatedAt())
				.build();
	}
	
	private User mapToUserEntity(UserRequest userRequest , User user) {
		user.setEmail(userRequest.getEmail());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setUserName(userRequest.getUserName());
		return user;
	}
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> findUniqueUser(int userId) {
		return userRepository.findById(userId).map(user -> ResponseEntity.ok(responseStructure.setStatuscode(HttpStatus.OK.value())
				.setMessage("User fetched suucessfully!").setData(mapToUserResponse(user))))
				.orElseThrow(()-> new UserNotFoundByIdException("Invalid User") );
	}

	
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId) {
		return userRepository.findById(userId).map(user -> {
			user.setDeleted(true);
			userRepository.save(user);
			return ResponseEntity.ok(responseStructure.setStatuscode(HttpStatus.OK.value())
					.setMessage("user deleted successfully!").setData(mapToUserResponse(user)));
		}).orElseThrow(() -> new UserNotFoundByIdException("Invalid user")); 
	}

	
}
