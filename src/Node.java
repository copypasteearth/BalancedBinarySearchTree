abstract class Node<KEY, TYPE>
{
	public abstract TYPE find(KEY theKey);

	public abstract InsertResult insert(KEY theKey, TYPE theValue,
			Node<KEY, TYPE> parent);

	public abstract void relink(Node<KEY, TYPE> oldChild, Node<KEY, TYPE> newChild);

	public abstract RemoveResult<TYPE> remove(KEY theKey, Node<KEY, TYPE> parent);
	
	public abstract void remove(Node<KEY, TYPE> child);

	public String toString(char associationType, int level)
	{
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < level; i++)
			buffer.append('\t');
		
		return buffer.toString();
	}
	
	public abstract void traverseInorder(Visitor<TYPE> visitor);

	public abstract void traversePostorder(Visitor<TYPE> visitor);

	public abstract void traversePreorder(Visitor<TYPE> visitor);
}