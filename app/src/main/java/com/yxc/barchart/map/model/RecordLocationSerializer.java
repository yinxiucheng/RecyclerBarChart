package com.yxc.barchart.map.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * @author yxc
 * @date 2019-06-14
 */
public class RecordLocationSerializer implements JsonSerializer<RecordLocation> {
    @Override
    public JsonElement serialize(RecordLocation src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("timestamp", src.getTimestamp());
        jsonObject.addProperty("endTime", src.getEndTime());
        jsonObject.addProperty("duration", src.getDuration());
        jsonObject.addProperty("longitude", src.getLongitude());
        jsonObject.addProperty("latitude", src.getLatitude());
        jsonObject.addProperty("speed", src.getSpeed());
        jsonObject.addProperty("itemDistance", src.getItemDistance());
        jsonObject.addProperty("distance", src.getDistance());
        jsonObject.addProperty("recordId", src.getRecordId());
        jsonObject.addProperty("recordType", src.getRecordType());
        jsonObject.addProperty("locationStr", src.getLocationStr());
        jsonObject.addProperty("milePost", src.getMilePost());
        return jsonObject;
    }
}
