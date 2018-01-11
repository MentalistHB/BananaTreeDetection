package com.btd.transfer;

import org.hibernate.validator.constraints.NotEmpty;

public class UserLoginTO {

	@NotEmpty(message = "The email must nut be empty")
	private String email;
	@NotEmpty(message = "The password must nut be empty")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
