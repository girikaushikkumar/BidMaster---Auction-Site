package com.auction.payload;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	private int id;

    @NotBlank
	@Size(min = 4,message = "UsName must be minimum four character")
	private String name;
	@Email(message = "Email id is not valid")
	private String email;

	private String phone;
	private String password;
	private String role;

}
