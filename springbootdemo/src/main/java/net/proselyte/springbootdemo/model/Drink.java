package net.proselyte.springbootdemo.model;
import javax.persistence.*;
import lombok.Data;
/**
 * Класс Drink - это модель виртуальной таблицы, которая хранит информацию об id, имени и цене напитков.
 * @Data автоматически генерирует методы геттеров, сеттеров и прочее для всех полей класса;
 * @Entity указывает, что класс является сущностью базы данных;
 * @Table помечает класс в качестве сущности, которая соответствовует таблице drink в базе данных;
 * @Id обозначает уникальный идентификатор;
 * @GeneratedValue автоматически генерирует ключ;
 * @Column определяет соответсвие для колонки в таблице drink в базе данных.
 */
@Data
@Entity
@Table(name = "drink")
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name = "price")
    private Integer price;

}
