package com.example.que7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextStreet, editTextEmail, editTextCountry, editTextID;
    private TextView textViewName, textViewPhone, textViewStreet, textViewEmail, textViewCountry;
    private Button firstButton, saveButton, lastButton, previousButton, byIdButton, nextButton;

    // Define a shared preferences file name
    private static final String PREFERENCE_NAME = "MyPrefs";
    private static final String DATA_KEY_PREFIX = "data_";
    private int currentDataIndex = 0;
    private int maxDataIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextStreet = findViewById(R.id.editTextStreet);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextCountry = findViewById(R.id.editTextCountry);
        editTextID = findViewById(R.id.editTextID);

        textViewName = findViewById(R.id.textViewName);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewStreet = findViewById(R.id.textViewStreet);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewCountry = findViewById(R.id.textViewCountry);

        firstButton = findViewById(R.id.firstButton);
        saveButton = findViewById(R.id.saveButton);
        lastButton = findViewById(R.id.lastButton);
        previousButton = findViewById(R.id.previousButton);
        byIdButton = findViewById(R.id.byIdButton);
        nextButton = findViewById(R.id.nextButton);

        // Initialize shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);

        // Load data from shared preferences
        loadAllData(sharedPreferences);

        // Button functionalities
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save data to shared preferences
                saveData(sharedPreferences, currentDataIndex);
            }
        });

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxDataIndex >= 0) {
                    currentDataIndex = 0;
                    showData(currentDataIndex);
                }
            }
        });

        lastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxDataIndex >= 0) {
                    currentDataIndex = maxDataIndex;
                    showData(currentDataIndex);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDataIndex > 0) {
                    currentDataIndex--;
                    showData(currentDataIndex);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDataIndex < maxDataIndex) {
                    currentDataIndex++;
                    showData(currentDataIndex);
                }
            }
        });

        // Implement "ByID" button functionality
        byIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the desired ID from the editTextID field
                String desiredID = editTextID.getText().toString();

                // Find the index associated with the desired ID
                int index = findIndexByDesiredID(sharedPreferences, desiredID);

                if (index >= 0) {
                    currentDataIndex = index;
                    showData(currentDataIndex);
                } else {
                    // Handle the case where the desired ID is not found
                    Toast.makeText(MainActivity.this, "ID not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadAllData(SharedPreferences sharedPreferences) {
        // Load data from shared preferences
        maxDataIndex = sharedPreferences.getInt("maxDataIndex", -1);
        for (int i = 0; i <= maxDataIndex; i++) {
            showData(i);
        }
    }

    private void saveData(SharedPreferences sharedPreferences, int index) {
        // Generate a unique ID
        int uniqueID = generateUniqueID(sharedPreferences);

        // Save data to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DATA_KEY_PREFIX + "id_" + index, String.valueOf(uniqueID)); // Save the ID
        editor.putString(DATA_KEY_PREFIX + "name_" + index, editTextName.getText().toString());
        editor.putString(DATA_KEY_PREFIX + "phone_" + index, editTextPhone.getText().toString());
        editor.putString(DATA_KEY_PREFIX + "street_" + index, editTextStreet.getText().toString());
        editor.putString(DATA_KEY_PREFIX + "email_" + index, editTextEmail.getText().toString());
        editor.putString(DATA_KEY_PREFIX + "country_" + index, editTextCountry.getText().toString());
        editor.putInt("maxDataIndex", index);
        editor.apply();

        // Display a toast message to indicate that the data has been saved
        Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show();
    }

    private void showData(int index) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        editTextName.setText(sharedPreferences.getString(DATA_KEY_PREFIX + "name_" + index, ""));
        editTextPhone.setText(sharedPreferences.getString(DATA_KEY_PREFIX + "phone_" + index, ""));
        editTextStreet.setText(sharedPreferences.getString(DATA_KEY_PREFIX + "street_" + index, ""));
        editTextEmail.setText(sharedPreferences.getString(DATA_KEY_PREFIX + "email_" + index, ""));
        editTextCountry.setText(sharedPreferences.getString(DATA_KEY_PREFIX + "country_" + index, ""));
        editTextID.setText(sharedPreferences.getString(DATA_KEY_PREFIX + "id_" + index, ""));
    }

    private int findIndexByDesiredID(SharedPreferences sharedPreferences, String desiredID) {
        int maxIndex = sharedPreferences.getInt("maxDataIndex", -1);

        for (int i = 0; i <= maxIndex; i++) {
            String savedID = sharedPreferences.getString(DATA_KEY_PREFIX + "id_" + i, "");
            if (savedID.equals(desiredID)) {
                return i;
            }
        }

        return -1; // ID not found
    }

    private int generateUniqueID(SharedPreferences sharedPreferences) {
        // Get the current maximum index
        int maxIndex = sharedPreferences.getInt("maxDataIndex", -1);

        // Generate a unique ID by incrementing the maximum index
        return maxIndex + 1;
    }
}
