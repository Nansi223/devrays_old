package app.jaid.devrays.packer;

public class Main {

	public static void main(String[] args)
	{
		BinWriter.write(new CodePacker().get(), "C:/ADT/eclipse/Workspace/Devrays/Devrays-android/assets/data/code.bin");
	}
}
