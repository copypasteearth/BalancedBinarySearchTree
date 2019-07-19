

public class ValidationVisitor extends Visitor<String>
{
	private String	validValues[];
	private int		expectedVisits;
	private int		visitCount;
	private boolean	valid;

	public ValidationVisitor(int expectedVisits, String validValues[])
	{
		reset(expectedVisits, validValues);
	}

	public boolean isValid()
	{
		return expectedVisits == visitCount && valid;
	}

	private void reset(int expectedVisits, String[] validValues)
	{
		this.validValues = validValues;
		this.expectedVisits = expectedVisits;
		this.visitCount = 0;
		this.valid = true;
	}

	@Override
	public void visit(String data)
	{
		if (data != validValues[visitCount++]){
			valid = false;
			System.out.println("data: " + data + "   :   " + validValues[visitCount - 1]);
		}
	}
}
