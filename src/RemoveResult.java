class RemoveResult<TYPE>
{
	public boolean	removed;
	public boolean	shorter;
	public TYPE		value;

	public RemoveResult(boolean removed, boolean shorter, TYPE value)
	{
		this.removed = removed;
		this.shorter = shorter;
		this.value = value;
	}

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append('[').append(removed?"removed":"no removed").append(", ")
				.append(shorter?"shorter":"not shorter").append(", value: ").append(value).append(']');
		return buffer.toString();
	}
}