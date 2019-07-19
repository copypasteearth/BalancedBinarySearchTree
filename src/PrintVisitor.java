

public class PrintVisitor extends Visitor<String>
{
	private boolean	first	= true;

	public void finish()
	{
		System.out.print(']');
	}

	public void start()
	{
		System.out.print('[');
		first = true;
	}

	@Override
	public void visit(String data)
	{
		if (first)
			first = false;
		else
			System.out.print(", ");
		System.out.print(data);
	}
}
