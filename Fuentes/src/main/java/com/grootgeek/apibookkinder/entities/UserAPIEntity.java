package com.grootgeek.apibookkinder.entities;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "BD_APIBOOKKINDER", schema = "ADMIN")
@Data
public class UserAPIEntity {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)

	@Column
	private String userName;
	
	@Column
	private String password;
	
	@Column
	private String appName;
	
	public UserAPIEntity() {
		super();
	}

	@Override
	public String toString() {
		return "UserEntity [userName=" + userName + ", password= [PROTECTED]" + ", appName="
				+ appName + "]";
	}
	
	

}
