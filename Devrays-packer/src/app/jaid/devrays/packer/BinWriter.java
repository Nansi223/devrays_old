package app.jaid.devrays.packer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class BinWriter {

	public static void write(String content, String file)
	{
		try
		{
			DeflaterOutputStream stream = new DeflaterOutputStream(new FileOutputStream(file, false), new Deflater(9));
			stream.write(content.getBytes("UTF-8"));
			stream.flush();
			stream.close();

			System.out.println("[" + content.length() + " -> " + new File(file).length() + "] " + content);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}
