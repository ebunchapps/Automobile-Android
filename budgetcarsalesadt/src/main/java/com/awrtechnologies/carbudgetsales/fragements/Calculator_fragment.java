package com.awrtechnologies.carbudgetsales.fragements;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.aaplication.CalculatorBrain;

public class Calculator_fragment extends Fragment implements OnClickListener {

	private TextView textview;
	private Boolean userIsInTheMiddleOfTypingANumber = false;
	private CalculatorBrain mCalculatorBrain;
	private static final String DIGITS = "0123456789.";

	DecimalFormat df = new DecimalFormat("@###########");
	private Button btn0;
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	private Button btn6;
	private Button btn7;
	private Button btn8;
	private Button btn9;
	private Button btn_add;
	private Button btn_sub;
	private Button btn_multiply;
	private Button btn_divide;
	private Button btn_toggle_sign;
	private Button btn_decimal_point;
	private Button btn_equals;
	private Button btn_clear;
	private Button btn_clear_memory;
	private Button btn_add_memory;
	private Button btn_sub_from_memory;
	private Button btn_recall_memory;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.calculator, null);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mCalculatorBrain = new CalculatorBrain();
		// Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
		// "fonts/digital-7.ttf");

		textview = (TextView) v.findViewById(R.id.edittext_number);
		// textview.setTypeface(tf);
		df.setMinimumFractionDigits(0);
		df.setMinimumIntegerDigits(1);
		df.setMaximumIntegerDigits(8);

		btn0 = (Button) v.findViewById(R.id.button0);
		btn1 = (Button) v.findViewById(R.id.button1);
		btn2 = (Button) v.findViewById(R.id.button2);
		btn3 = (Button) v.findViewById(R.id.button3);
		btn4 = (Button) v.findViewById(R.id.button4);
		btn5 = (Button) v.findViewById(R.id.button5);
		btn6 = (Button) v.findViewById(R.id.button6);
		btn7 = (Button) v.findViewById(R.id.button7);
		btn8 = (Button) v.findViewById(R.id.button8);
		btn9 = (Button) v.findViewById(R.id.button9);

		btn_add = (Button) v.findViewById(R.id.buttonAdd);
		btn_sub = (Button) v.findViewById(R.id.buttonSubtract);
		btn_multiply = (Button) v.findViewById(R.id.buttonMultiply);
		btn_divide = (Button) v.findViewById(R.id.buttonDivide);
		btn_toggle_sign = (Button) v.findViewById(R.id.buttonToggleSign);
		btn_decimal_point = (Button) v.findViewById(R.id.buttonDecimalPoint);
		btn_equals = (Button) v.findViewById(R.id.buttonEquals);
		btn_clear = (Button) v.findViewById(R.id.buttonClear);
		btn_clear_memory = (Button) v.findViewById(R.id.buttonClearMemory);
		btn_add_memory = (Button) v.findViewById(R.id.buttonAddToMemory);
		btn_sub_from_memory = (Button) v
				.findViewById(R.id.buttonSubtractFromMemory);
		btn_recall_memory = (Button) v.findViewById(R.id.buttonRecallMemory);

		btn_sub.setOnClickListener(this);
		btn_multiply.setOnClickListener(this);
		btn_divide.setOnClickListener(this);
		btn_toggle_sign.setOnClickListener(this);
		btn_decimal_point.setOnClickListener(this);
		btn_equals.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
		btn_clear_memory.setOnClickListener(this);
		btn_add_memory.setOnClickListener(this);
		btn_sub_from_memory.setOnClickListener(this);
		btn_recall_memory.setOnClickListener(this);
		btn_add.setOnClickListener(this);

		btn0.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn9.setOnClickListener(this);

		return v;

	}

	@Override
	public void onClick(View v) {

		String buttonPressed = ((Button) v).getText().toString();
		if (DIGITS.contains(buttonPressed)) {

			// digit was pressed
			if (userIsInTheMiddleOfTypingANumber) {

				if (buttonPressed.equals(".")
						&& textview.getText().toString().contains(".")) {
					// ERROR PREVENTION
					// Eliminate entering multiple decimals
				} else {
					textview.append(buttonPressed);
				}

			} else {

				if (buttonPressed.equals(".")) {
					// ERROR PREVENTION
					// This will avoid error if only the decimal is hit before
					// an operator, by placing a leading zero
					// before the decimal
					textview.setText(0 + buttonPressed);
				} else {
					textview.setText(buttonPressed);
				}

				userIsInTheMiddleOfTypingANumber = true;
			}

		} else {
			// operation was pressed
			if (userIsInTheMiddleOfTypingANumber) {

				mCalculatorBrain.setOperand(Double.parseDouble(textview
						.getText().toString()));
				userIsInTheMiddleOfTypingANumber = false;
			}

			mCalculatorBrain.performOperation(buttonPressed);
			textview.setText(df.format(mCalculatorBrain.getResult()));

		}

	}

	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	// // Save variables on screen orientation change
	// outState.putDouble("OPERAND", mCalculatorBrain.getResult());
	// outState.putDouble("MEMORY", mCalculatorBrain.getMemory());
	// }
	//
	// @Override
	// protected void onRestoreInstanceState(Bundle savedInstanceState) {
	// super.onRestoreInstanceState(savedInstanceState);
	// // Restore variables on screen orientation change
	// mCalculatorBrain.setOperand(savedInstanceState.getDouble("OPERAND"));
	// mCalculatorBrain.setMemory(savedInstanceState.getDouble("MEMORY"));
	// textview.setText(df.format(mCalculatorBrain.getResult()));
	// }

}
