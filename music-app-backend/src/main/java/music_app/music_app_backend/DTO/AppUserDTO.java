package music_app.music_app_backend.DTO;

import music_app.music_app_backend.Entity.AppUser;

public class AppUserDTO {
    private Long id;
    private String userName;
    private String password;

    public AppUserDTO() {}

    public AppUserDTO(String userName) {
        this.userName = userName;
    }

    public AppUserDTO(Long id, String userName) {
        setId(id);
        setUserName(userName);
    }

    public Long getId() { return id; }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() { return userName; }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() { return password; }
}
