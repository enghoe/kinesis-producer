package com.enghoe;

import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by user on 6/25/2017.
 */
public class KinesisProducer {

    static final String endpoint = "kinesis.us-east-1.amazonaws.com";
    static final String serviceName = "kinesis";
    static final String streamName = "game_rating_stream";

    private AmazonKinesisClient client;

    public void initClient() {

        String regionId = "";
        client = new AmazonKinesisClient();
        client.setEndpoint(endpoint); // (endpoint, serviceName, regionId);
    }

    public List<String> listStreams() {
        ListStreamsRequest listStreamsRequest = new ListStreamsRequest();
        listStreamsRequest.setLimit(20);
        ListStreamsResult listStreamsResult = client.listStreams(listStreamsRequest);
        List<String> streamNames = listStreamsResult.getStreamNames();
        while (listStreamsResult.getHasMoreStreams()) {
            if (streamNames.size() > 0) {
                listStreamsRequest.setExclusiveStartStreamName(streamNames.get(streamNames.size() - 1));
            }
            listStreamsResult = client.listStreams(listStreamsRequest);
            streamNames.addAll(listStreamsResult.getStreamNames());
        }

        return streamNames;
    }

    public void putRecords() throws JsonProcessingException {
        PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
        putRecordsRequest.setStreamName(streamName);
        List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<>();

        Random random = new Random(2377917);

        for (int i = 0; i < 100; i++) {
            PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
            GameRating gameRating = new GameRating();
            gameRating.deviceId = random.nextInt(10) + 1;
            gameRating.rating = random.nextInt(2);
            gameRating.gameTitle = "game" + (random.nextInt(4) + 1);
            //gameRating.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            ObjectMapper mapper = new ObjectMapper();
            putRecordsRequestEntry.setData(ByteBuffer.wrap(mapper.writeValueAsString(gameRating).getBytes()));
            putRecordsRequestEntry.setPartitionKey(String.format("partitionKey-%d", i));
            putRecordsRequestEntryList.add(putRecordsRequestEntry);
        }

        putRecordsRequest.setRecords(putRecordsRequestEntryList);
        PutRecordsResult putRecordsResult = client.putRecords(putRecordsRequest);
        System.out.println("Put Result" + putRecordsResult);
    }

    public static void main(String[] args) {
        KinesisProducer producer = new KinesisProducer();
        producer.initClient();

        try {
            producer.putRecords();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //producer.listStreams().forEach(System.out::println);
    }
}