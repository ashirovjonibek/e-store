package uz.e_store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "errors")
public class Error {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String tableName;

    public Error(String tableName, String text) {
        this.tableName = tableName;
        this.text = text;
    }

    @Column(columnDefinition = "text")
    private String text;
}
