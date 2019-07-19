

public class Tester
{
	public static void main(String[] args)
	{
		int keys[] = { 10, 5, 7, 20, 2, 14, 25, 30, 32 };

		String data[] = { "ten", "five", "seven", "twenty", "two", "fourteen",
				"twenty five", "thirty", "thirty two" };

		String insertTestSequence[][] = {
				{ "ten" },
				{ "five", "ten" },
				{ "five", "seven", "ten" },
				{ "five", "seven", "ten", "twenty" },
				{ "two", "five", "seven", "ten", "twenty" },
				{ "two", "five", "seven", "ten", "fourteen", "twenty" },
				{ "two", "five", "seven", "ten", "fourteen", "twenty",
						"twenty five" },
				{ "two", "five", "seven", "ten", "fourteen", "twenty",
						"twenty five", "thirty" },
				{ "two", "five", "seven", "ten", "fourteen", "twenty",
						"twenty five", "thirty", "thirty two" } };

		AVLSearchTree<Integer, String> tree = new AVLSearchTree<Integer, String>();
		System.out.println(tree);

		PrintVisitor printVisitor = new PrintVisitor();

		System.out.println("Testing insert process:");
		for (int i = 0; i < keys.length; i++)
		{
			tree.insert(keys[i], data[i]);

			ValidationVisitor validator = new ValidationVisitor(i + 1,
					insertTestSequence[i]);
			tree.traverseInorder(validator);
			if (validator.isValid())
				System.out.print("Passed: ");
			else
				System.out.print("Failed: ");
			printVisitor.start();
			tree.traverseInorder(printVisitor);
			printVisitor.finish();
			System.out.println();
			System.out.println(tree);
		}

		int notFoundKeys[] = { 0, 4, 15 };

		System.out.println("Testing search for missing keys:");
		for (int i = 0; i < 3; i++)
			try
			{
				tree.find(notFoundKeys[i]);
				System.out.print("Failed: Key ");
				System.out.print(notFoundKeys[i]);
				System.out.println(" found in list");
			}
			catch (KeyNotFoundException e)
			{
				System.out.print("Passed: Key ");
				System.out.print(notFoundKeys[i]);
				System.out.println(" not found in list");
			}

		int foundKeys[] = { 2, 25, 10, 7 };
		String foundData[] = { "two", "twenty five", "ten", "seven" };

		System.out.println("Testing search for found keys:");
		for (int i = 0; i < 4; i++)
			try
			{
				String foundValue = tree.find(foundKeys[i]);
				if (foundValue == foundData[i])
				{
					System.out.print("Passed: Key ");
					System.out.print(foundKeys[i]);
					System.out.print(" found in list with value ");
					System.out.println(foundValue);
				}
				else
				{
					System.out.print("Failed: Key ");
					System.out.print(foundKeys[i]);
					System.out.print(" found in list with value ");
					System.out.println(foundValue);
				}
			}
			catch (KeyNotFoundException e)
			{
				System.out.print("Failed: Key ");
				System.out.print(foundKeys[i]);
				System.out.println(" not found in list");
			}

		int removeKeys[] = { 7, 10, 5, 20, 14, 2, 25, 30, 32 };

		String removeData[] = { "seven", "ten", "five", "twenty", "fourteen",
				"two", "twenty five", "thirty", "thirty two" };

		String removeTestSequence[][] = {
				{ "two", "five", "ten", "fourteen", "twenty", "twenty five",
						"thirty", "thirty two" },
				{ "two", "five", "fourteen", "twenty", "twenty five", "thirty",
						"thirty two" },
				{ "two", "fourteen", "twenty", "twenty five", "thirty",
						"thirty two" },
				{ "two", "fourteen", "twenty five", "thirty", "thirty two" },
				{ "two", "twenty five", "thirty", "thirty two" },
				{ "twenty five", "thirty", "thirty two" },
				{ "thirty", "thirty two" }, { "thirty two" }, {} };

		System.out.println("Testing remove process:");
		for (int i = 0; i < keys.length; i++)
		{
			String removedValue = tree.remove(removeKeys[i]);

			ValidationVisitor validator = new ValidationVisitor(keys.length - i
					- 1, removeTestSequence[i]);
			tree.traverseInorder(validator);
			if (removedValue == removeData[i] && validator.isValid())
				System.out.print("Passed: ");
			else
				System.out.print("Failed: ");
			System.out.print("(");
			System.out.print(removedValue);
			System.out.print(") ");
			printVisitor.start();
			tree.traverseInorder(printVisitor);
			printVisitor.finish();
			System.out.println();
		}
	}
}
