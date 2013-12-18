package app.jaid.devrays;

import java.util.zip.InflaterInputStream;

import app.jaid.devrays.meta.code.SDKData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

public class Meta {

	static public SDKData	sdk;

	static public void initSDKData()
	{
		if (sdk == null)
			sdk = new Json().fromJson(SDKData.class, new InflaterInputStream(Gdx.files.internal("data/code.bin").read()));
	}

}
