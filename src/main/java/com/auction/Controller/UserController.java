package com.auction.Controller;

import com.auction.payload.ApiResponse;
import com.auction.payload.UserDto;
import com.auction.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    //Registration for seller
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
    {
        UserDto createUser = this.userService.createUser(userDto);
        return new ResponseEntity<>(createUser,HttpStatus.CREATED );
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUserDetails(@RequestBody UserDto userDto, @PathVariable int userId)
    {
        UserDto updateUser = this.userService.updateUser(userDto,userId);
        return  ResponseEntity.ok(updateUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable int userId)
    {
        UserDto getUser = this.userService.getUserById(userId);
        return ResponseEntity.ok(getUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable int userId)
    {
        this.userService.deleteUser(userId);
//        return new ResponseEntity(Map.of("message","Seller Deleted Successfully") , HttpStatus.OK);
        return new ResponseEntity<>(new ApiResponse("User Delete successfully",true),HttpStatus.OK);
    }

    @GetMapping("/")
    public  ResponseEntity<List<UserDto>> getAllUser()
    {
        return ResponseEntity.ok(this.userService.getAllUser());
    }
}
