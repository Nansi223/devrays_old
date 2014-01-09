package app.jaid.devrays.ui;

import app.jaid.Jtil;
import app.jaid.devrays.Devrays;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

public class NumericInput extends Table {

	public static class Listener {

		public void change(NumericInput input, int value, float floatValue)
		{}
	}

	class Filter implements TextFieldFilter {

		@Override
		public boolean acceptChar(TextField textField, char key)
		{
			if (key == ' ')
				Devrays.ui.setKeyboardFocus(null);

			return Character.isDigit(key) || commaDigits != 0 && key == '.' && textField.getText().indexOf('.') == -1;
		}
	}

	int			commaDigits;
	final float	defaultValue;
	ImageButton	increase, decrease;
	TextField	input;
	Listener	listener;
	float		number, min, max;

	public NumericInput(float number, float min, float max, final int commaDigits)
	{
		this.commaDigits = commaDigits;
		defaultValue = number;

		decrease = new ImageButton(Devrays.skin, "decrement");
		decrease.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				decrease();
			}
		});

		input = new TextField(String.valueOf(number), Devrays.skin);
		input.setTextFieldFilter(new Filter());
		input.addListener(new FocusListener() {

			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused)
			{
				if (!focused)
				{
					TextField textField = (TextField) actor;

					float value = textField.getText().length() != 0 ? Math.min(Math.max(NumericInput.this.min, Float.valueOf(textField.getText())), NumericInput.this.max) : defaultValue;

					if (commaDigits != 0)
						value = Jtil.roundCommaDigits(value, commaDigits);

					setValue(value);
				}
			}
		});

		increase = new ImageButton(Devrays.skin, "increment");
		increase.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				increase();
			}
		});

		add(input).width(40 + (String.valueOf(max).length() + commaDigits) * 10);
		add(increase).padLeft(-27).padTop(-23);
		add(decrease).padLeft(-27).padTop(23);

		setMin(min);
		setMax(max);
		setValue(number);
	}

	public void decrease()
	{
		if (number != min)
			setValue(number - 1f);
	}

	public void increase()
	{
		if (number != max)
			setValue(number + 1f);
	}

	public void setListener(Listener listener)
	{
		this.listener = listener;
	}

	public void setMax(float max)
	{
		this.max = max;
		input.setMaxLength(String.valueOf((int) max).length() + (commaDigits == 0 ? 0 : commaDigits + 1));
	}

	public void setMin(float min)
	{
		this.min = min;
	}

	public void setValue(float value)
	{
		number = value;

		if (decrease.isDisabled() && number > min)
			decrease.setDisabled(false);

		if (increase.isDisabled() && number < max)
			increase.setDisabled(false);

		if (number == min)
			decrease.setDisabled(true);
		if (number == max)
			increase.setDisabled(true);

		if (number < min)
		{
			setValue(min);
			return;
		}

		if (number > max)
		{
			setValue(max);
			return;
		}

		input.setText(commaDigits == 0 ? String.valueOf((int) number) : String.valueOf(number));
		notifyListeners();
	}

	private void notifyListeners()
	{
		if (listener != null)
			listener.change(this, (int) number, number);
	}

}
