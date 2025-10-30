package kz.test.lmssystem.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "lessons")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "order_number")
    private int order;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @Column(name = "created_Time")
    private LocalDateTime createdTime;

    @Column(name = "updated_Time")
    private LocalDateTime updatedTime;

    @PrePersist
    public void createdTime(){
        createdTime = LocalDateTime.now();
    }

    @PreUpdate
    public void updatedTime(){
        updatedTime = LocalDateTime.now();
    }
}
