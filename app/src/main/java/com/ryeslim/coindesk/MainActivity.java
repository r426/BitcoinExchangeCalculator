package com.ryeslim.coindesk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    final int MAX_FRACTION_DIGITS = 4;
    final int NUMBER_OF_CURRENCIES = 3;

    static long lastQuery;
    static long currentQuery;
    final long ONE_MINUTE = 60 * 1000;
    NumberFormat number;

    Button calculate[] = new Button[NUMBER_OF_CURRENCIES];

    private static MainActivity instance = null;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataProcessing.getInstance().setContext(this);
        MainActivity.instance = this;

        final EditText currencyField[][] =
                {{findViewById(R.id.amount_of_currency1), findViewById(R.id.amount_of_bitcoins1)},
                        {findViewById(R.id.amount_of_currency2), findViewById(R.id.amount_of_bitcoins2)},
                        {findViewById(R.id.amount_of_currency3), findViewById(R.id.amount_of_bitcoins3)}};

        calculate[0] = findViewById(R.id.calculate1);
        calculate[1] = findViewById(R.id.calculate2);
        calculate[2] = findViewById(R.id.calculate3);

        Button clear[] = {findViewById(R.id.clear1),
                findViewById(R.id.clear2),
                findViewById(R.id.clear3)};

        CalculatorWatcher watcher1 = new CalculatorWatcher(currencyField[0]);
        CalculatorWatcher watcher2 = new CalculatorWatcher(currencyField[1]);
        CalculatorWatcher watcher3 = new CalculatorWatcher(currencyField[2]);

        number = NumberFormat.getInstance(this.getResources().getConfiguration().locale);
        number.setMaximumFractionDigits(MAX_FRACTION_DIGITS);

        if (savedInstanceState == null) {
            lastQuery = System.currentTimeMillis();
            DataProcessing.getInstance().setLoadingQue(Volley.newRequestQueue(this));
        } else {
            DataProcessing.getInstance().readFromFile();
        }

        // Set a click listener for the refresh image
        ImageView refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentQuery = System.currentTimeMillis();
                if (currentQuery - lastQuery > ONE_MINUTE) {
                    lastQuery = currentQuery;
                    DataProcessing.getInstance().setLoadingQue(Volley.newRequestQueue(MainActivity.getInstance()));
                } else {
                    Toast.makeText(getApplicationContext(), getText(R.string.toast_message), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Set a click listener for all three calculate buttons
        for (int ii = 0; ii < 3; ii++) {
            final int i = ii;
            calculate[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    float theRate = DataProcessing.getInstance().getRateFloat()[i];
                    if (currencyField[i][0].getText().toString().length() > 0) {
                        float theValue = Float.valueOf(currencyField[i][0].getText().toString());
                        currencyField[i][1].setText(divide(theValue, theRate));
                    } else if (currencyField[i][1].getText().toString().length() > 0) {
                        float theValue = Float.valueOf(currencyField[i][1].getText().toString());
                        currencyField[i][0].setText(multiply(theValue, theRate));
                    } else {
                    } //both fields empty
                }
            });

            // Set a click listener for all three clear buttons
            clear[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < 2; j++) {
                        currencyField[i][j].setText("");
                    }
                }
            });
        }
    }

    public String divide(float theValue, float theRate) {
        return number.format(theValue / theRate).replaceAll(",", "");
    }

    public String multiply(float theValue, float theRate) {
        return number.format(theValue * theRate).replaceAll(",", "");
    }

    public void goForward() {

        TextView chartName = findViewById(R.id.chart_name);
        chartName.setText(DataProcessing.getInstance().getChartName());

        TextView updatedAt = findViewById(R.id.updated_at);
        updatedAt.setText(DataProcessing.getInstance().getLocalTime());

        TextView currency1 = findViewById(R.id.currency1);
        currency1.setText(DataProcessing.getInstance().getRate1());

        TextView currency2 = findViewById(R.id.currency2);
        currency2.setText(DataProcessing.getInstance().getRate2());

        TextView currency3 = findViewById(R.id.currency3);
        currency3.setText(DataProcessing.getInstance().getRate3());

        //when the user clicks 'refresh',
        // calculator fields that are not empty get updated as well
        for (int i = 0; i < NUMBER_OF_CURRENCIES; i++) {
            calculate[i].callOnClick();
        }
    }
}
