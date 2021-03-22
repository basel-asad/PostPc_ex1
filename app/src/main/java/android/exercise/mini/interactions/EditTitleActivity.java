package android.exercise.mini.interactions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditTitleActivity extends AppCompatActivity {

  // TODO:
  //  you can add fields to this class. those fields will be accessibly inside any method
  //  (like `onCreate()` and `onBackPressed()` methods)
  // for any field, make sure to set it's initial value. You CAN'T write a custom constructor
  // for example, you can add this field:
  // `private boolean isEditing = false;`
  // in onCreate() set `this.isEditing` to `true` once the user starts editing, set to `false` once done editing
  // in onBackPressed() check `if(this.isEditing)` to understand what to do

  private boolean animation_in_progress=false; // this boolean is enabled when an animation is in progress
  private Long shortFadeTime = (long) 1000;
  private boolean isEditing = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_title);

    // find all views
    FloatingActionButton fabStartEdit = findViewById(R.id.fab_start_edit);
    FloatingActionButton fabEditDone = findViewById(R.id.fab_edit_done);
    TextView textViewTitle = findViewById(R.id.textViewPageTitle);
    EditText editTextTitle = findViewById(R.id.editTextPageTitle);

    View backgroundBottomArea = findViewById(R.id.backgroundBottomArea);
    View backgroundTopArea = findViewById(R.id.backgroundTopArea);

    // setup - start from static title with "edit" button
    fabStartEdit.setVisibility(View.VISIBLE);
    fabEditDone.setVisibility(View.GONE);
    textViewTitle.setText("Page title here");
    textViewTitle.setVisibility(View.VISIBLE);
    editTextTitle.setText("Page title here");
    editTextTitle.setVisibility(View.GONE);

    // handle clicks on "start edit"
    fabStartEdit.setOnClickListener(v -> {
      start_editing(fabStartEdit,fabEditDone, textViewTitle, editTextTitle);
    });

    // handle clicks on "done edit"
    fabEditDone.setOnClickListener(v -> {
      stop_editing(fabStartEdit, fabEditDone, textViewTitle, editTextTitle);
    });


    // extras

    // also handle clicking the textview (does the same thing as clicking the pen
    textViewTitle.setOnClickListener(v -> {
      start_editing(fabStartEdit,fabEditDone, textViewTitle, editTextTitle);
    });
    // clicking top area focuses on editTextTitle and shows keyboard
    backgroundTopArea.setOnClickListener(v -> {
      // make sure that the keyboard is oppened
      showSoftKeyboard(editTextTitle);
    });
    // clicking on bottom area closes keyboard if shown
    backgroundBottomArea.setOnClickListener(v -> {
      // make sure that the keyboard is closed
      closeKeyboard(textViewTitle);
    });
  }

  private void start_editing(FloatingActionButton fabStartEdit,FloatingActionButton fabEditDone,
                             TextView textViewTitle, TextView editTextTitle) {
    if(! animation_in_progress) {
      this.isEditing=true;
      // 0. enable animation in progress
      animation_in_progress=true;
      // 1. animate out the "start edit" FAB
      fabStartEdit.animate()
              .alpha(0f)
              .setDuration(shortFadeTime)
              .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                  fabStartEdit.setVisibility(View.GONE);
                  animation_in_progress=false;
                }
              });
      // 2. animate in the "done edit" FAB
      fabEditDone.setAlpha(0f); // make invisible
      fabEditDone.setVisibility(View.VISIBLE); // make button active
      fabEditDone.animate().alpha(1f).setDuration(shortFadeTime).setListener(null);
      // 3. hide the static title (text-view)
      textViewTitle.setVisibility(View.GONE);
      // 4. show the editable title (edit-text)
      editTextTitle.setVisibility(View.VISIBLE);
      // 5. make sure the editable title's text is the same as the static one
      editTextTitle.setText(textViewTitle.getText());
      // 6. optional (HARD!) make the keyboard to open with the edit-text focused,
      //          so the user can start typing without the need another click on the edit-text
      showSoftKeyboard(editTextTitle);
    }
  }


  private void stop_editing(FloatingActionButton fabStartEdit, FloatingActionButton fabEditDone, TextView textViewTitle, EditText editTextTitle) {
    if(!animation_in_progress) {
      // 0. set animation_in_progress to true
      animation_in_progress=true;

      // 1. animate out the "done edit" FAB
      fabStartEdit.setAlpha(0f); // make invisible
      fabStartEdit.setVisibility(View.VISIBLE); // make button active
      fabStartEdit.animate().alpha(1f).setDuration(shortFadeTime).setListener(null);

      // 2. animate in the "start edit" FAB
      fabEditDone.animate()
              .alpha(0f)
              .setDuration(shortFadeTime)
              .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                  fabEditDone.setVisibility(View.GONE);
                  animation_in_progress=false;
                }
              });

      // 3. take the text from the user's input in the edit-text and put it inside the static text-view
      textViewTitle.setText(editTextTitle.getText());

      // 4. show the static title (text-view)
      textViewTitle.setVisibility(View.VISIBLE);

      // 5. hide the editable title (edit-text)
      editTextTitle.setVisibility(View.GONE);

      // 6. make sure that the keyboard is closed
      closeKeyboard(textViewTitle);
      // stop editing
      isEditing=false;

    }
  }

  public void closeKeyboard(View view){
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(),
            InputMethodManager.RESULT_UNCHANGED_SHOWN);
  }

  public void showSoftKeyboard(View view) {
    if(view.requestFocus()){
      InputMethodManager imm =(InputMethodManager)
              getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
    }
  }


  @Override
  public void onBackPressed() {
    // BACK button was clicked
    if(this.isEditing){
      this.isEditing = false;
      FloatingActionButton fabStartEdit = findViewById(R.id.fab_start_edit);
      FloatingActionButton fabEditDone = findViewById(R.id.fab_edit_done);
      TextView textViewTitle = findViewById(R.id.textViewPageTitle);
      EditText editTextTitle = findViewById(R.id.editTextPageTitle);

//      // close keyboard
//      closeKeyboard(editTextTitle);


      // 1. hide the edit-text

      editTextTitle.setVisibility(View.GONE);
      // 2. show the static text-view with previous text (discard user's input)

      textViewTitle.setVisibility(View.VISIBLE);

      //    3. animate out the "done-edit" FAB
      fabStartEdit.setAlpha(0f); // make invisible
      fabStartEdit.setVisibility(View.VISIBLE); // make button active
      fabStartEdit.animate().alpha(1f).setDuration(shortFadeTime).setListener(null);

      //    4. animate in the "start-edit" FAB
      fabEditDone.animate()
              .alpha(0f)
              .setDuration(shortFadeTime)
              .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                  fabEditDone.setVisibility(View.GONE);
                  animation_in_progress=false;
                }
              });
    }
    else{
      //else, the user isn't editing. continue normal BACK tap behavior to exit the screen.
      //    call `super.onBackPressed()`
      super.onBackPressed();
    }

  }
}