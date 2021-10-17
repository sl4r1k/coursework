package com.ebuy.userservice.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

public class EntityPatcher {
    @SuppressWarnings("unchecked")
    public <T> T patch(JsonPatch patch, T entity) throws JsonPatchException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.convertValue(entity, JsonNode.class);
        return (T) mapper.treeToValue(
                patch.apply(node),
                entity.getClass()
        );
    }
}
