class InsertResult
{
	public boolean	added;
	public boolean	taller;

	public InsertResult(boolean added, boolean taller)
	{
		this.added = added;
		this.taller = taller;
	}

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append('[').append(added?"added":"not added").append(", ")
				.append(taller?"taller":"not taller").append(']');
		return buffer.toString();
	}
}