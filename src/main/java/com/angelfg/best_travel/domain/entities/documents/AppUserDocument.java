package com.angelfg.best_travel.domain.entities.documents;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "app_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserDocument implements Serializable {

    @Id
    private String id;

    private String dni;
    private boolean enabled;
    private String password;
    private Role role;

}
