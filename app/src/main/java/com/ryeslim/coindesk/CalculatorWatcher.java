package com.ryeslim.coindesk;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CalculatorWatcher {

    private EditText currencyField[];

    public CalculatorWatcher(EditText[] currencyField) {
        this.currencyField = currencyField;
        calculatorWatcher(currencyField);
    }

    public EditText[] getCurrencyField() {
        return currencyField;
    }

    private void calculatorWatcher(EditText[] currencyField) {

        for (int ii = 0; ii < 2; ii++) {
            final int i = ii;
            final int j = (i + 1) % 2;
            currencyField[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (getCurrencyField()[i].getText().toString().length() > 0) {
                        getCurrencyField()[j].setFocusable(false);
                    } else {
                        for (int i = 0; i < 2; i++) {
                            getCurrencyField()[i].setFocusableInTouchMode(true);
                            getCurrencyField()[i].setFocusable(true);
                        }
                    }
                }
            });
        }
    }
}
