package com.auction.services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.auction.Entities.Role;
import com.auction.Entities.User;
import com.auction.exception.ResourceNotFoundException;
import com.auction.repositories.RoleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.auction.payload.UserDto;
import com.auction.repositories.UserRepo;
import com.auction.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiesImpl implements UserService {
	
	@Autowired
	private UserRepo userRepo;
	@Autowired
    private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepo roleRepo;

	@Override
	public UserDto registerNewUser(UserDto userDto) {
		String roleName = userDto.getRole();
		User user = this.dtoToUser(userDto);
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		Role role;
		if(roleName.equals("Admin")) {
			role = this.roleRepo.findById(101).get();
		} else if(roleName.equals("Seller")) {
			role = this.roleRepo.findById(201).get();
		} else {
			role = this.roleRepo.findById(301).get();
		}

		user.getRoles().add(role);
		User newUser = this.userRepo.save(user);
		return this.userToDto(newUser);
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.dtoToUser(userDto);
		User savelist = this.userRepo.save(user);
		return this.userToDto(savelist);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = this.userRepo.findById(userId).orElse(null);

	    if (user == null) {
	        // Seller not found, handle the situation accordingly (e.g., return an error response)
	        return null;
	    }

		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPhone(userDto.getPhone());
		user.setPassword(userDto.getPassword());

		User updatedUser = this.userRepo.save(user);
	    return this.userToDto(updatedUser);
	}
	@Override
	public UserDto getUserById(Integer userId) {
		// TODO Auto-generated method stub
		User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));
		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUser() {
		List<User> users=this.userRepo.findAll();
		List<UserDto> userDto =users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
		return userDto;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));
		this.userRepo.delete(user);
	}

	User dtoToUser(UserDto userDto)
	{
		User user = this.modelMapper.map(userDto,User.class);
//		Seller seller = new Seller();
//		seller.setId(sellerDto.getId());
//		seller.setName(sellerDto.getName());
//		seller.setEmail(sellerDto.getEmail());
//		seller.setPhone(sellerDto.getPhone());
//		seller.setPassword(sellerDto.getPassword());
		return user;
	}

	private UserDto userToDto(User user)
	{
		UserDto userDto = this.modelMapper.map(user, UserDto.class); //you can use this or below which is in comment
//		SellerDto sellerDto = new SellerDto();
//		sellerDto.setId(seller.getId());
//		sellerDto.setName(seller.getName());
//		sellerDto.setEmail(seller.getEmail());
//		sellerDto.setPhone(seller.getPhone());
//		sellerDto.setPassword(seller.getPassword());
		return userDto;
	}

}
