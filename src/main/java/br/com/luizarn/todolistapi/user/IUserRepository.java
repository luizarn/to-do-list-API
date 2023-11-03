package br.com.luizarn.todolistapi.user;

import org.springframework.data.jpa.repository.JpaRepository;


public interface IUserRepository extends JpaRepository<UserModel, Integer>{
    UserModel findByemail(String email);
}
