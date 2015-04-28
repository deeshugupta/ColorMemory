package com.colormem.database;

public class User {
	
	private Long id;
	private String name;
	private Long level;
	private Long cheats;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public Long getCheats() {
		return cheats;
	}

	public void setCheats(Long cheats) {
		this.cheats = cheats;
	}

	
	public User(Long id, String name, Long level, Long cheats){
		this.cheats = cheats;
		this.id = id;
		this.name = name;
		this.level =level;
	}

}
