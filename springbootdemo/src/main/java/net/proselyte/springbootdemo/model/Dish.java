package net.proselyte.springbootdemo.model;
import javax.persistence.*;
import lombok.*;
/**
 * Класс Dish - это модель виртуальной таблицы, которая хранит информацию об id, имени и цене блюд.
 * @Data автоматически генерирует методы геттеров, сеттеров и прочее для всех полей класса;
 * @Entity указывает, что класс является сущностью базы данных;
 * @Table помечает класс в качестве сущности, которая соответствовует таблице dish в базе данных;
 * @Id обозначает уникальный идентификатор;
 * @GeneratedValue автоматически генерирует ключ;
 * @Column определяет соответсвие для колонки в таблице dish в базе данных.
 */
@Data
@Entity
@Table(name = "dish")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name = "price")
    private Integer price;

}
