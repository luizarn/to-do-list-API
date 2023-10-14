package br.com.luizarn.todolistapi.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {
    @Id
    @GeneratedValue
    private UUID id;
    private String description;
    @Column(length = 50)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private UUID idUser;

    // vou lançar o erro para a próxima camada com o throws Exception
    // porque toda vez que uma exception é lançada preciso colocar uma forma de
    // tratar ela, se não dá erro no código
    public void setTitle(String title) throws Exception {
        if (title.length() > 50) {
            // exception são erros que passamos ao usuário, já acontece normalmente, mas
            // aqui customizado
            // essa 'Exception' especificamente é do tipo tratável(gerada pelo usuário) e
            // mais comum
            throw new Exception("O campo title deve conter no máximo 50 caracteres");
        }
      //aqui faz de fato o setTitle
        this.title = title;
    }
}
