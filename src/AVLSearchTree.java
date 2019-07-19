public class AVLSearchTree<KEY extends Comparable<? super KEY>, TYPE>
{
	private RootNode<KEY, TYPE>	root	= new RootNode<KEY, TYPE>();

	private int					size;

	public TYPE find(KEY key)
	{
		TYPE result;

		if (!isEmpty())
			result = root.find(key);
		else
			throw new TreeEmptyException();

		return result;
	}

	public int getSize()
	{
		return size;
	}

	public void insert(KEY key, TYPE data)
	{
		if (!isFull())
		{
			InsertResult result = root.insert(key, data, null);
			if (result.added) size++;
		}
		else
			throw new TreeFullException();
	}

	public boolean isEmpty()
	{
		return size == 0;
	}

	public boolean isFull()
	{
		return false;
	}

	public TYPE remove(KEY key)
	{
		RemoveResult<TYPE> result;

		if (!isEmpty())
			result = root.remove(key, null);
		else
			throw new TreeEmptyException();

		size--;

		return result.value;
	}

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append(" [");
		if (isEmpty())
			buffer.append("empty]");
		else if (isFull())
			buffer.append("full]");
		else
			buffer.append("size: ").append(size).append("]\n").append(
					root.toString('\0', 1));

		return buffer.toString();
	}

	public void traverseInorder(Visitor<TYPE> visitor)
	{
		root.traverseInorder(visitor);
	}

	public void traversePostorder(Visitor<TYPE> visitor)
	{
		root.traversePostorder(visitor);
	}

	public void traversePreorder(Visitor<TYPE> visitor)
	{
		root.traversePreorder(visitor);
	}
}