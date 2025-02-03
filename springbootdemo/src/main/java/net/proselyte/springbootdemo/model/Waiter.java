package net.proselyte.springbootdemo.model;
import javax.persistence.*;
import lombok.Data;
/**
 * Класс Waiter - это модель виртуальной таблицы, которая хранит информацию об id, имени и фамилии официантов.
 * @Data автоматически генерирует методы геттеров, сеттеров и прочее для всех полей класса;
 * @Entity указывает, что класс является сущностью базы данных;
 * @Table помечает класс в качестве сущности, которая соответствовует таблице waiter в базе данных;
 * @Id обозначает уникальный идентификатор;
 * @GeneratedValue автоматически генерирует ключ;
 * @Column определяет соответсвие для колонки в таблице waiter в базе данных.
 */
@Data
@Entity
@Table(name = "waiter")
public class Waiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;

}
