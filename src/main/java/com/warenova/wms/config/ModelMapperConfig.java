package com.warenova.wms.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// ================================================
// MODEL MAPPER CONFIG
// ================================================
// PURPOSE:
// Configures ModelMapper bean for the application
//
// WHAT IS MODEL MAPPER?
// Automatically converts between objects
// No need to manually map field by field
//
// EXAMPLE WITHOUT ModelMapper: ❌
// ItemResponse response = new ItemResponse();
// response.setSku(item.getSku());
// response.setItemName(item.getItemName());
// response.setCategory(item.getCategory());
// response.setWeight(item.getWeight());
// ... 10 more lines ...
//
// EXAMPLE WITH ModelMapper: ✅
// ItemResponse response =
//   modelMapper.map(item, ItemResponse.class);
// ← ONE LINE! All fields copied automatically
//
// USAGE IN SERVICE:
// @Autowired ModelMapper modelMapper;
// ItemResponse dto = modelMapper.map(
//   item, ItemResponse.class);
// ================================================

@Configuration
public class ModelMapperConfig {

    // ================================================
    // MODEL MAPPER BEAN
    // ================================================
    // Creates singleton ModelMapper instance
    // Shared across entire application
    // ================================================
    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        // ================================================
        // STRICT MATCHING STRATEGY
        // ================================================
        // STRICT = only map fields with
        // exactly matching names
        //
        // Prevents accidental wrong mappings
        // when field names are similar but different
        //
        // Example:
        // Item.id  →  ItemResponse.id  ✅ maps
        // Item.sku →  ItemResponse.skuCode  ❌ skip
        // ================================================
        modelMapper.getConfiguration()
                .setMatchingStrategy(
                        MatchingStrategies.STRICT
                );

        // ================================================
        // SKIP NULL VALUES
        // ================================================
        // If source field is null →
        // don't overwrite destination field
        // Useful for partial updates (PUT/PATCH)
        // ================================================
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true);

        return modelMapper;
    }
}
