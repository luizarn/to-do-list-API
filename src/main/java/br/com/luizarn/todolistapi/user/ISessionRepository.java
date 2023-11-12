package br.com.luizarn.todolistapi.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISessionRepository extends JpaRepository<SessionModel, Integer> {
        List<SessionModel> findFirstByUser_Id(int userId);

        List<SessionModel> findFirstByToken(String token);
}
