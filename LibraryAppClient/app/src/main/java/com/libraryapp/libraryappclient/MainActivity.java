package com.libraryapp.libraryappclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult =findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("base URI of server here") // *** NEED TO UPDATE ONCE SERVER IS HOSTED ***
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LibraryApi libraryApi = retrofit.create(LibraryApi.class);

        Call<List<Book>> call = libraryApi.getBooks();

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(!response.isSuccessful()) {
                    textViewResult.setText("Code: "+response.code());
                    return;
                }

                List<Book> books = response.body();

                for(Book book : books) {
                    String content = "";
                    content += "ISBN: " + book.getIsbn() + "\n";
                    content += "Title: " + book.getTitle() + "\n";
                    content += "Author: " + book.getAuthor() + "\n";
                    content += "Genre: " + book.getGenre() + "\n";
                    content += "Rating: " + book.getRating() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}