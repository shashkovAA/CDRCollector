package objects;

// Класс ResultOperation - тип в котором возвращаетя результат действий.
// Состоит из самого возвращаемого объекта ( object), который на выходе средствами кастинга приводится к тому или иному типу 
//и строковое значение, сообщающее о результате операции (успех, текст ошибки в случае ее возникновения) - нужна для отлавливания нестандартных ситуаций.
// Конструктор ResultOperation(Object object,String result) используется для возможности возвращения анонимного класса, то есть без его инстанциации (для сокращения кода)

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
