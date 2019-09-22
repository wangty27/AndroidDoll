package wang.tianyu.ragdoll;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mainView;
    private AlertDialog aboutPopup;
    private enum Model {
        DOLL,
        DOG
    }
    private Model currentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mainView = findViewById(R.id.main_layout);
        this.currentModel = Model.DOLL;

        // Create about
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("App Name: Ragdoll");
        builder.setMessage("Name: Tianyu Wang\nStudent Number: 20648688");
        builder.setPositiveButton("OK", null);
        this.aboutPopup = builder.create();

        Button aboutBtn = findViewById(R.id.btn_about);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ButtonClick", "About");
                MainActivity.this.aboutPopup.show();
            }
        });

        Button resetBtn = findViewById(R.id.btn_reset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ButtonClick", "Reset");
                if (MainActivity.this.currentModel == Model.DOLL) {
                    DollView v = findViewById(R.id.doll);
                    v.resetView();
                } else if (MainActivity.this.currentModel == Model.DOG) {
                    DogView v = findViewById(R.id.dog);
                    v.resetView();
                }

            }
        });

        Button modelBtn = findViewById(R.id.btn_models);
        modelBtn.setText("Change to Dog");
        modelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.this.currentModel == Model.DOLL) {
                    Log.d("ButtonClick", "Change to Dog");
                    DollView vDoll = findViewById(R.id.doll);
                    vDoll.resetView();
                    vDoll.setVisibility(View.GONE);
                    DogView vDog = findViewById(R.id.dog);
                    vDog.resetView();
                    vDog.setVisibility(View.VISIBLE);
                    Button modelBtn = findViewById(R.id.btn_models);
                    modelBtn.setText("Change to Doll");
                    MainActivity.this.currentModel = Model.DOG;
                } else if (MainActivity.this.currentModel == Model.DOG) {
                    Log.d("ButtonCLick", "Change to Doll");
                    DollView vDoll = findViewById(R.id.doll);
                    vDoll.resetView();
                    vDoll.setVisibility(View.VISIBLE);
                    DogView vDog = findViewById(R.id.dog);
                    vDog.resetView();
                    vDog.setVisibility(View.GONE);
                    Button modelBtn = findViewById(R.id.btn_models);
                    modelBtn.setText("Change to Dog");
                    MainActivity.this.currentModel = Model.DOLL;
                }
            }
        });
    }
}
