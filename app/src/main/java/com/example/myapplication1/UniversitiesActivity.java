package com.example.myapplication1;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.UniversityAdapter;
import com.example.myapplication1.ApiService;
import com.example.myapplication1.RetrofitClient;
import com.example.myapplication1.University;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UniversitiesActivity extends AppCompatActivity {

    private EditText editCountry;
    private Button btnSearch;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private UniversityAdapter adapter;
    private List<University> universityList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universities);

        editCountry = findViewById(R.id.editCountry);
        btnSearch = findViewById(R.id.btnSearch);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UniversityAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        btnSearch.setOnClickListener(v -> {
            String country = editCountry.getText().toString().trim();
            if (country.isEmpty()) {
                Toast.makeText(UniversitiesActivity.this, "Please enter a country", Toast.LENGTH_SHORT).show();
                return;
            }
            hideKeyboard();
            loadUniversitiesByCountry(country);
        });
    }

    private void loadUniversitiesByCountry(String country) {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<University>> call = apiService.getUniversitiesByCountry(country);

        call.enqueue(new Callback<List<University>>() {
            @Override
            public void onResponse(@NonNull Call<List<University>> call,
                                   @NonNull Response<List<University>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<University> list = response.body();
                    adapter.updateList(list);
                    if (list.isEmpty()) {
                        Toast.makeText(UniversitiesActivity.this, "No universities found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UniversitiesActivity.this, "Loaded " + list.size() + " universities", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UniversitiesActivity.this, "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<University>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UniversitiesActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null && getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception ignored) {}
    }
}
