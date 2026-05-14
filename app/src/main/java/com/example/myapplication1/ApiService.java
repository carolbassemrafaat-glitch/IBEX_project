package com.example.myapplication1;
import com.example.myapplication1.University;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("search")
    Call<List<University>> getUniversitiesByCountry(@Query("country") String country);

}
