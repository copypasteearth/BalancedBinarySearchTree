class BinaryNode<KEY extends Comparable<? super KEY>, TYPE> extends
		Node<KEY, TYPE>
{
	private enum BalanceFactor
	{
		EVEN_HIGH, LEFT_HIGH, RIGHT_HIGH
	}

	private BalanceFactor	balance;
	private KEY							key;
	private BinaryNode<KEY, TYPE>		left;
	private BinaryNode<KEY, TYPE>		right;
	private TYPE						value;

	public BinaryNode(KEY theKey, TYPE theValue)
	{
		key = theKey;
		value = theValue;
		balance = BalanceFactor.EVEN_HIGH;
		left = right = null;
	}

	@Override
	public TYPE find(KEY theKey)
	{
		TYPE result;

		if (theKey.compareTo(key)==0)
			result = value;
		else if (theKey.compareTo(key) < 0)
		{
			if (left != null)
				result = left.find(theKey);
			else
				throw new KeyNotFoundException();
		}
		else
		{
			if (right != null)
				result = right.find(theKey);
			else
				throw new KeyNotFoundException();
		}

		return result;
	}

	@Override
	public InsertResult insert(KEY theKey, TYPE theValue, Node<KEY, TYPE> parent)
	{
		InsertResult result = new InsertResult(true, true);

		if (theKey.compareTo(key) == 0)
			result.added = result.taller = false;
		else if (theKey.compareTo(key) < 0)
		{
			if (left == null)
			{
				left = new BinaryNode<KEY, TYPE>(theKey, theValue);

				switch (balance)
				{
					case LEFT_HIGH:
						throw new BalanceError();
					case EVEN_HIGH:
						balance = BalanceFactor.LEFT_HIGH;
						break;
					case RIGHT_HIGH:
						balance = BalanceFactor.EVEN_HIGH;
						result.taller = false;
				}
			}
			else
			{
				result = left.insert(theKey, theValue, this);

				if (result.taller)
				{
					switch (balance)
					{
						case LEFT_HIGH:
							insertLeftBalance(parent, result);
							break;
						case EVEN_HIGH:
							balance = BalanceFactor.LEFT_HIGH;
							break;
						case RIGHT_HIGH:
							balance = BalanceFactor.EVEN_HIGH;
							result.taller = false;
					}
				}
			}
		}
		else
		{
			if (right == null)
			{
				right = new BinaryNode<KEY, TYPE>(theKey, theValue);

				switch (balance)
				{
					case LEFT_HIGH:
						balance = BalanceFactor.EVEN_HIGH;
						result.taller = false;
						break;
					case EVEN_HIGH:
						balance = BalanceFactor.RIGHT_HIGH;
						break;
					case RIGHT_HIGH:
						throw new Error("incomplete");
				}
			}
			else
			{
				result = right.insert(theKey, theValue, this);

				if (result.taller)
				{
					switch (balance)
					{
						case LEFT_HIGH:
							balance = BalanceFactor.EVEN_HIGH;
							result.taller = false;
							break;
						case EVEN_HIGH:
							balance = BalanceFactor.RIGHT_HIGH;
							break;
						case RIGHT_HIGH:
							insertRightBalance(parent, result);
					}
				}
			}
		}

		return result;
	}

	@Override
	public void relink(Node<KEY, TYPE> oldChild, Node<KEY, TYPE> newChild)
	{
		if (newChild == null || newChild instanceof BinaryNode)
		{
			BinaryNode<KEY, TYPE> newBinaryChild = (BinaryNode<KEY, TYPE>) newChild;
			if (oldChild == left)
				left = newBinaryChild;
			else if (oldChild == right)
				right = newBinaryChild;
			else
				throw new Error("child link to remove does not match the link");
		}
		else
			throw new Error("replacement link is wrong type");
	}

	@Override
	public RemoveResult<TYPE> remove(KEY theKey, Node<KEY, TYPE> parent)
	{
		RemoveResult<TYPE> result;

		if (theKey.compareTo(key) == 0)
		{
			if (isLeaf())
			{
				result = new RemoveResult<TYPE>(true, true, value);
				parent.remove(this);
			}
			else
			{
				result = new RemoveResult<TYPE>(true, false, value);
				if (right != null)
				{
					BinaryNode<KEY, TYPE> sacrificialNode = right.getSmallest();
					copyFrom(sacrificialNode);
					right.remove(sacrificialNode.key, this);
				}
				else
				{
					BinaryNode<KEY, TYPE> sacrificialNode = left.getLargest();
					copyFrom(sacrificialNode);
					left.remove(sacrificialNode.key, this);
				}
			}
		}
		else if (theKey.compareTo(key) < 0)
		{
			if (left != null)
			{
				result = left.remove(theKey, this);
				if (result.shorter) deleteRightBalance(parent, result);
			}
			else
				throw new KeyNotFoundException();
		}
		else
		{
			if (right != null)
			{
				result = right.remove(theKey, this);
				if (result.shorter) deleteLeftBalance(parent, result);
			}
			else
				throw new KeyNotFoundException();
		}

		return result;
	}

	private void deleteLeftBalance(Node<KEY, TYPE> parent,
			RemoveResult<TYPE> result)
	{
		switch (balance)
		{
			case RIGHT_HIGH:
				balance = BalanceFactor.EVEN_HIGH;
				break;

			case EVEN_HIGH:
				balance = BalanceFactor.LEFT_HIGH;
				result.shorter = left == null;
				break;

			case LEFT_HIGH:
				BinaryNode<KEY, TYPE> leftTree = left;
				if (leftTree.balance == BalanceFactor.RIGHT_HIGH)
				{
					BinaryNode<KEY, TYPE> rightTree = leftTree.right;
					switch (rightTree.balance)
					{
						case RIGHT_HIGH:
							leftTree.balance = BalanceFactor.LEFT_HIGH;
							balance = BalanceFactor.EVEN_HIGH;
							break;

						case EVEN_HIGH:
							balance = BalanceFactor.EVEN_HIGH;
							leftTree.balance = BalanceFactor.EVEN_HIGH;
							break;

						case LEFT_HIGH:
							balance = BalanceFactor.RIGHT_HIGH;
							leftTree.balance = BalanceFactor.EVEN_HIGH;
					}
					rightTree.balance = BalanceFactor.EVEN_HIGH;
					left.rotateLeft(this);
					rotateRight(parent);
				}
				else
				{
					switch (leftTree.balance)
					{
						case LEFT_HIGH:
						case RIGHT_HIGH:
							balance = BalanceFactor.EVEN_HIGH;
							leftTree.balance = BalanceFactor.EVEN_HIGH;
							break;

						case EVEN_HIGH:
							balance = BalanceFactor.LEFT_HIGH;
							result.shorter = false;
					}
					rotateRight(parent);
				}
		}
	}

	private void deleteRightBalance(Node<KEY, TYPE> parent,
			RemoveResult<TYPE> result)
	{
		switch (balance)
		{
			case LEFT_HIGH:
				balance = BalanceFactor.EVEN_HIGH;
				break;

			case EVEN_HIGH:
				balance = BalanceFactor.RIGHT_HIGH;
				result.shorter = right == null;
				break;

			case RIGHT_HIGH:
				BinaryNode<KEY, TYPE> rightTree = right;
				if (rightTree.balance == BalanceFactor.LEFT_HIGH)
				{
					BinaryNode<KEY, TYPE> leftTree = rightTree.left;
					switch (leftTree.balance)
					{
						case LEFT_HIGH:
							rightTree.balance = BalanceFactor.RIGHT_HIGH;
							balance = BalanceFactor.EVEN_HIGH;
							break;

						case EVEN_HIGH:
							balance = BalanceFactor.EVEN_HIGH;
							rightTree.balance = BalanceFactor.EVEN_HIGH;
							break;

						case RIGHT_HIGH:
							balance = BalanceFactor.LEFT_HIGH;
							rightTree.balance = BalanceFactor.EVEN_HIGH;
					}
					leftTree.balance = BalanceFactor.EVEN_HIGH;
					right.rotateRight(this);
					rotateLeft(parent);
				}
				else
				{
					switch (rightTree.balance)
					{
						case LEFT_HIGH:
						case RIGHT_HIGH:
							balance = BalanceFactor.EVEN_HIGH;
							rightTree.balance = BalanceFactor.EVEN_HIGH;
							break;

						case EVEN_HIGH:
							balance = BalanceFactor.RIGHT_HIGH;
							result.shorter = false;
					}
					rotateLeft(parent);
				}
		}
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

	public String toString(char associationType, int level)
	{
		StringBuffer buffer = new StringBuffer(super.toString(associationType,
				level));

		if (associationType != '\0')
			buffer.append(associationType).append(": ");

		buffer.append(getClass().getName()).append(" [key: ").append(key)
				.append(", value: ").append(value).append(", balance: ")
				.append(balance).append(']');

		if (left != null)
			buffer.append('\n').append(left.toString('L', level + 1));
		if (right != null)
			buffer.append('\n').append(right.toString('R', level + 1));

		return buffer.toString();
	}

	@Override
	public void traverseInorder(Visitor<TYPE> visitor)
	{
		if (left != null) left.traverseInorder(visitor);
		visitor.visit(value);
		if (right != null) right.traverseInorder(visitor);
	}

	@Override
	public void traversePostorder(Visitor<TYPE> visitor)
	{
		if (left != null) left.traversePostorder(visitor);
		if (right != null) right.traversePostorder(visitor);
		visitor.visit(value);
	}

	@Override
	public void traversePreorder(Visitor<TYPE> visitor)
	{
		visitor.visit(value);
		if (left != null) left.traversePreorder(visitor);
		if (right != null) right.traversePreorder(visitor);
	}

	private void copyFrom(BinaryNode<KEY, TYPE> sacrificialNode)
	{
		key = sacrificialNode.key;
		value = sacrificialNode.value;
	}

	private BinaryNode<KEY, TYPE> getLargest()
	{
		BinaryNode<KEY, TYPE> result;

		if (right == null)
			result = this;
		else
			result = right.getLargest();

		return result;
	}

	private BinaryNode<KEY, TYPE> getSmallest()
	{
		BinaryNode<KEY, TYPE> result;

		if (left == null)
			result = this;
		else
			result = left.getSmallest();

		return result;

	}

	private void insertLeftBalance(Node<KEY, TYPE> parent, InsertResult result)
	{
		BinaryNode<KEY, TYPE> leftTree = left;
		switch (leftTree.balance)
		{
			case LEFT_HIGH:
				balance = BalanceFactor.EVEN_HIGH;
				leftTree.balance = BalanceFactor.EVEN_HIGH;
				rotateRight(parent);
				result.taller = false;
				break;

			case EVEN_HIGH:
				throw new BalanceError();

			case RIGHT_HIGH:
				BinaryNode<KEY, TYPE> rightTree = leftTree.right;
				switch (rightTree.balance)
				{
					case LEFT_HIGH:
						balance = BalanceFactor.RIGHT_HIGH;
						leftTree.balance = BalanceFactor.EVEN_HIGH;
						break;

					case EVEN_HIGH:
						balance = BalanceFactor.EVEN_HIGH;
						leftTree.balance = BalanceFactor.EVEN_HIGH;
						break;

					case RIGHT_HIGH:
						balance = BalanceFactor.EVEN_HIGH;
						leftTree.balance = BalanceFactor.LEFT_HIGH;
				}
				rightTree.balance = BalanceFactor.EVEN_HIGH;
				left.rotateLeft(this);
				rotateRight(parent);
				result.taller = false;
		}
	}

	private void insertRightBalance(Node<KEY, TYPE> parent, InsertResult result)
	{
		BinaryNode<KEY, TYPE> rightTree = right;
		switch (rightTree.balance)
		{
			case RIGHT_HIGH:
				balance = BalanceFactor.EVEN_HIGH;
				rightTree.balance = BalanceFactor.EVEN_HIGH;
				rotateLeft(parent);
				result.taller = false;
				break;

			case EVEN_HIGH:
				throw new BalanceError();

			case LEFT_HIGH:
				BinaryNode<KEY, TYPE> leftTree = rightTree.left;
				switch (leftTree.balance)
				{
					case RIGHT_HIGH:
						balance = BalanceFactor.LEFT_HIGH;
						rightTree.balance = BalanceFactor.EVEN_HIGH;
						break;

					case EVEN_HIGH:
						balance = BalanceFactor.EVEN_HIGH;
						rightTree.balance = BalanceFactor.EVEN_HIGH;
						break;

					case LEFT_HIGH:
						balance = BalanceFactor.EVEN_HIGH;
						rightTree.balance = BalanceFactor.RIGHT_HIGH;
				}
				leftTree.balance = BalanceFactor.EVEN_HIGH;
				right.rotateRight(this);
				rotateLeft(parent);
				result.taller = false;
		}
	}

	private boolean isLeaf()
	{
		return left == null && right == null;
	}

	private void rotateLeft(Node<KEY, TYPE> parent)
	{
		BinaryNode<KEY, TYPE> tempNode = right;
		right = tempNode.left;
		tempNode.left = this;
		parent.relink(this, tempNode);
	}

	private void rotateRight(Node<KEY, TYPE> parent)
	{
		BinaryNode<KEY, TYPE> tempNode = left;
		left = tempNode.right;
		tempNode.right = this;
		parent.relink(this, tempNode);
	}
}