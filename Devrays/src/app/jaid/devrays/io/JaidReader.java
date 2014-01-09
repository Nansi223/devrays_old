package app.jaid.devrays.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.zip.InflaterInputStream;

public class JaidReader {

	InputStream	stream;

	public JaidReader(InputStream stream)
	{
		this.stream = new InflaterInputStream(stream);
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

	public int read1Byte()
	{
		try
		{
			return stream.read();
		} catch (IOException e)
		{
			app.jaid.devrays.debug.Log.e(e);
		}
		return 0;
	}

	public int read2Bytes()
	{
		byte[] word = new byte[2];

		try
		{
			stream.read(word);
		} catch (IOException e)
		{
			app.jaid.devrays.debug.Log.e(e);
		}

		return ByteBuffer.wrap(word).asShortBuffer().get();
	}

	public int read4Bytes()
	{
		byte[] integer = new byte[4];

		try
		{
			stream.read(integer);
		} catch (IOException e)
		{
			app.jaid.devrays.debug.Log.e(e);
		}

		return ByteBuffer.wrap(integer).asIntBuffer().get();
	}

	public String readString()
	{
		int length = read2Bytes();
		byte[] bytes = new byte[length];

		try
		{
			stream.read(bytes);
		} catch (IOException e)
		{
			app.jaid.devrays.debug.Log.e(e);
		}

		try
		{
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			app.jaid.devrays.debug.Log.e(e);
		}

		return null;
	}

}
