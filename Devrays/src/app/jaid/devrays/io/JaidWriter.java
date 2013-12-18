package app.jaid.devrays.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class JaidWriter {

	OutputStream	stream;

	public JaidWriter(OutputStream stream, int compressionLevel)
	{
		if (compressionLevel == -1)
			this.stream = stream;
		else
			this.stream = new DeflaterOutputStream(stream, new Deflater(0/* compressionLevel */));
	}

	public void close()
	{
		try
		{
			stream.close();
		} catch (IOException e)
		{
			app.jaid.devrays.debug.Log.e(e);
		}
	}

	public void write1Byte(int... values)
	{
		for (int value : values)
			try
			{
				stream.write((byte) value);
			} catch (IOException e)
			{
				app.jaid.devrays.debug.Log.e(e);
			}
	}

	public void write2Bytes(int... values)
	{
		for (int value : values)
			writeBytes(new byte[] {(byte) (value >> 8 & 0xff), (byte) value});
	}

	public void write3Bytes(int... values)
	{
		for (int value : values)
			writeBytes(new byte[] {(byte) value, (byte) (value >> 8 & 0xff), (byte) (value >> 16 & 0xff)});
	}

	public void write4Bytes(int... values) // SIGNED
	{
		for (int value : values)
			writeBytes(new byte[] {(byte) value, (byte) (value >> 8 & 0xff), (byte) (value >> 16 & 0xff), (byte) (value >> 24 & 0xff)});
	}

	public void writeBits(boolean... values)
	{
		if (values.length % 8 != 0)
			throw new IllegalArgumentException("Die Anzahl an Argumenten muss ein Vielfaches von 8 sein!");

	}

	public void writeBits(int... values)
	{
		boolean[] booleanValues = new boolean[values.length];

		for (int i = 0; i != values.length; i++)
			booleanValues[i] = values[i] != 0;

		writeBits(booleanValues);
	}

	public void writeBytes(byte[] bytes)
	{
		try
		{
			stream.write(bytes);
		} catch (IOException e)
		{
			app.jaid.devrays.debug.Log.e(e);
		}
	}

	public void writeNibbles(int... values)
	{
		if (values.length % 2 != 0)
			throw new IllegalArgumentException("Die Anzahl an Argumenten muss ein Vielfaches von 2 sein!");

		for (int i = 0; i != values.length; i += 2)
			;
	}

	public void writeString(String string)
	{
		write2Bytes(string.getBytes().length);

		try
		{
			writeBytes(string.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e)
		{
			app.jaid.devrays.debug.Log.e(e);
		}
	}
}
