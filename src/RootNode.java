class RootNode<KEY extends Comparable<? super KEY>, TYPE> extends
		Node<KEY, TYPE>
{
	private BinaryNode<KEY, TYPE>	link	= null;

	@Override
	public TYPE find(KEY theKey)
	{
		TYPE result;

		if (link != null)
			result = link.find(theKey);
		else
			throw new KeyNotFoundException();

		return result;
	}

	@Override
	public InsertResult insert(KEY theKey, TYPE theValue, Node<KEY, TYPE> parent)
	{
		InsertResult result;

		if (link == null)
		{
			link = new BinaryNode<KEY, TYPE>(theKey, theValue);
			result = new InsertResult(true, true);
		}
		else
			result = link.insert(theKey, theValue, this);

		return result;
	}

	@Override
	public void relink(Node<KEY, TYPE> oldChild, Node<KEY, TYPE> newChild)
	{
		if (newChild == null || newChild instanceof BinaryNode)
			if (oldChild == link)
				link = (BinaryNode<KEY, TYPE>) newChild;
			else
				throw new Error("child link to remove does not match the link");
		else
			throw new Error("replacement link is wrong type");
	}

	@Override
	public RemoveResult<TYPE> remove(KEY theKey, Node<KEY, TYPE> parent)
	{
		RemoveResult<TYPE> result;

		if (link != null)
			result = link.remove(theKey, this);
		else
			throw new KeyNotFoundException();

		return result;
	}

	@Override
	public void remove(Node<KEY, TYPE> child)
	{
		relink(child, null);
	}

	@Override
	public String toString()
	{
		return toString('\0', 0);
	}

	@Override
	public String toString(char associationType, int level)
	{
		StringBuffer buffer = new StringBuffer(super.toString(associationType,
				level));

		if (associationType != '\0')
			buffer.append(associationType).append(": ");

		buffer.append(getClass().getName());
		if (link != null)
			buffer.append('\n').append(link.toString('\0', level + 1));

		return buffer.toString();
	}

	@Override
	public void traverseInorder(Visitor<TYPE> visitor)
	{
		if (link != null) link.traverseInorder(visitor);
	}

	@Override
	public void traversePostorder(Visitor<TYPE> visitor)
	{
		if (link != null) link.traversePostorder(visitor);
	}

	@Override
	public void traversePreorder(Visitor<TYPE> visitor)
	{
		if (link != null) link.traversePreorder(visitor);
	}
}