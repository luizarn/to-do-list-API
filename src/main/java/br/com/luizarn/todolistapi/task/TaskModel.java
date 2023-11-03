package br.com.luizarn.todolistapi.task;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.mapping.List;

import br.com.luizarn.todolistapi.user.UserModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class TaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50)
    private String title;


    private String description;

    private String status;

    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserModel user;

    public void setTitle(String title) throws Exception {
        if (title.length() > 50) {
            throw new Exception("The 'title' field must contain a maximum of 50 characters");
        }
        this.title = title;
    }
}
