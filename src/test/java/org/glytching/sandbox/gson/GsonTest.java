package org.glytching.sandbox.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GsonTest {

    @Test
    public void twoWayTransform() {
        Gson gson = new GsonBuilder().serializeNulls().create();

        List<GsonSUT> incomings = Arrays.asList(new GsonSUT(), new GsonSUT());

        String json = gson.toJson(incomings);

        // use TypeToken to inform Gson about the type of the elements in the generic list
        List<GsonSUT> fromJson = gson.fromJson(json, new TypeToken<ArrayList<GsonSUT>>() {
        }.getType());

        assertEquals(2, fromJson.size());
        for (GsonSUT incoming : incomings) {
            // this will pass if GsonSUT has an equals() method
            assertTrue(fromJson.contains(incoming));
        }
    }
}