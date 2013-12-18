package app.jaid.devrays.net;

import app.jaid.devrays.Devrays;
import app.jaid.devrays.debug.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncTask;

public class SessionSender {

	private static int			index, fail, sent;
	private static FileHandle	logtxt;
	private static String[]		unsent;

	public static void execute()
	{
		new AsyncExecutor(1).submit(new AsyncTask<Object>() {

			@Override
			public Object call() throws Exception
			{
				start();
				return null;
			}
		});
	}

	private static void iterate()
	{
		if (unsent.length > index)
			send(unsent[index++]);
		else
			Log.m("[LogSender Thread] Finished! (" + sent + " sent, " + fail + " failed)");
	}

	private static void removeFromLogtxt(String filename)
	{
		logtxt.writeString(logtxt.readString().replace(filename + " ", ""), false);
	}

	private static void send(final String filename)
	{
		FileHandle file = Devrays.file("log/sessions/" + filename);

		if (!file.exists())
		{
			Log.m("Logfile \"" + filename + "\" not found.");
			removeFromLogtxt(filename);
			iterate();
		}
		else
		{
			HttpRequest post = new HttpRequest(HttpMethods.POST);
			post.setUrl("http://sosman.eu/devrays/index.php?file=" + filename);
			post.setTimeOut(5000);
			post.setContent(file.read(), file.length());
			Gdx.net.sendHttpRequest(post, new HttpResponseListener() {

				@Override
				public void failed(Throwable t)
				{
					fail++;
					iterate();
				}

				@Override
				public void handleHttpResponse(HttpResponse httpResponse)
				{
					sent++;
					removeFromLogtxt(filename);
					Gdx.app.log("Devrays.SessionSender", "SessionSender Request done! Status " + httpResponse.getStatus().getStatusCode() + ": " + httpResponse.getResultAsString());
					iterate();
				}
			});
		}
	}

	private static void start()
	{
		logtxt = Devrays.file("log.txt");
		unsent = logtxt.readString().split(" ");
		Log.m(unsent.length == 0 ? "[LogSender Thread] Nothing to send!" : "[LogSender Thread] Built queue with " + unsent.length + " items.");
		iterate();
	}
}
