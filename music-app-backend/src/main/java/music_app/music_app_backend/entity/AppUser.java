package music_app.music_app_backend.entity;

import jakarta.persistence.*;
import music_app.music_app_backend.DTO.AppUserDTO;

import java.util.Set;

@Entity
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userName;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserFavorite> favorites;

    public AppUser() {}
    public AppUser(Long id) { setId(id); }
    public AppUser(String userName, String password) {
        setUserName(userName); setPassword(password);
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserFavorite> getFavorites() {
        return favorites;
    }
    public void setFavorites(Set<UserFavorite> favorites) {
        this.favorites = favorites;
    }

    @Override
    public String toString() {
        return "AppUser {" +
                "id=" +
                ", username=" + userName + ", " + password + ", }";
    }
}
