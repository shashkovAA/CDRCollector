package objects;

// ����� ResultOperation - ��� � ������� ����������� ��������� ��������.
// ������� �� ������ ������������� ������� ( object), ������� �� ������ ���������� �������� ���������� � ���� ��� ����� ���� 
//� ��������� ��������, ���������� � ���������� �������� (�����, ����� ������ � ������ �� �������������) - ����� ��� ������������ ������������� ��������.
// ����������� ResultOperation(Object object,String result) ������������ ��� ����������� ����������� ���������� ������, �� ���� ��� ��� ������������ (��� ���������� ����)

public class ResultOperation 
{
	private String result;
	private Object object;
	
	public ResultOperation(){};
	
	public ResultOperation(Object object,String result)
	{
		this.result = result;
		this.object = object;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	
	

}
