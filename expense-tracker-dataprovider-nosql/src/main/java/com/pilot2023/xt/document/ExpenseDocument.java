package com.pilot2023.xt.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Document(collection = "expense")
public class ExpenseDocument extends AbstractDocument {

    @Indexed(unique = true)
    private String id;

    private String category;

    private String description;

    private Float cost;

}