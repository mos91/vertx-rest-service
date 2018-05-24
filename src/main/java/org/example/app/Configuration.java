package org.example.app;

import com.google.common.io.Resources;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigResolveOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created on 09.05.2018.
 */
public class Configuration {

  private int port;

  private String topicName;

  private Properties kafkaProducerConfig;

  public Configuration() throws IOException {
    Config appConfig = ConfigFactory.load();

    try (InputStream is = Resources.getResource("kafka-producer.properties").openStream()) {
      kafkaProducerConfig = new Properties();
      kafkaProducerConfig.load(is);
    }

    port = appConfig.getInt("app.http.port");
    topicName = appConfig.getString("app.kafka.topicName");
  }

  public int getPort() {
    return port;
  }

  public String getTopicName() {
    return topicName;
  }

  public Properties getKafkaProducerConfig() {
    return kafkaProducerConfig;
  }
}
