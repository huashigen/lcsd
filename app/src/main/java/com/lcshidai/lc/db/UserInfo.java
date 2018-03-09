package com.lcshidai.lc.db;

public class UserInfo
{
	//编号
	private String uid;
	
	//昵称
	private String uname;
	
	private String email;
	
	private String password;
	
	//令牌
	private String token;
	
	//英文名称
	private String secret;
	
	//身份
	private String role;
	
	private byte[] face;
	
	private String login_time;
	
	private int is_login;
    //1：初始化,2：待审核,3：已签约
	private String contract_status = "";

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public int getIs_login() {
		return is_login;
	}

	public void setIs_login(int is_login) {
		this.is_login = is_login;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getContract_status() {
		return contract_status;
	}

	public void setContract_status(String contract_status) {
		this.contract_status = contract_status;
	}

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

	public byte[] getFace() {
		return face;
	}

	public void setFace(byte[] face) {
		this.face = face;
	}

	public String getLogin_time() {
		return login_time;
	}

	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}
}