package org.apache.kafka.avro;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

public class AvroSchema {
  // example schema string
  public static String schema = "{" +
                                  "\"schema\":" +
                                      "\"{" +
                                          "\"type\": \"record\"," +
                                          "\"name\": \"test\"," +
                                          "\"fields\":" +
                                              "[" +
                                                "{" +
                                                  "\"type\": \"string\"," +
                                                  "\"name\": \"field1\"" +
                                                "}" +
                                              "]" +
                                      "}\"" +
                                  "}";

  public static String getAvroSchema() {
    ObjectMapper mapper = new ObjectMapper();

    // type object node
    ObjectNode typeObjectNode = mapper.createObjectNode();
    typeObjectNode.put("type", "record");

    // name object node
    ObjectNode nameObjectNode = mapper.createObjectNode();
    nameObjectNode.put("name", "confluent_kafka");

    // preparation for field object node
    ObjectNode typeFieldName1 = mapper.createObjectNode();
    typeFieldName1.put("type", "int");
    ObjectNode nameFieldName1 = mapper.createObjectNode();
    nameFieldName1.put("name", "key");
    ObjectNode field1 = mapper.createObjectNode();
    field1.putAll(typeFieldName1);
    field1.putAll(nameFieldName1);

    ObjectNode typeFieldName2 = mapper.createObjectNode();
    typeFieldName2.put("type", "string");
    ObjectNode nameFieldName2 = mapper.createObjectNode();
    nameFieldName2.put("name", "value");
    ObjectNode field2 = mapper.createObjectNode();
    field2.putAll(typeFieldName2);
    field2.putAll(nameFieldName2);

    // field object node
    ArrayNode fieldObjectNode = mapper.createArrayNode();
    fieldObjectNode.add(field1);
    fieldObjectNode.add(field2);

    // schema node value preparation
    ObjectNode avroSchemaNode = mapper.createObjectNode();
    avroSchemaNode.putAll(typeObjectNode);
    avroSchemaNode.putAll(nameObjectNode);
    avroSchemaNode.put("fields", fieldObjectNode);

    // schema node
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("schema", avroSchemaNode.toString());

    // save & access only Avro schema later
    schema = avroSchemaNode.toString();

    return mapper.createObjectNode().putAll(objectNode).toString();
  }

  public static Schema getSchema() {
    return SchemaBuilder.builder()
      .record("confluent_kafka")
      .namespace("org.apache.avro")
      .fields()
      .name("key").type().intType().noDefault()
      .name("value").type().stringType().noDefault()
      .endRecord();
  }
}