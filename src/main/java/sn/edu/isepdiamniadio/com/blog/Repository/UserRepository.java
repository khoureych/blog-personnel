package sn.edu.isepdiamniadio.com.blog.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sn.edu.isepdiamniadio.com.blog.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

