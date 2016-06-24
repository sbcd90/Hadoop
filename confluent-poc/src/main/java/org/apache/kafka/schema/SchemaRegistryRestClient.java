package org.apache.kafka.schema;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

public class SchemaRegistryRestClient {
  private static final String schemaRegistryUrl = "http://10.9.43.125:8081";
  private final Client client = ClientBuilder.newClient();

  public final String KEY_SERIALIZATION_FLAG = "key";
  public final String VALUE_SERIALIZATION_FLAG = "value";

  public List<String> getSchemaRegistries() {
    String resource = "/subjects";

    WebTarget target = client.target(schemaRegistryUrl + resource);
    Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);

    Response response = builder.get();
    if (response.getStatus() == 200) {
      System.out.println(response.readEntity(String.class));
    }
    return null;
  }

  public String getSchemaRegistry(String topic, SerializationOption flag) throws IOException {
    String resource = "/subjects/";
    if (flag == SerializationOption.KEY) {
      resource += topic + "-" + KEY_SERIALIZATION_FLAG + "/versions/latest";
    } else {
      resource += topic + "-" + VALUE_SERIALIZATION_FLAG + "/versions/latest";
    }

    WebTarget target = client.target(schemaRegistryUrl + resource);
    Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);

    Response response = builder.get();
    if (response.getStatus() == 200) {
      ObjectMapper mapper = new ObjectMapper();
      String responseJson = mapper.readTree(response.readEntity(String.class)).get("schema").asText();
      return responseJson;
    } else {
      throw new RuntimeException("Schema not registered");
    }
  }

  private boolean postSchemaRegistry(String topic, SerializationOption flag, String avroSchema) {
    String resource = "/subjects/";
    if (flag == SerializationOption.KEY) {
      resource += topic + "-" + KEY_SERIALIZATION_FLAG + "/versions";
    }  else {
      resource += topic + "-" + VALUE_SERIALIZATION_FLAG + "/versions";
    }

    WebTarget target = client.target(schemaRegistryUrl + resource);
    Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);

    Response response = builder.post(Entity.entity(avroSchema, MediaType.APPLICATION_JSON_TYPE));
    if (response.getStatus() == 200) {
      System.out.println(response.readEntity(String.class));
      return true;
    } else {
      throw new RuntimeException(response.readEntity(String.class));
    }
  }

  private boolean checkSchemaExistence(String topic, SerializationOption flag, String avroSchema) {
    String resource = "/subjects/";
    if (flag == SerializationOption.KEY) {
      resource += topic + "-" + KEY_SERIALIZATION_FLAG;
    } else {
      resource += topic + "-" + VALUE_SERIALIZATION_FLAG;
    }

    WebTarget target = client.target(schemaRegistryUrl + resource);
    Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);

    Response response = builder.post(Entity.entity(avroSchema, MediaType.APPLICATION_JSON_TYPE));
    if (response.getStatus() == 200) {
      System.out.println(response.readEntity(String.class));
      return true;
    } else if (response.getStatus() == 404) {
      System.out.println("Schema not found");
      return false;
    } else {
      throw new RuntimeException(response.readEntity(String.class));
    }
  }

  public void doSchemaRegistry(String topic, SerializationOption flag, String avroSchema) {
    if (!checkSchemaExistence(topic, flag, avroSchema)) {
      postSchemaRegistry(topic, flag, avroSchema);
    }
  }
}