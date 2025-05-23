package org.myNeuroV2;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class StorageNN {

    String fileName = "nn.json";
    public StorageNN(){}

    public StorageNN(String fileName)
    {
        this.fileName = fileName;
    }
    public void save(NeuralNetwork nn) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(fileName), nn);
    }

    public NeuralNetwork restore() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // JSON-файл → объект
        return mapper.readValue(new File(fileName), NeuralNetwork.class);

    }

}
