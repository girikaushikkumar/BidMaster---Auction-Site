package com.auction.services;

import java.util.List;

import com.auction.payload.UserDto;


public interface UserService {

	UserDto registerNewUser(UserDto user);
	UserDto createUser(UserDto userDto);
	
	UserDto updateUser(UserDto userDto, Integer userId);
	
	UserDto getUserById(Integer userId);
	
	List<UserDto> getAllUser();
	
	void deleteUser(Integer userId);


}
