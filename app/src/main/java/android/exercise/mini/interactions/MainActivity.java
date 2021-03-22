package android.exercise.mini.interactions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.buttonOpenEditScreen).setOnClickListener(
      v -> startActivity(new Intent(MainActivity.this, EditTitleActivity.class))
    );
  }
}